package gov.pbc.xjcloud.provider.contract.service.impl.auditManage;

import gov.pbc.xjcloud.provider.contract.entity.auditManage.AuditPlanInfo;
import gov.pbc.xjcloud.provider.contract.mapper.auditManage.AuditPlanInfoMapper;
import gov.pbc.xjcloud.provider.contract.service.auditManage.AuditPlanInfoService;
import gov.pbc.xjcloud.provider.contract.service.impl.IBaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional(rollbackFor = {Exception.class})
public class AuditPlanInfoServiceImpl extends IBaseServiceImpl<AuditPlanInfoMapper, AuditPlanInfo> implements AuditPlanInfoService {

    @Resource
    private AuditPlanInfoMapper auditPlanInfoMapper;

    @Override
    public AuditPlanInfo getByPlanUserId(String planId, String userId) {
        return auditPlanInfoMapper.getByPlanUserId(planId, userId);
    }

    @Override
    public void updateById(String id, String status) {
        auditPlanInfoMapper.updateById(id, status);
    }

    @Override
    public void updateByPlanUserId(String planId, String userId, String status) {
        auditPlanInfoMapper.updateByPlanUserId(planId, userId, status);
    }

}
