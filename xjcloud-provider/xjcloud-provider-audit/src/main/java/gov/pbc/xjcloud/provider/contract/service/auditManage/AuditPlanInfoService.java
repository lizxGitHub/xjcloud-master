package gov.pbc.xjcloud.provider.contract.service.auditManage;

import com.baomidou.mybatisplus.extension.service.IService;
import gov.pbc.xjcloud.provider.contract.entity.auditManage.AuditPlanInfo;

public interface AuditPlanInfoService extends IService<AuditPlanInfo> {

    AuditPlanInfo getByPlanUserId(String planId, String userId);

    void updateById(String id, String status);

    void updateByPlanUserId(String planId, String userId, String status);
}
