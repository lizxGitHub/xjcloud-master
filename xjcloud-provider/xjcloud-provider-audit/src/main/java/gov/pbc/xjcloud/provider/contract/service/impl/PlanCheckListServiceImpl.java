package gov.pbc.xjcloud.provider.contract.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import gov.pbc.xjcloud.provider.contract.entity.PlanCheckListNew;
import gov.pbc.xjcloud.provider.contract.mapper.PlanCheckListNewMapper;
import gov.pbc.xjcloud.provider.contract.service.PlanCheckListService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional(rollbackFor = {Exception.class})
public class PlanCheckListServiceImpl extends IBaseServiceImpl<PlanCheckListNewMapper, PlanCheckListNew> implements PlanCheckListService {

    @Resource
    private PlanCheckListNewMapper planCheckListNewMapper;

    /**
     * 自定义分页查询
     *
     * @param page  分页对象
     * @param query 查询参数
     * @return
     */
    public Page<PlanCheckListNew> selectAll(Page page, PlanCheckListNew query, int type, int userId, String status) {
        List<PlanCheckListNew> list = planCheckListNewMapper.selectAll(page, query, type, userId, status);
        page.setRecords(list);
        return page;
    }

    @Override
    public PlanCheckListNew selectById(int id) {
        return planCheckListNewMapper.selectById(id);
    }

    @Override
    public int saveReturnPK(PlanCheckListNew planCheckList) {
        return planCheckListNewMapper.saveReturnPK(planCheckList);
    }
}
