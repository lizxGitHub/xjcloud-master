package gov.pbc.xjcloud.provider.contract.service.impl.auditManage;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import gov.pbc.xjcloud.provider.contract.entity.PlanCheckList;
import gov.pbc.xjcloud.provider.contract.mapper.auditManage.PlanManagementMapper;
import gov.pbc.xjcloud.provider.contract.service.auditManage.PlanManagementService;
import gov.pbc.xjcloud.provider.contract.service.impl.IBaseServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor = {Exception.class})
public class PlanManagementServiceImpl extends IBaseServiceImpl<PlanManagementMapper, PlanCheckList> implements PlanManagementService {

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

    @Override
    public List<Map<String, Object>> selectEntryByCategoryId(String categoryId) {
        return planManagementMapper.selectEntryByCategoryId(categoryId);
    }

    @Override
    public List<Map<String, Object>> selectEntryByQuery(PlanCheckList query, Long pageStart, Long pageNo) {
        String agencyId = query.getImplementingAgencyId();
        if(StringUtils.contains(agencyId,"all")){
            agencyId="";
        }
        query.setImplementingAgencyId(StringUtils.join(StringUtils.split(agencyId,","),"','"));
        if(StringUtils.equals(query.getAuditNatureId(),"all")){
            query.setAuditNatureId("");
        }
        if(StringUtils.equals(query.getAuditObjectId(),"all")){
            query.setAuditObjectId("");
        }
        if(StringUtils.equals(query.getProblemSeverityId(),"all")){
            query.setProblemSeverityId("");
        }
        if(StringUtils.equals(query.getRectifySituationId(),"all")){
            query.setRectifySituationId("");
        }

        return planManagementMapper.selectEntryByQuery(query,pageStart,pageNo);
    }

    @Override
    public int countEntryByQuery(PlanCheckList query) {
        return Integer.valueOf(planManagementMapper.countEntryByQuery(query).get(0).get("count").toString());
    }

    @Override
    public List<Map<String, Object>> groupCountEntryByQuery(PlanCheckList query, String groupName, String groupField) {
        return planManagementMapper.groupCountEntryByQuery(query, groupName, groupField);
    }

    @Override
    public List<Map<String, Object>> countPlan(String agencyId) {
        return planManagementMapper.countPlan(agencyId);
    }

    @Override
    public List<Map<String, Object>> statisticPlanReport(Long pageStart, Long pageNo) {
        return planManagementMapper.statisticPlanReport(pageStart, pageNo);
    }

    @Override
    public int countStatisticPlanReport() {
        return Integer.valueOf(planManagementMapper.countStatisticPlanReport().get(0).get("count").toString());
    }
    /**
     * 按计划完成类型查询分页数据
     * @param page
     * @param query
     * @return
     */
    public Page<PlanCheckList> selectTypePage(Page<PlanCheckList> page, Map<String, String> query) {
        List<PlanCheckList> list = planManagementMapper.selectTypePage(page,query);
        page.setRecords(list);
        return page;
    }

    public void addCheckAttention(String userId, String checkStr) {
        String[] checkArr = checkStr.split(",");
        planManagementMapper.cancelCheckAttention(userId,checkArr);
        planManagementMapper.addCheckAttention(userId,checkArr);
    }
}
