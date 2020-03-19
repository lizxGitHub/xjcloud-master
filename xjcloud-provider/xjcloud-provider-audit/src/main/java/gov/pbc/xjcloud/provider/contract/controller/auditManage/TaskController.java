package gov.pbc.xjcloud.provider.contract.controller.auditManage;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.enums.ApiErrorCode;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import gov.pbc.xjcloud.provider.contract.entity.PlanCheckList;
import gov.pbc.xjcloud.provider.contract.entity.PlanCheckListNew;
import gov.pbc.xjcloud.provider.contract.entity.PlanTimeTemp;
import gov.pbc.xjcloud.provider.contract.entity.auditManage.PlanFile;
import gov.pbc.xjcloud.provider.contract.entity.auditManage.PlanInfo;
import gov.pbc.xjcloud.provider.contract.enumutils.PlanStatusEnum;
import gov.pbc.xjcloud.provider.contract.feign.activiti.AuditActivitiService;
import gov.pbc.xjcloud.provider.contract.feign.user.UserCenterService;
import gov.pbc.xjcloud.provider.contract.service.impl.PlanCheckListServiceImpl;
import gov.pbc.xjcloud.provider.contract.service.impl.PlanTimeTempServiceImpl;
import gov.pbc.xjcloud.provider.contract.service.impl.auditManage.PlanInfoServiceImpl;
import gov.pbc.xjcloud.provider.contract.service.impl.auditManage.PlanManagementServiceImpl;
import gov.pbc.xjcloud.provider.contract.utils.IdGenUtil;
import gov.pbc.xjcloud.provider.contract.utils.PageUtil;
import gov.pbc.xjcloud.provider.contract.utils.R;
import gov.pbc.xjcloud.provider.contract.vo.ac.ActAuditVO;
import io.swagger.models.auth.In;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 接口审核
 */
@RestController
@RequestMapping("audit-api/task")
public class TaskController {
    /**
     * 流程服务接口
     */
    @Autowired
    AuditActivitiService activitiService;


    @Value("${audit.flow-key:auditApply}")
    private String auditFlowDefKey;

    @Resource
    private PlanCheckListServiceImpl planCheckListService;

    @Resource
    private PlanManagementServiceImpl planManagementService;

    @Resource
    private PlanInfoServiceImpl planInfoService;

    @Resource
    private PlanTimeTempServiceImpl planTimeTempService;

    @Autowired
    private UserCenterService userCenterService;

    @GetMapping(value = {"statuses", ""})
    public com.baomidou.mybatisplus.extension.api.R<Page<PlanCheckListNew>> statuses(PlanCheckListNew query, Page<PlanCheckListNew> page, String statuses) {
        PageUtil.initPage(page);
        String[] array = statuses.split(",");
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("size", 1000);
            params.put("current", 1);
            R<LinkedHashMap<String, Object>> actTaskMap = activitiService.todo(auditFlowDefKey, params);
            LinkedHashMap<String, Object> actTaskMapData = actTaskMap.getData();
            List<Map<String, String>> resultList = (List<Map<String, String>>) actTaskMapData.get("records");
            Map<Integer, Map<String, String>> collect = resultList.stream().filter(e -> StringUtils.isNotBlank(e.get("bizKey"))).collect(Collectors.toMap(e -> Integer.parseInt(e.get("bizKey")), e -> e));
            page = planCheckListService.selectByStatuses(page, query, array);
            page.getRecords().stream().filter(e -> e.getImplementingAgencyId() != null && e.getAuditObjectId() != null).forEach(e -> {
                if (null != collect.get(e.getId())) {
                    Map<String, String> taskInfo = collect.get(e.getId());
                    String taskId = taskInfo.get("taskId");
                    String taskName = taskInfo.get("taskName");
                    e.setTaskId(taskId);
                    e.setTaskName(taskName);
                    R<Integer> auditStatus = activitiService.getTaskVariable(taskId, "auditStatus");
                    R<String> rollbackText = activitiService.getTaskVariable(taskId, "rollbackText");
                    e.setAuditStatus(String.valueOf(auditStatus.getData()));
                    e.setRollbackText(String.valueOf(rollbackText.getData()));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return com.baomidou.mybatisplus.extension.api.R.ok(page);
    }

    /**
     * 流程页面
     *
     * @param query
     * @return
     */
    @GetMapping(value = {"/flow/page", ""})
    public com.baomidou.mybatisplus.extension.api.R<Page<PlanCheckListNew>> flowPage(HttpServletRequest request, PlanCheckListNew query, Page<PlanCheckListNew> page) {
        PageUtil.initPage(page);
        try {
            //获取代办
            Map<String, Object> params = new HashMap<>();
            params.put("size", 1000);
            params.put("current", 1);
            int type = 1;
            if (request.getParameter("type") != null) {
                type = Integer.parseInt(request.getParameter("type"));
            }
            int userId = 0;
            if (request.getParameter("userId") == null) {
                throw new Exception("用户id不为空");
            } else {
                userId = Integer.parseInt(request.getParameter("userId"));
            }
            String status = request.getParameter("status");
            R<LinkedHashMap<String, Object>> actTaskMap = activitiService.todo(auditFlowDefKey, params);
            LinkedHashMap<String, Object> actTaskMapData = actTaskMap.getData();
            List<Map<String, String>> resultList = (List<Map<String, String>>) actTaskMapData.get("records");
            Map<Integer, Map<String, String>> collect = resultList.stream().filter(e -> StringUtils.isNotBlank(e.get("bizKey"))).collect(Collectors.toMap(e -> Integer.parseInt(e.get("bizKey")), e -> e));
            page = planCheckListService.selectAll(page, query, type, userId, status);
            page.getRecords().stream().filter(e -> e.getImplementingAgencyId() != null && e.getAuditObjectId() != null).forEach(e -> {
                if (null != collect.get(e.getId())) {
                    Map<String, String> taskInfo = collect.get(e.getId());
                    String taskId = taskInfo.get("taskId");
                    String taskName = taskInfo.get("taskName");
                    e.setTaskId(taskId);
                    e.setTaskName(taskName);
                    R<Integer> auditStatus = activitiService.getTaskVariable(taskId, "auditStatus");
                    R<String> rollbackText = activitiService.getTaskVariable(taskId, "rollbackText");
                    e.setAuditStatus(String.valueOf(auditStatus.getData()));
                    e.setRollbackText(String.valueOf(rollbackText.getData()));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            return com.baomidou.mybatisplus.extension.api.R.failed(e.getMessage());
        }
        return com.baomidou.mybatisplus.extension.api.R.ok(page);
    }

    /**
     * 分页获取流程待办任务
     *
     * @param params 分页参数
     * @return
     */
    @GetMapping("todo")
    public R<Map<String, Object>> LustToDo(Map<String, Object> params) {
        try {
            if (!params.containsKey("size")) {
                params.put("size", 10);
            }
            if (!params.containsKey("current")) {
                params.put("current", 1);
            }
            R<LinkedHashMap<String, Object>> actTaskMap = activitiService.todo(auditFlowDefKey, params);
            LinkedHashMap<String, Object> actTaskMapData = actTaskMap.getData();
            List<ActAuditVO> resultList = ((List<Map>) actTaskMapData.get("records")).stream().filter(Objects::nonNull).map(actVO -> {
                String actJsonStr = JSONObject.toJSONString(actVO);
                ActAuditVO actAuditVO = JSONUtil.toBean(actJsonStr, ActAuditVO.class);
                R<Integer> auditStatus = activitiService.getTaskVariable(actAuditVO.getTaskId(), "auditStatus");
                R<String> projectName = activitiService.getTaskVariable(actAuditVO.getTaskId(), "projectName");
                R<String> projectCode = activitiService.getTaskVariable(actAuditVO.getTaskId(), "projectCode");
                R<String> implementingAgencyId = activitiService.getTaskVariable(actAuditVO.getTaskId(), "implementingAgencyId");
                R<String> auditObjectId = activitiService.getTaskVariable(actAuditVO.getTaskId(), "auditObjectId");
                R<String> auditNatureId = activitiService.getTaskVariable(actAuditVO.getTaskId(), "auditNatureId");
                R<String> auditYear = activitiService.getTaskVariable(actAuditVO.getTaskId(), "auditYear");
                R<String> status = activitiService.getTaskVariable(actAuditVO.getTaskId(), "status");
                actAuditVO.setAuditStatus(auditStatus.getData());
                actAuditVO.setProjectName(projectName.getData());
                actAuditVO.setProjectCode(projectCode.getData());
                actAuditVO.setImplementingAgencyId(implementingAgencyId.getData());
                actAuditVO.setAuditObjectId(auditObjectId.getData());
                actAuditVO.setAuditNatureId(auditNatureId.getData());
                actAuditVO.setAuditYear(auditYear.getData());
                actAuditVO.setStatus(status.getData());
                return actAuditVO;
            }).collect(Collectors.toList());
            actTaskMap.getData().put("records", resultList);
            return new R().setData(actTaskMap.getData());
        } catch (Exception e) {
            e.printStackTrace();
            return new R().setData(new ArrayList<>());
        }
    }

    /**
     * 办理流程任务
     *
     * @param params 分页参数
     * @return
     */
    @PostMapping("complete/{taskId}")
    public R complete(@PathVariable("taskId") String taskId, @RequestBody Map<String, Object> params) {
        R complete = new R();
        try {
            Float days = 0F; //整改天数
            Float daysPart = 0F; //整改天数
            Date startTimeAll = null;
            Date endTimeAll = null;
            Date startTimePartOne = null;
            Date endTimePartOne = null;
            Date startTimePartTwo = null;
            Date endTimePartTwo = null;
            //修改状态
            String planId = (String) params.get("bizKey");
            String opinion = ""; //驳回意见
            if (params.get("opinion") != null) {
                opinion = (String) params.get("opinion");
            }
            String delayDate = ""; //延迟天数
            if (params.get("delayDate") != null) {
                delayDate = (String) params.get("delayDate");
            }
            int status = (int) params.get("auditStatus");
            String statusStr = params.get("auditStatus").toString();
            String mark = "";
            if (params.get("mark") != null) {
                mark = params.get("mark").toString();
            }
            PlanCheckListNew plan = planCheckListService.getById(planId);
            PlanTimeTemp planTimeTemp = planTimeTempService.getByPlanId(plan.getId());
            if (PlanStatusEnum.PLAN_IMP_REJECT.getCode() == status && StringUtils.isNotBlank(planId)) {
                //审计对象管理员
                planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getAuditAdminId()), "1006");
            } else if (PlanStatusEnum.PLAN_AUDIT_PASS.getCode() == status && StringUtils.isNotBlank(planId)) {
                if (plan.getAuditUserId() == 0) {
                    return complete.setData(false);
                }
                //流程添加审计对象员工
                params.put("auditUserAssignee", plan.getAuditUserId());
//                //审计对象管理员
//                planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getAuditAdminId()), "1003");
                //审计对象一般员工
                PlanInfo planInfo1 = new PlanInfo();
                planInfo1.setUserId(plan.getAuditUserId());
                planInfo1.setStatusUser("1006"); //待完善
                planInfo1.setPlanId(plan.getId());
                planInfo1.setType(1);
                planInfoService.save(planInfo1);
            } else if (PlanStatusEnum.RECTIFY_INCOMPLETE.getCode() == status && StringUtils.isNotBlank(planId)) {
                if (StringUtils.isBlank(plan.getRectifyWay())) {
                    return complete.setData(false);
                }
                startTimePartOne = new Date();
                planTimeTemp.setStartTimePartOne(startTimePartOne);
//                //实施部门管理员
//                planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getImpAdminId()), "1001");
                //审计对象管理员
                planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getAuditAdminId()), "1006");
                //审计对象员工
                planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getAuditUserId()), "1002");
                //实施部门员工
                planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getImpUserId()), "1003");

            } else if (PlanStatusEnum.RECTIFY_REJECT.getCode() == status && StringUtils.isNotBlank(planId)) {
                planInfoService.updateProjectOpinionByPlanUserId(planId, String.valueOf(plan.getAuditAdminId()), opinion);
                endTimePartOne = new Date();
                daysPart = planTimeTemp.getDays() + daysOfTwo(planTimeTemp.getStartTimePartOne(), endTimePartOne);
                planTimeTemp.setDays(daysPart);
                //审计对象员工
                planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getAuditUserId()), "1003");
                //实施部门员工
                planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getImpUserId()), "1005");
//                //实施部门管理员
//                planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getImpAdminId()), "1002");
//                //审计对象管理员
//                planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getAuditAdminId()), "1005");
            } else if (PlanStatusEnum.RECTIFY_COMPLETE.getCode() == status && StringUtils.isNotBlank(planId)) {
                if ("1".equals(mark)) { //整改领导
                    planInfoService.updateProjectOpinionByPlanUserId(planId, String.valueOf(plan.getAuditUserId()), opinion);
//                    endTimePartTwo = new Date();
//                    daysPart = planTimeTemp.getDays() + daysOfTwo(planTimeTemp.getStartTimePartTwo(), endTimePartTwo);
//                    planTimeTemp.setDays(daysPart);
                    //审计对象员工
                    planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getAuditUserId()), "1003");
                    //审计对象管理员
                    planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getAuditAdminId()), "1002");
                } else if ("2".equals(mark)) { //实施员工
                    planInfoService.updateProjectOpinionByPlanUserId(planId, String.valueOf(plan.getAuditAdminId()), opinion);
                    endTimePartTwo = new Date();
                    daysPart = planTimeTemp.getDays() + daysOfTwo(planTimeTemp.getStartTimePartTwo(), endTimePartTwo);
                    planTimeTemp.setDays(daysPart);
                    //审计对象员工
                    planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getAuditUserId()), "1001");
                    //审计对象管理员
                    planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getAuditAdminId()), "1005");
                    //实施机构员工
                    planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getImpUserId()), "1002");
                } else if ("3".equals(mark)) { //实施领导
                    planInfoService.updateProjectOpinionByPlanUserId(planId, String.valueOf(plan.getImpUserId()), opinion);
                    endTimePartTwo = new Date();
                    daysPart = planTimeTemp.getDays() + daysOfTwo(planTimeTemp.getStartTimePartTwo(), endTimePartTwo);
                    planTimeTemp.setDays(daysPart);
                    //审计对象员工
                    planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getAuditUserId()), "1001");
                    //实施机构员工
                    planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getImpUserId()), "1002");
                    //实施机构领导
                    planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getImpAdminId()), "1002");
                } else {
                    endTimePartOne = new Date();
                    daysPart = planTimeTemp.getDays() + daysOfTwo(planTimeTemp.getStartTimePartOne(), endTimePartOne);
                    planTimeTemp.setDays(daysPart);

                    //项目整改结果录入时间
                    plan.setResultEnterTime(new Date());
                    planCheckListService.updatePlanById(plan);

//                    //实施部门一般员工
//                    planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getImpUserId()), "1003");
//                    //实施部门管理员
//                    planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getImpAdminId()), "1003");
//                    //审计对象管理员
//                    planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getAuditAdminId()), "1003");
//                    //审计对象一般员工
//                    planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getAuditUserId()), "1001");
                    //审计对象员工
                    planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getAuditUserId()), "1001");
                    //实施机构员工
                    planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getImpUserId()), "1002");
                }
            } else if (PlanStatusEnum.COMPLETE_TOBE_AUDIT.getCode() == status && StringUtils.isNotBlank(planId)) {
                if ("2".equals(mark)) { //延期整改批准过来的流程
                    endTimePartTwo = new Date();
                    daysPart = planTimeTemp.getDays() + daysOfTwo(planTimeTemp.getStartTimePartTwo(), endTimePartTwo);
                    planTimeTemp.setDays(daysPart);

                    plan.setStatus("1004"); //设置项目状态为延期整改
                    planCheckListService.updatePlanById(plan);
                }
                //审计对象员工
                planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getAuditUserId()), "1001");
                //实施机构员工
                planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getImpUserId()), "1001");
                //实施机构领导
                planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getImpAdminId()), "1003");
            } else if (PlanStatusEnum.IMP_AUDIT.getCode() == status && StringUtils.isNotBlank(planId)) {
//                if (StringUtils.isBlank(plan.getRectifyResult())) {
//                    return complete.setData(false);
//                }
                startTimePartTwo = new Date();
                planTimeTemp.setStartTimePartTwo(startTimePartTwo);
                //实施部门一般员工
                planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getImpUserId()), "1003");
                //审计对象一般员工
                planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getAuditUserId()), "1005");
                //审计对象管理员
                planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getAuditAdminId()), "1001");
            } else if (PlanStatusEnum.IMP_REJECT.getCode() == status && StringUtils.isNotBlank(planId)) {
                //实施部门一般员工
                planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getImpUserId()), "1004");
                //审计对象员工
                planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getAuditUserId()), "1002");
            } else if (PlanStatusEnum.IMP_PASS.getCode() == status && StringUtils.isNotBlank(planId)) {
                endTimePartTwo = new Date();
                daysPart = planTimeTemp.getDays() + daysOfTwo(planTimeTemp.getStartTimePartTwo(), endTimePartTwo);
                planTimeTemp.setDays(daysPart);
                endTimeAll = new Date();
                planTimeTemp.setEndTimeAll(endTimeAll);
                //项目实施结束
                plan.setStatus("1002"); //实施结束
                planCheckListService.updatePlanById(plan);

                //实施部门一般员工
                planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getImpUserId()), "1006");
                //实施部门管理员
                planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getImpAdminId()), "1005");
                //审计对象员工
                planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getAuditUserId()), "1004");
                //审计对象管理员
                planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getAuditAdminId()), "1006");
            } else if (PlanStatusEnum.FILE.getCode() == status && StringUtils.isNotBlank(planId)) {
                plan.setRectifyEvaluation(opinion);
                planCheckListService.updatePlanById(plan);
                days = daysOfTwo(planTimeTemp.getStartTimeAll(), planTimeTemp.getEndTimeAll()) - planTimeTemp.getDays();
                planTimeTemp.setDays(days);
                //项目实施结束
                plan.setStatus("1003"); //实施结束
                //项目归档时间
                plan.setArchiveTime(new Date());
                planCheckListService.updatePlanById(plan);
            } else if (PlanStatusEnum.DELAY_APPLY.getCode() == status && StringUtils.isNotBlank(planId)) {
                //更新延迟时间与说明
                plan.setDelayDate(delayDate);
                plan.setDelayRemarks(opinion);
                planCheckListService.updatePlanById(plan);
                startTimePartTwo = new Date();
                planTimeTemp.setStartTimePartTwo(startTimePartTwo);
                //审计对象员工
                planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getAuditUserId()), "1002");
                //审计对象领导
                planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getAuditAdminId()), "1001");
            } else if (PlanStatusEnum.DELAY_AUDIT_PASS.getCode() == status && StringUtils.isNotBlank(planId)) {
                //实施部门一般员工
                planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getImpUserId()), "1003");
                //审计对象员工
                planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getAuditUserId()), "1005");
                //审计对象领导
                planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getAuditAdminId()), "1003");
            } else if (PlanStatusEnum.DELAY_IMP_AUDIT.getCode() == status && StringUtils.isNotBlank(planId)) {
                //实施部门一般员工
                planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getImpUserId()), "1003");
                //实施部门管理员
                planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getImpAdminId()), "1001");
            } else if (PlanStatusEnum.DELAY_AUDIT.getCode() == status && StringUtils.isNotBlank(planId)) {
                if (StringUtils.isBlank(plan.getRectifyResult())) {
                    return complete.setData(false);
                }
                //实施部门一般员工
                planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getImpUserId()), "1002");
                //审计对象一般员工
                planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getAuditUserId()), "1002");
                //审计对象领导
                planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getAuditAdminId()), "1001");
            }

            planTimeTempService.updateById(planTimeTemp);

            if (StringUtils.isNotBlank((String) params.get("fileUri"))) {
                PlanFile planFile = new PlanFile();
                planFile.setId(IdGenUtil.uuid());
                //planFile.setTaskId(Integer.parseInt(taskId));
                planFile.setTaskName("");
                planFile.setFileUri(params.get("fileUri").toString());
                planFile.setBizKey(Integer.parseInt(planId));
                planFile.setUploadUser(params.get("uploadUser").toString());
                planFile.setCreatedTime(new Date());
                planManagementService.addFileLog(planFile);
            }

            params.remove("bizKey");
            params.remove("opinion");
            complete = activitiService.complete(taskId, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        complete.setData(true);
        return complete;
    }

    /**
     * 获取流程图
     *
     * @param processDefKey
     * @param businessId
     * @return
     */
    @GetMapping("getTaskView")
    public ResponseEntity taskView(@RequestParam(name = "processDefKey", required = true) String processDefKey, @RequestParam(name = "businessId", required = true) String businessId) {
        return activitiService.getTaskView(processDefKey, businessId);
    }

    /**
     * 对象装换map
     *
     * @param obj
     * @return
     */
    private static Map<String, Object> transBean2Map(Object obj) {
        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                // 过滤class属性
                if (!key.equals("class") && !key.equals("pageNo") && !key.equals("pageSize")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);
                    map.put(key, value);
                }
            }
        } catch (Exception e) {
            System.out.println("transBean2Map Error " + e);
        }
        return map;
    }

    /**
     * 获取流程业务记录
     *
     * @param processDefKey
     * @param businessId
     * @return
     */
    @GetMapping("taskHistory")
    public R taskHistory(@RequestParam(name = "processDefKey", required = true) String processDefKey, @RequestParam(name = "businessId", required = true) String businessId) {
        try {
            R<List<Map<String,Object>>> history = activitiService.history(processDefKey, businessId);
            List<Map<String, Object>> data = history.getData();
            Map<String,Object> start = new HashMap<>();
            PlanCheckList planCheckList = planManagementService.getById(Integer.valueOf(businessId));
            R<Map<String,Object>> user = userCenterService.user(planCheckList.getCreatedBy());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            start.put("taskId",0);
            start.put("taskName","项目启动");
            start.put("startTime", sdf.format(planCheckList.getStartTime()));
            start.put("endTime",sdf.format(planCheckList.getStartTime()));
            start.put("assigneeName",user.getData().get("username"));
            start.put("assigneeId",user.getData().get("userId"));
            data.add(start);
            List<Map<String, Object>> collect = data.stream().sorted((e1, e2) -> {
                Integer var1 = Integer.parseInt(e1.get("taskId").toString());
                Integer var2 = Integer.parseInt(e2.get("taskId").toString());
                return var2 - var1;
            }).collect(Collectors.toList());
            return history.setData(collect);
        } catch (Exception e) {
            e.printStackTrace();
            return new R().setData(new ArrayList<>());
        }
    }

    /**
     * 提交整改结果
     *
     * @param params
     * @return
     */
    @PostMapping("submitReport")
    public R<Boolean> submitReport(@RequestBody Map<String, Object> params) {
        try {
            if (null == params.get("taskId") || null == params.get("id")) {
                return new R().setCode((int) ApiErrorCode.FAILED.getCode()).setMsg("参数缺失").setData(false);
            }
            planManagementService.reportPlanAndTask(params);
            return new R().setData(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new R().setCode((int) ApiErrorCode.FAILED.getCode()).setMsg(e.getMessage()).setData(false);
        }

    }

    /**
     * 晚上整改计划
     *
     * @param params
     * @return
     */
    @PostMapping("completePlan")
    public R<Boolean> completePlan(@RequestBody Map<String, Object> params) {
        try {
            if (null == params.get("id")) {
                return new R().setCode((int) ApiErrorCode.FAILED.getCode()).setMsg("参数缺失").setData(false);
            }
            planManagementService.completePlan(params);
            return new R().setData(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new R().setCode((int) ApiErrorCode.FAILED.getCode()).setMsg("参数缺失").setData(false);
        }

    }

    /**
     * 算天数
     *
     * @param fDate
     * @param oDate
     * @return
     */
    public int daysOfTwo(Date fDate, Date oDate) {
        // 安全检查
        if (fDate == null || oDate == null) {
            throw new IllegalArgumentException("date is null, check it again");
        }
        // 根据相差的毫秒数计算
        int daysPart = (int) ((oDate.getTime() - fDate.getTime()) / (24 * 3600 * 1000));
        return daysPart;
    }

    public static String getStringDate(Date currentTime) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    @GetMapping("/plan/costdays/{bizKey}")
    public R<PlanTimeTemp> getBizWasteDay(@PathVariable Integer bizKey) {
        try {
            PlanTimeTemp byPlanId = planTimeTempService.getByPlanId(bizKey);
            return new R<>(byPlanId);
        } catch (Exception e) {
            e.printStackTrace();
            return new R().setMsg(e.getMessage()).setCode(1);
        }

    }


    /**
     * 获取部门用户的一般用户
     *
     * @param bizKey
     * @param deptId
     * @return
     */
    @GetMapping("dept/normal/user")
    public R getNormalUserByDept(String bizKey, Integer deptId, @RequestParam(required = false) String roleName) {

        if (StringUtils.isBlank(bizKey)) {
            return new R().setCode(0).setMsg("业务ID为空");
        }
        try {
            if (StringUtils.isBlank(roleName)) {
                roleName = "审计实施一般角色";
            }
            //获取实施部门的一般人员
            R r = userCenterService.getUsersByRoleNameAndDept(deptId, roleName);
            return r;
        } catch (Exception e) {
            e.printStackTrace();
            return new R().setCode(0).setMsg(e.getMessage());
        }
    }

    @PostMapping("commitAuditAdminUser")
    public R commitImpUser(Integer bizKey, Integer auditAdminId) {
        if (null == (bizKey) || null == (auditAdminId)) {
            return new R().setMsg("参数错误").setCode(1).setData(false);
        }
        PlanCheckListNew planCheckListNew = planCheckListService.selectById(bizKey);
        planCheckListNew.setAuditAdminId(auditAdminId);
        planCheckListService.updatePlanById(planCheckListNew);
        return new R().setData(true);
    }

    /**
     * @param id
     * @return
     */
    @GetMapping("user/{id}")
    public R getUserById(@PathVariable(name = "id") Integer id) {
        if (null == id) {
            return new R().setCode(1).setData(null).setMsg("参数为空");
        }
        return userCenterService.user(id);
    }
}
