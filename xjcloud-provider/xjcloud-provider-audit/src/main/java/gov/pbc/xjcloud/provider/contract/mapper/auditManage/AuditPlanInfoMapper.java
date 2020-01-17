package gov.pbc.xjcloud.provider.contract.mapper.auditManage;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import gov.pbc.xjcloud.provider.contract.entity.auditManage.AuditPlanInfo;
import gov.pbc.xjcloud.provider.contract.mapper.IBaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AuditPlanInfoMapper extends IBaseMapper<AuditPlanInfo> {
    /**
     * 查询分页对象
     * @param page
     * @param query
     * @return
     */
    List<AuditPlanInfo> selectAuditPlanInfoList(@Param("page") Page page, @Param("query") AuditPlanInfo query);

    AuditPlanInfo getById(@Param("id") String id, @Param("roleId") String roleId);
}
