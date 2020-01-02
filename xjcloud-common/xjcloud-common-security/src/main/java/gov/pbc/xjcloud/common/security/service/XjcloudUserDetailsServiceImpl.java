package gov.pbc.xjcloud.common.security.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import gov.pbc.xjcloud.common.core.constant.CommonConstants;
import gov.pbc.xjcloud.common.core.constant.SecurityConstants;
import gov.pbc.xjcloud.common.core.util.R;
import gov.pbc.xjcloud.provider.usercenter.api.dto.UserInfo;
import gov.pbc.xjcloud.provider.usercenter.api.entity.SysUser;
import gov.pbc.xjcloud.provider.usercenter.api.feign.RemoteUserService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@Service
@AllArgsConstructor
public class XjcloudUserDetailsServiceImpl implements UserDetailsService {
	private final RemoteUserService remoteUserService;
	private final CacheManager cacheManager;

	/**
	 * 用户密码登录
	 *
	 * @param username 用户名
	 * @return
	 * @throws UsernameNotFoundException
	 */
	@Override
	@SneakyThrows
	public UserDetails loadUserByUsername(String username) {
		Cache cache = cacheManager.getCache("user_details");
		if (cache != null && cache.get(username) != null) {
			return (XjcloudUser) cache.get(username).get();
		}

		R<UserInfo> result = remoteUserService.info(username, SecurityConstants.FROM_IN);
		UserDetails userDetails = getUserDetails(result);
		cache.put(username, userDetails);
		return userDetails;
	}

	/**
	 * 构建userdetails
	 *
	 * @param result 用户信息
	 * @return
	 */
	private UserDetails getUserDetails(R<UserInfo> result) {
		if (result == null || result.getData() == null) {
			throw new UsernameNotFoundException("用户不存在");
		}

		UserInfo info = result.getData();
		Set<String> dbAuthsSet = new HashSet<>();
		if (ArrayUtil.isNotEmpty(info.getRoles())) {
			// 获取角色
			Arrays.stream(info.getRoles()).forEach(roleId -> dbAuthsSet.add(SecurityConstants.ROLE + roleId));
			// 获取资源
			dbAuthsSet.addAll(Arrays.asList(info.getPermissions()));

		}
		Collection<? extends GrantedAuthority> authorities
				= AuthorityUtils.createAuthorityList(dbAuthsSet.toArray(new String[0]));
		SysUser user = info.getSysUser();
		boolean enabled = StrUtil.equals(user.getLockFlag(), CommonConstants.STATUS_NORMAL);
		// 构造security用户
		return new XjcloudUser(user.getUserId(), user.getDeptId(), info.getDeptName(), user.getTenantId(), user.getUsername(), SecurityConstants.BCRYPT + user.getPassword(), enabled,
				true, true, !CommonConstants.STATUS_LOCK.equals(user.getLockFlag()), authorities);
	}
}
