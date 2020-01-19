package gov.pbc.xjcloud.provider.contract.mapper.auditManage;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import gov.pbc.xjcloud.provider.contract.entity.auditManage.AuditProjectInfo;
import gov.pbc.xjcloud.provider.contract.mapper.IBaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AuditProjectInfoMapper extends IBaseMapper<AuditProjectInfo> {
    /**
     * 查询分页对象
     * @param page
     * @param query
     * @return
     */
    List<AuditProjectInfo> selectAuditProject(@Param("page") Page page, @Param("query") AuditProjectInfo query);

    AuditProjectInfo getById(@Param("id") String id, @Param("roleId") String roleId);

    void updateById(@Param("id") String id, @Param("status") String status);

    void updateByPlanId(@Param("planId") String planId, @Param("roleId") String roleId, @Param("status") String status);

    void insertA(@Param("query") AuditProjectInfo query);
}
