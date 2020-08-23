package gov.pbc.xjcloud.provider.contract.service.impl.auditManage;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import gov.pbc.xjcloud.provider.contract.dto.PlanCheckListDTO;
import gov.pbc.xjcloud.provider.contract.entity.PlanCheckList;
import gov.pbc.xjcloud.provider.contract.entity.PlanOverTimeTip;
import gov.pbc.xjcloud.provider.contract.entity.auditManage.PlanFile;
import gov.pbc.xjcloud.provider.contract.enumutils.PlanStatusEnum;
import gov.pbc.xjcloud.provider.contract.feign.activiti.AuditActivitiService;
import gov.pbc.xjcloud.provider.contract.mapper.auditManage.PlanManagementMapper;
import gov.pbc.xjcloud.provider.contract.service.IBaseService;
import gov.pbc.xjcloud.provider.contract.service.auditManage.PlanManagementService;
import gov.pbc.xjcloud.provider.contract.service.impl.IBaseServiceImpl;
import gov.pbc.xjcloud.provider.contract.utils.IdGenUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;

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
            query.setImplementingAgencyId(agencyId);
        }
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
        if (StringUtils.equals(query.getProjectType(), "all")) {
            query.setProjectType("");
        }
        if (StringUtils.equals(query.getAuditNatureId(), "all")) { //审计性质
            query.setAuditNatureId("");
        }
        if (StringUtils.equals(query.getCostTime(), "all")) { //审计性质
            query.setCostTime("");
        } else if (StringUtils.isNotBlank(query.getCostTime())) {
            dealtCostTimeParams(query);
        }
        if (StringUtils.equals(query.getOverTime(), "all")) { //审计性质
            query.setOverTime("");
        } else if (StringUtils.isNotBlank(query.getOverTime())) {
            calculateOverTime(query);
        }

        return planManagementMapper.selectEntryByQuery(query, pageStart, pageNo);
    }

    private void calculateOverTime(PlanCheckList query) {
        String overTime = query.getOverTime();
        Integer overTimeCase = Integer.parseInt(overTime);
        Integer overTimeStart = 0;
        Integer overTimeEnd = 0;
        switch (overTimeCase) {
            case 7:
                overTimeStart = (overTimeCase * 30);
                break;
            default:
                overTimeStart = (overTimeCase * 30);
                overTimeEnd = (overTimeCase * 30 + 30);
        }
        query.setOverTimeStart(overTimeStart == 0 ? "" : overTimeStart.toString());
        query.setOverTimeEnd(overTimeStart == 0 ? "" : overTimeEnd.toString());
    }

    private void dealtCostTimeParams(PlanCheckList query) {
        String costTimeStr = query.getCostTime();
        int caseTime = Integer.parseInt(costTimeStr);
        Integer overTimeStart = 0;
        Integer overTimeEnd = 0;
        switch (caseTime) {
            case 7:
                overTimeStart = (caseTime * 30-30);
                overTimeEnd = 365;
                break;
            case 8:
                overTimeStart = 365;
                break;
            default:
                overTimeStart = (caseTime * 30-30);
                overTimeEnd = (caseTime * 30 );
        }
        query.setCostTimeStart(overTimeStart == 0 ? "" : overTimeStart.toString());
        query.setCostTimeEnd(overTimeStart == 0 ? "" : overTimeEnd.toString());
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
    public List<Map<String, Object>> groupCountEntry(String auditYear) {
        return planManagementMapper.groupCountEntry(auditYear);
    }

    @Override
    public List<Map<String, Object>> groupCountProType(String auditYear, String deptId) {
        return planManagementMapper.groupCountProType(auditYear, deptId);
    }

    @Override
    public List<Map<String, Object>> countPlan(String agencyId, String auditYear) {
        return planManagementMapper.countPlan(agencyId, auditYear);
    }

    @Override
    public List<Map<String, Object>> statisticPlanReport(Long pageStart, Long pageNo, String auditYear) {
        return planManagementMapper.statisticPlanReport(pageStart, pageNo, auditYear);
    }

    @Override
    public int countStatisticPlanReport() {
        return Integer.valueOf(planManagementMapper.countStatisticPlanReport().get(0).get("count").toString());
    }

    @Override
    public List<Map<String, Object>> statisticPlanReportByDeptId(Long pageStart, Long pageNo, String auditYear, int deptId) {
        return planManagementMapper.statisticPlanReportByDeptId(pageStart, pageNo, auditYear, deptId);
    }

    @Override
    public int countStatisticPlanReportByDeptId(int deptId) {
        return Integer.valueOf(planManagementMapper.countStatisticPlanReportByDeptId(deptId).get(0).get("count").toString());
    }

    @Override
    public List<Map<String, Object>> getShortPlans(List deptChild, String status, String auditYear) {
        return planManagementMapper.getShortPlans(deptChild, status, auditYear);
    }

    @Override
    public List<Map<String, Object>> getShortPlansNew(int implementingAgencyId, String status, String auditYear) {
        return planManagementMapper.getShortPlansNew(implementingAgencyId, status, auditYear);
    }

    @Override
    public int saveReturnPK(PlanCheckList planCheckList) {
        return planManagementMapper.saveReturnPK(planCheckList);
    }

    @Override
    public List<PlanCheckListDTO> findDeadlinePlanList() {
        return this.planManagementMapper.findDeadlinePlanList();
    }

    @Override
    public PlanOverTimeTip findCheckListTip(PlanOverTimeTip dtoObj) {
        return this.planManagementMapper.findCheckListTip(dtoObj);
    }

    @Override
    public int insertTip(PlanOverTimeTip tip) {
        return this.planManagementMapper.insertTip(tip);
    }

    @Override
    public Map<String, Object> selectProNumAndOverTime(int implementingAgencyId, String auditYear) {
        return planManagementMapper.selectProNumAndOverTime(implementingAgencyId, auditYear);
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

    public void cancelCheckAttention(String userId, String checkStr) {
        String[] checkArr = checkStr.split(",");
        planManagementMapper.cancelCheckAttention(userId, checkArr);
    }

    public void saveDeptYearReport(String deptId, String auditYear, String content) {
        List<Map<String, Object>> report = planManagementMapper.selectDeptYearReport(deptId, auditYear);
        if (null != report && report.size() > 0) {
            planManagementMapper.updateDeptYearReport(deptId, auditYear, content);
        } else {
            planManagementMapper.addDeptYearReport(deptId, auditYear, content);
        }
    }

    public String loadDeptYearReportContent(String deptId, String auditYear) {
        String content = "";
        List<Map<String, Object>> report = planManagementMapper.selectDeptYearReport(deptId, auditYear);
        if (null != report && report.size() > 0) {
            return report.get(0).get("content") != null ? report.get(0).get("content").toString() : "";
        }
        return content;
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
        return planManagementMapper.getDeadlinePlanPage(page, query);
    }

    /**
     * 文件记录
     *
     * @param planFile
     */
    public void addFileLog(PlanFile planFile) {
        if (StringUtils.isBlank(planFile.getFileUri())) {
            return;
        }
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
     *
     * @param params
     */
    public void reportPlanAndTask(Map<String, Object> params) {
        PlanCheckList originalPlan = planManagementMapper.selectById(params.get("id").toString());
        originalPlan.setRectifyResult(params.get("rectifyResult").toString());
        originalPlan.setRectifySituationId((String) params.get("rectifySituationId"));
        UpdateWrapper<PlanCheckList> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("rectify_result", params.get("rectifyResult"));
        updateWrapper.set("rectify_Situation_Id", params.get("rectifySituationId"));
        updateWrapper.eq("id", params.get("id"));
//        updateWrapper.set("audit_status",params.get("status"));
        planManagementMapper.update(originalPlan, updateWrapper);
        //提交文件

        if (null != params.get("fileUri") && StringUtils.isNotBlank(params.get("fileUri").toString())) {
            PlanFile planFile = new PlanFile();
            planFile.setId(IdGenUtil.uuid());
//            planFile.setTaskId(Integer.parseInt(params.get("taskId").toString()));
            planFile.setTaskName(params.get("taskName").toString());
            planFile.setFileUri(params.get("fileUri").toString());
            planFile.setBizKey(Integer.parseInt(params.get("id").toString()));
            planFile.setUploadUser(params.get("uploadUser").toString());
            planFile.setCreatedTime(new Date());
            addFileLog(planFile);
        }
//        Map<String,Object> actVars = new HashMap<>();
//        actVars.put("rectifyResult",params.get("rectifyResult"));
//        actVars.put("auditStatus",params.get("status"));
//        actVars.put("status",params.get("status"));
//        auditActivitiService.complete(params.get("taskId").toString(),actVars);
    }

    /**
     * 完善计划
     *
     * @param params
     */
    public void completePlan(Map<String, Object> params) {
        PlanCheckList originalPlan = planManagementMapper.selectById(params.get("id").toString());
        UpdateWrapper<PlanCheckList> updateWrapper = new UpdateWrapper<>();
        if (null != params.get("rectifyResult")) {
            originalPlan.setRectifyResult(params.get("rectifyResult").toString());
            updateWrapper.set("rectify_result", originalPlan.getRectifyResult());
        }
        if (null != params.get("auditUser")) {
            originalPlan.setRectifyResult(params.get("auditUser").toString());
            updateWrapper.set("auditUser", originalPlan.getRectifyResult());
        }
        if (null != params.get("auditUserId")) {
            originalPlan.setAuditUserId(Integer.parseInt(params.get("auditUserId").toString()));
            updateWrapper.set("audit_user_id", originalPlan.getAuditUserId());
        }
        if (null != params.get("planTime")) {
            originalPlan.setPlanTime(params.get("planTime").toString());
            updateWrapper.set("plan_time", originalPlan.getPlanTime());
        }
        if (null != params.get("rectifyWay")) {
            originalPlan.setRectifyWay(params.get("rectifyWay").toString());
            updateWrapper.set("rectify_way", originalPlan.getRectifyWay());
        }
        updateWrapper.eq("id", originalPlan.getId());
        planManagementMapper.update(originalPlan, updateWrapper);
        //提交文件
        if (null != params.get("fileUri")) {
            PlanFile planFile = new PlanFile();
            planFile.setId(IdGenUtil.uuid());
//            planFile.setTaskId(Integer.parseInt(params.get("taskId").toString()));
            planFile.setTaskName(params.get("taskName").toString());
            planFile.setFileUri(params.get("fileUri").toString());
            planFile.setBizKey(Integer.parseInt(params.get("id").toString()));
            planFile.setUploadUser(params.get("uploadUser").toString());
            planFile.setCreatedTime(new Date());
            addFileLog(planFile);
        }
//        Map<String,Object> actVars = new HashMap<>();
//        actVars.put("planTime",params.get("planTime"));
//        actVars.put("rectifyWay",params.get("rectifyWay"));
//        actVars.put("auditStatus",params.get("status"));
//        actVars.put("status",params.get("status"));
//        auditActivitiService.complete(params.get("taskId").toString(),actVars);
    }

    /**
     * 分组统计
     *
     * @param params
     */
    public List<Map<String, Object>> groupCount(Map<String, Object> params) {
        //超时统计
        if (null != params.get("overTime") && params.get("overTime").toString().equals("all")) {
            List<Map<String, Object>> list = this.planManagementMapper.listAllWithDays(params);
            List<Map<String, Object>> result = calculateOverTime(list);
            return result;
        } else if (null != params.get("overTime") && !(params.get("overTime")).equals("all") && StringUtils.isNotBlank((String) params.get("overTime")))
            dealtOverTimeParams(params);
        else if (null != params.get("costTime") && params.get("costTime").toString().equals("all")) {
            List<Map<String, Object>> list = this.planManagementMapper.listAllWithDays(params);
            List<Map<String, Object>> result = calculateCostTime(list);
            return result;
        } else if (null != params.get("costTime") && !(params.get("costTime")).equals("all") && StringUtils.isNotBlank((String) params.get("costTime"))) {
            dealtCostTimeParams(params);
        }
        return planManagementMapper.groupCount(params);
    }

    private void dealtOverTimeParams(Map<String, Object> params) {
        String overTimeStr = (String) params.get("overTime");
        int caseTime = Integer.parseInt(overTimeStr);
        switch (caseTime) {
            case 7: {//超过6个月，第七个月开始计算
                int overTimeStart = (caseTime * 30);
                params.put("overTimeStart", overTimeStart);
                break;
            }
            default:
                int overTimeStart = (caseTime * 30);
                int overTimeEnd = (caseTime * 30 + 30);
                params.put("overTimeStart", overTimeStart);
                params.put("overTimeEnd", overTimeEnd);
        }
    }

    private void dealtCostTimeParams(Map<String, Object> params) {
        String costTimeStr = (String) params.get("costTime");
        int caseTime = Integer.parseInt(costTimeStr);
        switch (caseTime) {
            case 7: {//半年到一年
                int overTimeStart = (caseTime * 30-30);
                int overTimeEnd = 365;
                params.put("costTimeStart", overTimeStart);
                params.put("costTimeEnd", overTimeEnd);
                break;
            }
            case 8: {//一年以上
                int overTimeStart = 365;
                params.put("overTimeStart", overTimeStart);
                break;
            }
            default:
                int overTimeStart = (caseTime * 30-30);
                int overTimeEnd = (caseTime * 30 );
                params.put("overTimeStart", overTimeStart);
                params.put("overTimeEnd", overTimeEnd);
        }
    }

    private List<Map<String, Object>> calculateCostTime(List<Map<String, Object>> list) {
        List<Map<String, Object>> result = new ArrayList<>(list.size() / 2);
        for (AtomicInteger month = new AtomicInteger(1); month.get() <= 8; month.addAndGet(1)) {
            long count = list.stream().filter(e -> {
                Date startTimeAll = (Date) e.get("startTimeAll");
                Date endTimeAll = (Date) e.get("endTimeAll");
                if (null == endTimeAll) {
                    endTimeAll = new Date();
                }
                long costTime = diffDays(startTimeAll, endTimeAll);
                if (month.get() == 7) {
                    boolean lg = (costTime - 0 - 0) >= (month.get() * 30 - 30);
                    boolean gt = (costTime - 0 - 0) < 365;
                    return lg & gt;
                }
                if (month.get() == 8) {
                    return (costTime - 0 - 0) >= 365;
                }
                boolean lg = (costTime - 0 - 0) >= (month.get() * 30 - 30);
                boolean gt = (costTime - 0 - 0) < (month.get() * 30);
                return lg & gt;
            }).count();
            Map<String, Object> value = new HashMap<>();
            value.put("value", count);
            if (month.get() == 7) {
                value.put("name", String.format("整改%d个月小于一年", month.get() - 1));
            } else if (month.get() == 8) {
                value.put("name", String.format("整改一年以上", month.get() - 1));
            } else {
                value.put("name", String.format("整改%d个月", month.get()));
            }
            result.add(value);
        }
        return result;
    }

    private List<Map<String, Object>> calculateOverTime(List<Map<String, Object>> list) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (AtomicInteger month = new AtomicInteger(1); month.get() <= 7; month.addAndGet(1)) {
            long count = list.stream().filter(e -> {
                Long planTime = Long.parseLong(e.get("planTime").toString());
                Date startTimeAll = (Date) e.get("startTimeAll");
                Date endTimeAll = (Date) e.get("endTimeAll");
                if (null == endTimeAll) {
                    endTimeAll = new Date();
                }
                long costTime = diffDays(startTimeAll, endTimeAll);
                long delayDate = Long.parseLong(e.get("delayDate").toString());
                if (month.get() == 7) {
                    return (costTime - planTime + delayDate) >= (month.get() * 30);
                }
                boolean lg = (costTime - planTime + delayDate) >= (month.get() * 30);
                boolean gt = (costTime - planTime + delayDate) < (month.get() * 30 + 30);
                return lg & gt;
            }).count();
            Map<String, Object> value = new HashMap<>();
            value.put("value", count);
            if (month.get() == 7) {
                value.put("name", String.format("超时%d个月以上", month.get() - 1));
            } else {
                value.put("name", String.format("超时%d个月", month.get()));
            }
            result.add(value);
        }
        return result;
    }

    private long diffDays(Date startTimeAll, Date endTimeAll) {
        long start = (startTimeAll).getTime();
        long end = (endTimeAll).getTime();
        return (end - start) / (24 * 60 * 60 * 1000);
    }


    /**
     * Date转换为LocalDateTime
     *
     * @param date
     */
    public LocalDateTime date2LocalDateTime(Date date) {
        Instant instant = date.toInstant();//An instantaneous point on the time-line.(时间线上的一个瞬时点。)
        ZoneId zoneId = ZoneId.systemDefault();//A time-zone ID, such as {@code Europe/Paris}.(时区)
        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();

        System.out.println(localDateTime.toString());//2018-03-27T14:07:32.668
        System.out.println(localDateTime.toLocalDate() + " " + localDateTime.toLocalTime());//2018-03-27 14:48:57.453

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");//This class is immutable and thread-safe.@since 1.8
        System.out.println(dateTimeFormatter.format(localDateTime));//2018-03-27 14:52:57
        return localDateTime;
    }

    /**
     * 查询词条 by级别和类型
     * @param params
     * @return
     */
    public List<Map<String, Object>> selectEntryByKeyAndLevel(Map<String,String> params) {
        return planManagementMapper.selectEntryByKeyAndLevel(params);
    }
}
