package gov.pbc.xjcloud.common.data.interceptor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.security.core.GrantedAuthority;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import gov.pbc.xjcloud.common.core.constant.SecurityConstants;
import gov.pbc.xjcloud.common.core.exception.CheckedException;
import gov.pbc.xjcloud.common.data.annotation.DataScope;
import gov.pbc.xjcloud.common.security.service.XjcloudUser;
import gov.pbc.xjcloud.common.security.util.SecurityUtils;
import gov.pbc.xjcloud.provider.usercenter.api.entity.SysDept;
import gov.pbc.xjcloud.provider.usercenter.api.entity.SysRole;
import gov.pbc.xjcloud.provider.usercenter.api.feign.RemoteDeptService;
import gov.pbc.xjcloud.provider.usercenter.api.feign.RemoteRoleService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@AllArgsConstructor
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class DataScopeInterceptor implements Interceptor {

	private RemoteDeptService deptService;
	
	private RemoteRoleService roleService;

	@Override
	@SneakyThrows
	public Object intercept(Invocation invocation) {
		StatementHandler statementHandler = PluginUtils.realTarget(invocation.getTarget());
		MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
		// 非SELECT语句时直接返回
		MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
		if (!SqlCommandType.SELECT.equals(mappedStatement.getSqlCommandType()))
			return invocation.proceed();
		
		// 未标记DataScope注解，或未启用DataScope注解时直接返回
		DataScope dataScope = this.getDataScopeAnnotation(mappedStatement);
		if (dataScope == null || !dataScope.actived())
			return invocation.proceed();

		XjcloudUser user = SecurityUtils.getUser();
		if (user == null) {
			throw new CheckedException("Failed to get user info while update data scope.");
		}

		List<SysRole> roles = listRolesByUserAndPermission(user, dataScope.permission());
		
		List<Integer> deptIds = new ArrayList<Integer>();
		if (CollectionUtil.isNotEmpty(roles)) {
			SysRole role = roles.stream().min(Comparator.comparingInt(o -> o.getDsType())).get();
			
			Integer dsType = role.getDsType();
			switch (dsType) {
			case 0: // DataScopeTypeEnum.ALL.getType():
				return invocation.proceed();
			case 1: // DataScopeTypeEnum.SELF_AND_LOWER_LEVEL_INSTITUTION.getType():
				deptIds.add(user.getDeptId());
				Object deptObj = deptService.dept(user.getDeptId()).getData();
				SysDept dept = BeanUtil.toBean(deptObj, SysDept.class);
				Object deptsObj = deptService.children(dept.getParentId(), true).getData();
				List<SysDept> depts = Convert.toList(SysDept.class, deptsObj);
				deptIds.addAll(depts.stream().map(item -> { return item.getDeptId(); }).collect(Collectors.toList()));
				break;
			case 2: // DataScopeTypeEnum.SELF_INSTITUTION.getType():
				deptIds.add(user.getDeptId());
				Object deptObj2 = deptService.dept(user.getDeptId()).getData();
				SysDept dept2 = BeanUtil.toBean(deptObj2, SysDept.class);
				Object deptsObj2 = deptService.children(dept2.getParentId(), false).getData();
				List<SysDept> depts2 = Convert.toList(SysDept.class, deptsObj2);
				List<SysDept> scopedDepts = depts2.stream().filter(item -> item.getLevel().equals(dept2.getLevel())).collect(Collectors.toList());
				deptIds.addAll(scopedDepts.stream().map(item -> { return item.getDeptId(); }).collect(Collectors.toList()));
				break;
			case 3: // DataScopeTypeEnum.SELF_DEPARTMENT.getType():
				deptIds.add(user.getDeptId());
				break;
			}
		}
		
		String strDeptIds = CollectionUtil.join(deptIds, ",");
		if (strDeptIds == null || strDeptIds.length() == 0)
			strDeptIds = "-1";

		BoundSql boundSql = statementHandler.getBoundSql();
		// 获取到原始sql语句
		String sql = boundSql.getSql();
		String methodName = mappedStatement.getId().substring(mappedStatement.getId().lastIndexOf(".") + 1,
				mappedStatement.getId().length());
		boolean isPageCount = methodName.matches("(.*)_COUNT");
		String mSql = sqlRemould(isPageCount, sql, dataScope.scopeName(), strDeptIds);

		// 通过反射修改sql语句
		Field field = boundSql.getClass().getDeclaredField("sql");
		field.setAccessible(true);
		field.set(boundSql, mSql);
		return invocation.proceed();
	}

	private List<SysRole> listRolesByUserAndPermission(XjcloudUser user, String permission) {
		// 获取用户被赋予的角色ID
		List<String> roleIdList = user.getAuthorities()
				.stream().map(GrantedAuthority::getAuthority)
				.filter(authority -> authority.startsWith(SecurityConstants.ROLE))
				.map(authority -> authority.split("_")[1])
				.collect(Collectors.toList());
		
		// 根据注解中标注的权限信息，获取具备该权限的角色ID
		List<Integer> menuRoleIdList = (List<Integer>) roleService.getByPermission(permission).getData();
		List<String> strMenuRoleIdList = menuRoleIdList.stream().map(item -> {
			return item == null ? "" : item.toString();
		}).collect(Collectors.toList());
		
		// 对以上两个ID取交集，得出具备permission的角色集合
		roleIdList.retainAll(strMenuRoleIdList);
		if (CollectionUtil.isEmpty(roleIdList))
			return new ArrayList<SysRole>();
		return roleService.getByIds(roleIdList).getData();
	}

	@SneakyThrows
	private DataScope getDataScopeAnnotation(MappedStatement mappedStatement) {
		Class<?> classType = Class
				.forName(mappedStatement.getId().substring(0, mappedStatement.getId().lastIndexOf(".")));
		// 获取接口方法名
		String methodName = mappedStatement.getId().substring(mappedStatement.getId().lastIndexOf(".") + 1,
				mappedStatement.getId().length());
		for (Method method : classType.getDeclaredMethods()) {
			if (method.isAnnotationPresent(DataScope.class) && methodName.equals(method.getName())) {
				return method.getAnnotation(DataScope.class);
			}
		}
		return null;
	}

	/**
	 * 改造sql，添加权限
	 *
	 * @param sql
	 * @return
	 */
	private String sqlRemould(boolean isPageCount, String sql, String scopeName, String deptIds) {
		if (isPageCount) {
			final String COUNT_SQL = "SELECT count(0) FROM";
			sql = sql.substring(COUNT_SQL.length());
			sql = COUNT_SQL + "(select " + scopeName + " from " + sql + ") as temp where temp." + scopeName + " in (" + deptIds + ")";
		} else {
			int limitIndex = sql.indexOf("LIMIT ?");
			String limitStr = "";
			if (limitIndex != -1) {
				limitStr = sql.substring(sql.indexOf("LIMIT"));
				sql = sql.substring(0, limitIndex);
			}
			sql = "SELECT * FROM (" + sql + ") as temp where temp." + scopeName + " in (" + deptIds + ") " + limitStr;
		}
		return sql;
	}

	@Override
	public Object plugin(Object target) {
		if (target instanceof StatementHandler) {
			return Plugin.wrap(target, this);
		} else {
			return target;
		}
	}

	@Override
	public void setProperties(Properties properties) {
		// TODO Auto-generated method stub

	}

}
