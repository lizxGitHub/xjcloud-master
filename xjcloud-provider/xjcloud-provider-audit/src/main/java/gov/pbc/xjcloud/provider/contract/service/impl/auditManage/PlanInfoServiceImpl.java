package gov.pbc.xjcloud.provider.contract.service.impl.auditManage;

import gov.pbc.xjcloud.provider.contract.entity.auditManage.PlanInfo;
import gov.pbc.xjcloud.provider.contract.mapper.auditManage.PlanInfoMapper;
import gov.pbc.xjcloud.provider.contract.service.auditManage.PlanInfoService;
import gov.pbc.xjcloud.provider.contract.service.impl.IBaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional(rollbackFor = {Exception.class})
public class PlanInfoServiceImpl extends IBaseServiceImpl<PlanInfoMapper, PlanInfo> implements PlanInfoService {

    @Resource
    private PlanInfoMapper planInfoMapper;

    @Override
    public PlanInfo getPlanByPlanUserId(String planId, String userId) {
        return planInfoMapper.getPlanByPlanUserId(planId, userId);
    }

    @Override
    public PlanInfo getProjectByPlanUserId(String planId, String userId) {
        return planInfoMapper.getProjectByPlanUserId(planId, userId);
    }

    @Override
    public void updateById(String id, String statusUser) {
        planInfoMapper.updateById(id, statusUser);
    }

    @Override
    public void updatePlanByPlanUserId(String planId, String userId, String statusUser) {
        planInfoMapper.updatePlanByPlanUserId(planId, userId, statusUser);
    }

    @Override
    public void updateProjectByPlanUserId(String planId, String userId, String statusUser) {
        planInfoMapper.updateProjectByPlanUserId(planId, userId, statusUser);
    }

}
