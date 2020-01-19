package gov.pbc.xjcloud.provider.contract.service.impl.auditManage;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import gov.pbc.xjcloud.provider.contract.entity.auditManage.PlanInfo;
import gov.pbc.xjcloud.provider.contract.mapper.auditManage.PlanInfoMapper;
import gov.pbc.xjcloud.provider.contract.service.auditManage.PlanInfoService;
import gov.pbc.xjcloud.provider.contract.service.impl.IBaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional(rollbackFor = {Exception.class})
public class PlanInfoServiceImpl extends IBaseServiceImpl<PlanInfoMapper, PlanInfo> implements PlanInfoService {

    @Resource
    private PlanInfoMapper planInfoMapper;

    @Override
    public Page<PlanInfo> selectPlanInfoList(Page page, PlanInfo query) {
        List<PlanInfo> list =  planInfoMapper.selectPlanInfoList(page, query);
        page.setRecords(list);
        return page;
    }

    @Override
    public PlanInfo getById(String id) {
        return planInfoMapper.getById(id);
    }

    @Override
    public void updateByPlanId(String planId, String status) {
        planInfoMapper.updateByPlanId(planId, status);
    }

    @Override
    public void insertA(PlanInfo query) {
        planInfoMapper.insertA(query);
    }

}
