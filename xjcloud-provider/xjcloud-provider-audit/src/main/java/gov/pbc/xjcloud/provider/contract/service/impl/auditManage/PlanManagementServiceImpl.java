package gov.pbc.xjcloud.provider.contract.service.impl.auditManage;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import gov.pbc.xjcloud.provider.contract.entity.PlanCheckList;
import gov.pbc.xjcloud.provider.contract.enumutils.PlanStatusEnum;
import gov.pbc.xjcloud.provider.contract.mapper.auditManage.PlanManagementMapper;
import gov.pbc.xjcloud.provider.contract.service.auditManage.PlanManagementService;
import gov.pbc.xjcloud.provider.contract.service.impl.IBaseServiceImpl;
import gov.pbc.xjcloud.provider.contract.utils.R;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor = {Exception.class})
public class PlanManagementServiceImpl extends IBaseServiceImpl<PlanManagementMapper, PlanCheckList> implements PlanManagementService {

    @Resource
    private PlanManagementMapper planManagementMapper;

    /**
     * 自定义分页查询
     *
     * @param page  分页对象
     * @param query 查询参数
     * @return
     */
    public Page<PlanCheckList> selectPlanCheckList(Page page, PlanCheckList query) {
        List<PlanCheckList> list = planManagementMapper.selectPlanCheckList(page, query);
        page.setRecords(list);
        return page;
    }

    public Page<PlanCheckList> selectPlanCheckListByAdmin(Page page, PlanCheckList query) {
        List<PlanCheckList> list = planManagementMapper.selectPlanCheckListByAdmin(page, query);
        page.setRecords(list);
        return page;
    }

    @Override
    public List<Map<String, Object>> selectEntryByCategoryId(String categoryId) {
        return planManagementMapper.selectEntryByCategoryId(categoryId);
    }

    @Override
    public Map<String, Object> selectEntryById(String id) {
        return planManagementMapper.selectEntryById(id);
    }

    @Override
    public List<Map<String, Object>> selectEntryByQuery(PlanCheckList query, Long pageStart, Long pageNo) {
        String agencyId = query.getImplementingAgencyId();
        if (StringUtils.contains(agencyId, "all")) {
            agencyId = "";
        }
        query.setImplementingAgencyId(StringUtils.join(StringUtils.split(agencyId, ","), "','"));
        if (StringUtils.equals(query.getAuditNatureId(), "all")) {
            query.setAuditNatureId("");
        }
        if (StringUtils.equals(query.getAuditObjectId(), "all")) {
            query.setAuditObjectId("");
        }
        if (StringUtils.equals(query.getProblemSeverityId(), "all")) {
            query.setProblemSeverityId("");
        }
        if (StringUtils.equals(query.getRectifySituationId(), "all")) {
            query.setRectifySituationId("");
        }

        return planManagementMapper.selectEntryByQuery(query, pageStart, pageNo);
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

    @Override
    public List<Map<String, Object>> getShortPlans(List deptChild, String status, String auditYear) {
        return planManagementMapper.getShortPlans(deptChild, status, auditYear);
    }

    @Override
    public int saveReturnPK(PlanCheckList planCheckList) {
        return planManagementMapper.saveReturnPK(planCheckList);
    }

    /**
     * 按计划完成类型查询分页数据
     *
     * @param page
     * @param query
     * @return
     */
    public Page<PlanCheckList> selectTypePage(Page<PlanCheckList> page, Map<String, Object> query) {
        List<PlanCheckList> list = planManagementMapper.selectTypePage(page, query);
        page.setRecords(list);
        return page;
    }

    public void addCheckAttention(String userId, String checkStr) {
        String[] checkArr = checkStr.split(",");
        planManagementMapper.cancelCheckAttention(userId, checkArr);
        planManagementMapper.addCheckAttention(userId, checkArr);
    }

    /**
     * 关注列表
     *
     * @param page
     * @param query
     * @return
     */
    public Page<PlanCheckList> selectAttentionPage(Page<PlanCheckList> page, Map<String, Object> query) {
        return planManagementMapper.selectAttentionPage(page, query);
    }

    public int getDeadlinePlan(String userId) {
        Date now = DateTime.now().toDate();
        Map<String, Object> query = new HashMap<>();
        query.put("userId", userId);
        query.put("now", now);
        query.put("status", PlanStatusEnum.FILE.getCode());
        return planManagementMapper.findDeadlinePlan(query);
    }

    public Page<PlanCheckList> getDeadlinePlanPage(Map<String, Object> query, Page<PlanCheckList> page) {
        return  planManagementMapper.getDeadlinePlanPage(page,query);
    }
}
