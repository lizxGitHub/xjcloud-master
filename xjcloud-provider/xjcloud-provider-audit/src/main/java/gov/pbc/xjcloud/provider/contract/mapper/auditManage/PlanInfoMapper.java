package gov.pbc.xjcloud.provider.contract.mapper.auditManage;

import gov.pbc.xjcloud.provider.contract.entity.auditManage.PlanInfo;
import gov.pbc.xjcloud.provider.contract.mapper.IBaseMapper;
import org.apache.ibatis.annotations.Param;

public interface PlanInfoMapper extends IBaseMapper<PlanInfo> {

    PlanInfo getPlanByPlanUserId(@Param("planId") String planId, @Param("userId") String userId);

    PlanInfo getProjectByPlanUserId(@Param("planId") String planId, @Param("userId") String userId);

    void updateById(@Param("id") String id, @Param("statusUser") String statusUser);

    void updatePlanByPlanUserId(@Param("planId") String planId, @Param("userId") String userId, @Param("statusUser") String statusUser);

    void updateProjectByPlanUserId(@Param("planId") String planId, @Param("userId") String userId, @Param("statusUser") String statusUser);
}
