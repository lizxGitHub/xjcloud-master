package gov.pbc.xjcloud.provider.contract.service.impl;

import gov.pbc.xjcloud.provider.contract.entity.PlanTimeTemp;
import gov.pbc.xjcloud.provider.contract.mapper.PlanTimeTempMapper;
import gov.pbc.xjcloud.provider.contract.service.auditManage.PlanTimeTempService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional(rollbackFor = {Exception.class})
public class PlanTimeTempServiceImpl extends IBaseServiceImpl<PlanTimeTempMapper, PlanTimeTemp> implements PlanTimeTempService {

    @Resource
    private PlanTimeTempMapper planTimeTempMapper;

    @Override
    public PlanTimeTemp getByPlanId(int planId) {
        return planTimeTempMapper.getByPlanId(planId);
    }
}
