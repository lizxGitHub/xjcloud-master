package gov.pbc.xjcloud.provider.contract.mapper.auditManage;

import gov.pbc.xjcloud.provider.contract.entity.auditManage.AuditPlanInfo;
import gov.pbc.xjcloud.provider.contract.mapper.IBaseMapper;
import org.apache.ibatis.annotations.Param;

public interface AuditPlanInfoMapper extends IBaseMapper<AuditPlanInfo> {

    AuditPlanInfo getByPlanUserId(@Param("planId") String planId, @Param("userId") String userId);

    void updateById(@Param("id") String id, @Param("status") String status);

    void updateByPlanUserId(@Param("planId") String planId, @Param("userId") String userId, @Param("status") String status);
}
