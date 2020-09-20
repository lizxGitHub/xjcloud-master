package gov.pbc.xjcloud.provider.contract.service.auditManage;

import com.baomidou.mybatisplus.extension.service.IService;
import gov.pbc.xjcloud.provider.contract.entity.auditManage.AuditPlanInfo;

import java.util.List;
import java.util.Map;

public interface AuditPlanInfoService extends IService<AuditPlanInfo> {

    List<Map<String, Object>> getByPlanUserId(int planId, int userId);

    List<Map<String, Object>> getByPlanId(int planId);
}
