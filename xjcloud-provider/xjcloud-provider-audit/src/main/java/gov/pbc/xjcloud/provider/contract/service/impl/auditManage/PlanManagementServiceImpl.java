package gov.pbc.xjcloud.provider.contract.service.impl.auditManage;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import gov.pbc.xjcloud.provider.contract.entity.auditManage.PlanCheckList;
import gov.pbc.xjcloud.provider.contract.mapper.auditManage.PlanManagementMapper;
import gov.pbc.xjcloud.provider.contract.service.auditManage.PlanManagementService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional(rollbackFor = {Exception.class})
public class PlanManagementServiceImpl extends ServiceImpl<PlanManagementMapper, PlanCheckList> implements PlanManagementService {

    @Resource
    private PlanManagementMapper planManagementMapper;
    /**
     * 自定义分页查询
     * @param page 分页对象
     * @param query 查询参数
     * @return
     */
    public Page<PlanCheckList> selectPlanCheckList(Page page, PlanCheckList query) {
        List<PlanCheckList> list = planManagementMapper.selectPlanCheckList(page, query);
        page.setRecords(list);
        return page;
    }
}
