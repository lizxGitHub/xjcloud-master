package gov.pbc.xjcloud.provider.contract.service.impl.auditManage;

import gov.pbc.xjcloud.provider.contract.entity.auditManage.AuditPlanInfo;
import gov.pbc.xjcloud.provider.contract.mapper.auditManage.AuditPlanInfoMapper;
import gov.pbc.xjcloud.provider.contract.service.auditManage.AuditPlanInfoService;
import gov.pbc.xjcloud.provider.contract.service.impl.IBaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor = {Exception.class})
public class AuditPlanInfoServiceImpl extends IBaseServiceImpl<AuditPlanInfoMapper, AuditPlanInfo> implements AuditPlanInfoService {

    @Resource
    private AuditPlanInfoMapper auditPlanInfoMapper;

    @Override
    public List<Map<String, Object>> getByPlanUserId(int planId, int userId) {
        return auditPlanInfoMapper.getByPlanUserId(planId, userId);
    }

    @Override
    public List<Map<String, Object>> getByPlanId(int planId) {
        return auditPlanInfoMapper.getByPlanId(planId);
    }

}
