package gov.pbc.xjcloud.provider.contract.service.auditManage;

import com.baomidou.mybatisplus.extension.service.IService;
import gov.pbc.xjcloud.provider.contract.entity.auditManage.PlanInfo;

public interface PlanInfoService extends IService<PlanInfo> {

    PlanInfo getByPlanUserId(String planId, String userId);

    void updateById(String id, String statusUser);

    void updateByPlanUserId(String planId, String userId, String statusUser);
}
