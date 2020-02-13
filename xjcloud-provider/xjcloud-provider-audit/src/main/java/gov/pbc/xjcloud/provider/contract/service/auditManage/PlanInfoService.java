package gov.pbc.xjcloud.provider.contract.service.auditManage;

import com.baomidou.mybatisplus.extension.service.IService;
import gov.pbc.xjcloud.provider.contract.entity.auditManage.PlanInfo;

public interface PlanInfoService extends IService<PlanInfo> {

    PlanInfo getPlanByPlanUserId(String planId, String userId);

    PlanInfo getProjectByPlanUserId(String planId, String userId);

    void updateById(String id, String statusUser);

    void updatePlanByPlanUserId(String planId, String userId, String statusUser);

    void updateProjectOpinionByPlanUserId(String planId, String userId, String opinion);

    void updateProjectByPlanUserId(String planId, String userId, String statusUser);
}
