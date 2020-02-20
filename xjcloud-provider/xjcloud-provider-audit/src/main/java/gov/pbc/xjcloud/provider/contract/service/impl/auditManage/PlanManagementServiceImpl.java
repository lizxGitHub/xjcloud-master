package gov.pbc.xjcloud.provider.contract.service.impl.auditManage;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import gov.pbc.xjcloud.provider.contract.entity.PlanCheckList;
import gov.pbc.xjcloud.provider.contract.entity.auditManage.PlanFile;
import gov.pbc.xjcloud.provider.contract.enumutils.PlanStatusEnum;
import gov.pbc.xjcloud.provider.contract.feign.activiti.AuditActivitiService;
import gov.pbc.xjcloud.provider.contract.mapper.auditManage.PlanManagementMapper;
import gov.pbc.xjcloud.provider.contract.service.auditManage.PlanManagementService;
import gov.pbc.xjcloud.provider.contract.service.impl.IBaseServiceImpl;
import gov.pbc.xjcloud.provider.contract.utils.IdGenUtil;
import gov.pbc.xjcloud.provider.contract.utils.R;
import io.swagger.models.auth.In;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private AuditActivitiService auditActivitiService;

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

    /**
     * 文件记录
     * @param planFile
     */
    public void addFileLog(PlanFile planFile) {
        planManagementMapper.addFileLog(planFile);
    }
/**
 * 获取文件列表
 */
    public List<gov.pbc.xjcloud.provider.contract.vo.PlanFileVO> findFilesByBizKey(String bizKey) {
        return planManagementMapper.findFilesByBizKey(bizKey);
    }

    /**
     * 提交整改结果
     * @param params
     */
    public void reportPlanAndTask(Map<String, Object> params) {
        PlanCheckList originalPlan = this.getById(params.get("id").toString());
        originalPlan.setRectifyResult(params.get("rectifyResult").toString());
        UpdateWrapper<PlanCheckList> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("rectify_result",params.get("rectifyResult"));
        updateWrapper.eq("id",params.get("id"));
//        updateWrapper.set("audit_status",params.get("status"));
        this.update(originalPlan,updateWrapper);
        //提交文件

        if(null!=params.get("fileUri")){
            PlanFile planFile = new PlanFile();
            planFile.setId(IdGenUtil.uuid());
            planFile.setTaskId(Integer.parseInt(params.get("taskId").toString()));
            planFile.setTaskName(params.get("taskName").toString());
            planFile.setFileUri(params.get("fileUri").toString());
            planFile.setBizKey(Integer.parseInt(params.get("id").toString()));
            planFile.setUploadUser(params.get("uploadUser").toString());
            planFile.setCreatedTime(new Date());
            this.addFileLog(planFile);
        }
//        Map<String,Object> actVars = new HashMap<>();
//        actVars.put("rectifyResult",params.get("rectifyResult"));
//        actVars.put("auditStatus",params.get("status"));
//        actVars.put("status",params.get("status"));
//        auditActivitiService.complete(params.get("taskId").toString(),actVars);
    }

    /**
     * 完善计划
     * @param params
     */
    public void completePlan(Map<String, Object> params) {
        PlanCheckList originalPlan = this.getById(params.get("id").toString());
        if(null != params.get("rectifyResult")){
            originalPlan.setRectifyResult(params.get("rectifyResult").toString());
        }
        if(null != params.get("auditUserId")){
            originalPlan.setAuditUserId(Integer.parseInt(params.get("auditUserId").toString()));
        }
        if(null != params.get("planTime")){
            originalPlan.setPlanTime(params.get("planTime").toString());
        }
        if(null != params.get("rectifyWay")){
            originalPlan.setRectifyWay(params.get("rectifyWay").toString());
        }
        this.updateById(originalPlan);
        //提交文件
        if(null!=params.get("fileUri")){
            PlanFile planFile = new PlanFile();
            planFile.setId(IdGenUtil.uuid());
            planFile.setTaskId(Integer.parseInt(params.get("taskId").toString()));
            planFile.setTaskName(params.get("taskName").toString());
            planFile.setFileUri(params.get("fileUri").toString());
            planFile.setBizKey(Integer.parseInt(params.get("id").toString()));
            planFile.setUploadUser(params.get("uploadUser").toString());
            planFile.setCreatedTime(new Date());
            this.addFileLog(planFile);
        }
//        Map<String,Object> actVars = new HashMap<>();
//        actVars.put("planTime",params.get("planTime"));
//        actVars.put("rectifyWay",params.get("rectifyWay"));
//        actVars.put("auditStatus",params.get("status"));
//        actVars.put("status",params.get("status"));
//        auditActivitiService.complete(params.get("taskId").toString(),actVars);
    }
}
