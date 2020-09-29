package gov.pbc.xjcloud.provider.contract.controller.auditManage;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.enums.ApiErrorCode;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import gov.pbc.xjcloud.provider.contract.entity.PlanCheckList;
import gov.pbc.xjcloud.provider.contract.entity.PlanCheckListNew;
import gov.pbc.xjcloud.provider.contract.entity.PlanTimeTemp;
import gov.pbc.xjcloud.provider.contract.entity.auditManage.AuditPlanInfo;
import gov.pbc.xjcloud.provider.contract.entity.auditManage.PlanFile;
import gov.pbc.xjcloud.provider.contract.entity.auditManage.PlanInfo;
import gov.pbc.xjcloud.provider.contract.enumutils.PlanStatusEnum;
import gov.pbc.xjcloud.provider.contract.feign.activiti.AuditActivitiService;
import gov.pbc.xjcloud.provider.contract.feign.user.UserCenterService;
import gov.pbc.xjcloud.provider.contract.service.impl.PlanCheckListServiceImpl;
import gov.pbc.xjcloud.provider.contract.service.impl.PlanTimeTempServiceImpl;
import gov.pbc.xjcloud.provider.contract.service.impl.auditManage.AuditPlanInfoServiceImpl;
import gov.pbc.xjcloud.provider.contract.service.impl.auditManage.PlanInfoServiceImpl;
import gov.pbc.xjcloud.provider.contract.service.impl.auditManage.PlanManagementServiceImpl;
import gov.pbc.xjcloud.provider.contract.utils.IdGenUtil;
import gov.pbc.xjcloud.provider.contract.utils.PageUtil;
import gov.pbc.xjcloud.provider.contract.utils.R;
import gov.pbc.xjcloud.provider.contract.vo.ac.ActAuditVO;
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
import java.math.BigDecimal;
import java.text.DecimalFormat;
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
    private AuditPlanInfoServiceImpl auditPlanInfoServiceImpl;

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
            String auditFlowDefKeyStr = auditFlowDefKey;
            R<LinkedHashMap<String, Object>> actTaskMap = activitiService.todo(auditFlowDefKeyStr, params);
            R<LinkedHashMap<String, Object>> actTaskMap1 = activitiService.todo(auditFlowDefKeyStr + "_1", params);
            LinkedHashMap<String, Object> actTaskMapData = actTaskMap.getData();
            LinkedHashMap<String, Object> actTaskMapData1 = actTaskMap1.getData();
            List<Map<String, String>> resultList = (List<Map<String, String>>) actTaskMapData.get("records");
            List<Map<String, String>> resultList1 = (List<Map<String, String>>) actTaskMapData1.get("records");
            resultList.addAll(resultList1);
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
            String auditFlowDefKeyStr = auditFlowDefKey;
            R<LinkedHashMap<String, Object>> actTaskMap = activitiService.todo(auditFlowDefKeyStr, params);
            R<LinkedHashMap<String, Object>> actTaskMap1 = activitiService.todo(auditFlowDefKeyStr + "_1", params);
            LinkedHashMap<String, Object> actTaskMapData = actTaskMap.getData();
            LinkedHashMap<String, Object> actTaskMapData1 = actTaskMap1.getData();
            List<Map<String, String>> resultList = (List<Map<String, String>>) actTaskMapData.get("records");
            List<Map<String, String>> resultList1 = (List<Map<String, String>>) actTaskMapData1.get("records");
            resultList.addAll(resultList1);
            // 当前登录用户的流程数据
            Map<Integer, Map<String, String>> collect = resultList.stream().filter(e -> StringUtils.isNotBlank(e.get("bizKey"))).collect(Collectors.toMap(e -> Integer.parseInt(e.get("bizKey")), e -> e,(e1,e2)->e1));
            page = planCheckListService.selectAll(page, query, type, userId, status);
            page.getRecords().stream().filter(e -> e.getImplementingAgencyId() != null).forEach(e -> {
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
            String auditFlowDefKeyStr = auditFlowDefKey;
            R<LinkedHashMap<String, Object>> actTaskMap = activitiService.todo(auditFlowDefKeyStr, params);
            R<LinkedHashMap<String, Object>> actTaskMap1 = activitiService.todo(auditFlowDefKeyStr + "_1", params);
            LinkedHashMap<String, Object> actTaskMapData = actTaskMap.getData();
            LinkedHashMap<String, Object> actTaskMapData1 = actTaskMap1.getData();
            List<Map<String, String>> resultList0 = (List<Map<String, String>>) actTaskMapData.get("records");
            List<Map<String, String>> resultList1 = (List<Map<String, String>>) actTaskMapData1.get("records");
            resultList0.addAll(resultList1);
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
            String evaluation = ""; //整改评价
            if (params.get("evaluation") != null) {
                evaluation = (String) params.get("evaluation");
            }
            String delayDate = ""; //延迟天数
            if (params.get("delayDate") != null) {
                delayDate = (String) params.get("delayDate");
            }
            int userId = 0;
            if (params.get("userId") != null) {
                userId = (int) params.get("userId");
            }
            int status = (int) params.get("auditStatus");
            String statusStr = params.get("auditStatus").toString();
            String mark = "";
            if (params.get("mark") != null) {
                mark = params.get("mark").toString();
            }

            //驳回新逻辑
            int atUser = 0;
            int atNextUser = 0;

            PlanCheckListNew plan = planCheckListService.getById(planId);
            PlanTimeTemp planTimeTemp = planTimeTempService.getByPlanId(plan.getId());
            R<List> auditLeaderAssigneeListR = activitiService.getTaskVariable(taskId, "auditLeaderAssigneeList");
            List auditLeaderAssigneeList = auditLeaderAssigneeListR.getData();
            R<List> auditUserInnerListR = activitiService.getTaskVariable(taskId, "auditUserInnerList");
            List auditUserInnerList = auditUserInnerListR.getData();
            if (PlanStatusEnum.PLAN_IMP_REJECT.getCode() == status && StringUtils.isNotBlank(planId)) {
                //审计对象管理员
                for (int i = 0; i < auditLeaderAssigneeList.size(); i++) {
                    planInfoService.updateProjectByPlanUserId(planId, String.valueOf(auditLeaderAssigneeList.get(i)), "1004");
                }
//                planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getAuditAdminId()), "1004");
            } else if (PlanStatusEnum.PLAN_AUDIT_PASS.getCode() == status && StringUtils.isNotBlank(planId)) {
                if (plan.getAuditUserId() == 0) {
                    return complete.setData(false);
                }
                plan.setAuditAdminId(userId);
                //流程添加审计对象员工
                params.put("auditUserAssignee", plan.getAuditUserId());
                params.put("auditLeaderAssignee", userId);
                //审计对象一般员工
                PlanInfo planInfo1 = new PlanInfo();
                planInfo1.setUserId(plan.getAuditUserId());
                planInfo1.setStatusUser("1006"); //待完善
                planInfo1.setPlanId(plan.getId());
                planInfo1.setType(1);
                planInfoService.save(planInfo1);
                //审计对象管理员
                for (int i = 0; i < auditLeaderAssigneeList.size(); i++) {
                    planInfoService.updateProjectByPlanUserId(planId, String.valueOf(auditLeaderAssigneeList.get(i)), "1006");
                }
//                planInfoService.updateProjectByPlanUserId(planId, String.valueOf(userId), "1006");
            } else if (PlanStatusEnum.RECTIFY_INCOMPLETE.getCode() == status && StringUtils.isNotBlank(planId)) {
                if (StringUtils.isBlank(plan.getRectifyWay())) {
                    return complete.setData(false);
                }
                if (status != Integer.valueOf(plan.getAuditStatus1()) && plan.getAuditObjectIdNew() != null && !(plan.getAuditObjectIdNew().equals(plan.getImplementingAgencyId()))) {
                    params.put("auditStatus", 1019);
                    //内审人员
                    for (int j = 0; j < auditUserInnerList.size(); j++) {
                        planInfoService.updateProjectByPlanUserId(planId, String.valueOf(auditUserInnerList.get(j)), "1001");
                    }
                } else {
                    //审计对象员工
                    planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getAuditUserId()), "1002");
                    //实施部门员工
                    planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getImpUserId()), "1003");
                }
                startTimePartOne = new Date();
                planTimeTemp.setStartTimePartOne(startTimePartOne);
            } else if (PlanStatusEnum.RECTIFY_REJECT.getCode() == status && StringUtils.isNotBlank(planId)) {
                if ("4".equals(mark)) {
                    planInfoService.updateProjectOpinionByPlanUserId(planId, String.valueOf(plan.getAuditUserId()), opinion);
                    atUser = plan.getAuditUserId();
                    //内审人员
                    for (int j = 0; j < auditUserInnerList.size(); j++) {
                        atNextUser = Integer.valueOf(String.valueOf(auditUserInnerList.get(j)));
                        planInfoService.updateProjectByPlanUserId(planId, String.valueOf(auditUserInnerList.get(j)), "1007");
                    }
                    //审计对象员工
                    planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getAuditUserId()), "1001");
                } else {
                    atUser = plan.getAuditUserId();
                    atNextUser = plan.getImpUserId();
                    planInfoService.updateProjectOpinionByPlanUserId(planId, String.valueOf(plan.getAuditUserId()), opinion);
                    //审计对象员工
                    planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getAuditUserId()), "1003");
                    //实施部门员工
                    planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getImpUserId()), "1005");
                }
                endTimePartOne = new Date();
                daysPart = planTimeTemp.getDays() + daysOfTwo(planTimeTemp.getStartTimePartOne(), endTimePartOne);
                planTimeTemp.setDays(daysPart);
            } else if (PlanStatusEnum.RECTIFY_COMPLETE.getCode() == status && StringUtils.isNotBlank(planId)) {
                if ("1".equals(mark)) { //整改领导
                    atUser = plan.getAuditUserId();
                    atNextUser = plan.getAuditAdminId();
                    planInfoService.updateProjectOpinionByPlanUserId(planId, String.valueOf(plan.getAuditUserId()), opinion);
//                    endTimePartTwo = new Date();
//                    daysPart = planTimeTemp.getDays() + daysOfTwo(planTimeTemp.getStartTimePartTwo(), endTimePartTwo);
//                    planTimeTemp.setDays(daysPart);
                    //审计对象员工
                    planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getAuditUserId()), "1003");
                    //审计对象管理员
                    for (int i = 0; i < auditLeaderAssigneeList.size(); i++) {
                        planInfoService.updateProjectByPlanUserId(planId, String.valueOf(auditLeaderAssigneeList.get(i)), "1002");
                    }
//                    planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getAuditAdminId()), "1002");
                } else if ("2".equals(mark)) { //实施员工
                    atUser = plan.getAuditAdminId();
                    atNextUser = plan.getImpUserId();
                    for (int i = 0; i < auditLeaderAssigneeList.size(); i++) {
                        planInfoService.updateProjectOpinionByPlanUserId(planId, String.valueOf(auditLeaderAssigneeList.get(i)), opinion);
                    }
//                    planInfoService.updateProjectOpinionByPlanUserId(planId, String.valueOf(plan.getAuditAdminId()), opinion);
                    endTimePartTwo = new Date();
                    daysPart = planTimeTemp.getDays() + daysOfTwo(planTimeTemp.getStartTimePartTwo(), endTimePartTwo);
                    planTimeTemp.setDays(daysPart);
                    //审计对象员工
                    planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getAuditUserId()), "1001");
                    //审计对象管理员
                    for (int i = 0; i < auditLeaderAssigneeList.size(); i++) {
                        planInfoService.updateProjectByPlanUserId(planId, String.valueOf(auditLeaderAssigneeList.get(i)), "1005");
                    }
//                    planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getAuditAdminId()), "1005");
                    //实施机构员工
                    planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getImpUserId()), "1002");
                } else if ("3".equals(mark)) { //实施领导
                    atUser = plan.getImpUserId();
                    atNextUser = plan.getImpAdminId();
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
                } else if ("4".equals(mark)) { //内审人员
                    planInfoService.updateProjectOpinionByPlanUserId(planId, String.valueOf(plan.getAuditUserId()), opinion);
                    endTimePartTwo = new Date();
                    daysPart = planTimeTemp.getDays() + daysOfTwo(planTimeTemp.getStartTimePartTwo(), endTimePartTwo);
                    planTimeTemp.setDays(daysPart);
                    //内审人员
                    atUser = plan.getAuditAdminId();
                    for (int i = 0; i < auditUserInnerList.size(); i++) {
                        atNextUser = Integer.valueOf(String.valueOf(auditUserInnerList.get(i)));
                        planInfoService.updateProjectByPlanUserId(planId, String.valueOf(auditUserInnerList.get(i)), "1007");
                    }
                    //审计对象员工
                    planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getAuditUserId()), "1001");
                    //审计对象管理员
                    for (int i = 0; i < auditLeaderAssigneeList.size(); i++) {
                        planInfoService.updateProjectByPlanUserId(planId, String.valueOf(auditLeaderAssigneeList.get(i)), "1005");
                    }
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
                if (status != Integer.valueOf(plan.getAuditStatus1()) && plan.getAuditObjectIdNew() != null && !(plan.getAuditObjectIdNew().equals(plan.getImplementingAgencyId()))) {
                    params.put("auditStatus", 1020);
                    //内审人员
                    for (int j = 0; j < auditUserInnerList.size(); j++) {
                        planInfoService.updateProjectByPlanUserId(planId, String.valueOf(auditUserInnerList.get(j)), "1001");
                    }
                } else {
                    //实施部门一般员工
                    planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getImpUserId()), "1003");
                    //审计对象一般员工
                    planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getAuditUserId()), "1005");
                    //审计对象管理员
                    for (int i = 0; i < auditLeaderAssigneeList.size(); i++) {
                        planInfoService.updateProjectByPlanUserId(planId, String.valueOf(auditLeaderAssigneeList.get(i)), "1001");
                    }
//                    planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getAuditAdminId()), "1001");
                }
                startTimePartTwo = new Date();
                planTimeTemp.setStartTimePartTwo(startTimePartTwo);
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
                //实施一般员工评价
                plan.setEvaluation(evaluation);
                //实施部门一般员工
                planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getImpUserId()), "1006");
                //实施部门管理员
                planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getImpAdminId()), "1005");
                //审计对象员工
                planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getAuditUserId()), "1004");
                //审计对象管理员
                for (int i = 0; i < auditLeaderAssigneeList.size(); i++) {
                    planInfoService.updateProjectByPlanUserId(planId, String.valueOf(auditLeaderAssigneeList.get(i)), "1006");
                }
//                planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getAuditAdminId()), "1006");
            } else if (PlanStatusEnum.FILE.getCode() == status && StringUtils.isNotBlank(planId)) {
                atUser = plan.getAuditAdminId();
                atNextUser = plan.getImpUserId();
                plan.setRectifyEvaluation(opinion);
                days = daysOfTwo(planTimeTemp.getStartTimeAll(), planTimeTemp.getEndTimeAll()) - planTimeTemp.getDays();
                planTimeTemp.setDays(days);
                //项目实施结束
                plan.setStatus("1003"); //实施结束
                //项目归档时间
                plan.setArchiveTime(new Date());
            } else if (PlanStatusEnum.DELAY_APPLY.getCode() == status && StringUtils.isNotBlank(planId)) {
                //更新延迟时间与说明
                plan.setDelayDate(delayDate);
                plan.setDelayRemarks(opinion);
                startTimePartTwo = new Date();
                planTimeTemp.setStartTimePartTwo(startTimePartTwo);
                //审计对象员工
                planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getAuditUserId()), "1002");
                //审计对象领导
                for (int i = 0; i < auditLeaderAssigneeList.size(); i++) {
                    planInfoService.updateProjectByPlanUserId(planId, String.valueOf(auditLeaderAssigneeList.get(i)), "1001");
                }
//                planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getAuditAdminId()), "1001");
            } else if (PlanStatusEnum.DELAY_AUDIT_PASS.getCode() == status && StringUtils.isNotBlank(planId)) {
                if (status != Integer.valueOf(plan.getAuditStatus1()) && plan.getAuditObjectIdNew() != null && !(plan.getAuditObjectIdNew().equals(plan.getImplementingAgencyId()))) {
                    params.put("auditStatus", 1021);
                    //内审人员
                    for (int j = 0; j < auditUserInnerList.size(); j++) {
                        planInfoService.updateProjectByPlanUserId(planId, String.valueOf(auditUserInnerList.get(j)), "1001");
                    }
                } else {
                    //实施部门一般员工
                    planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getImpUserId()), "1003");
                    //审计对象员工
                    planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getAuditUserId()), "1005");
                    //审计对象领导
                    for (int i = 0; i < auditLeaderAssigneeList.size(); i++) {
                        planInfoService.updateProjectByPlanUserId(planId, String.valueOf(auditLeaderAssigneeList.get(i)), "1003");
                    }
//                    planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getAuditAdminId()), "1003");
                }
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
                for (int i = 0; i < auditLeaderAssigneeList.size(); i++) {
                    planInfoService.updateProjectByPlanUserId(planId, String.valueOf(auditLeaderAssigneeList.get(i)), "1001");
                }
//                planInfoService.updateProjectByPlanUserId(planId, String.valueOf(plan.getAuditAdminId()), "1001");
            }

            //更新时间
            planTimeTempService.updateById(planTimeTemp);
            //更新计划
//            plan.setAuditStatus(statusStr);
            plan.setAuditStatus1(statusStr);
            planCheckListService.updatePlanById(plan);

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

            //新驳回逻辑
            if (StringUtils.isNotBlank(opinion)) {
                Date time = new Date();
                AuditPlanInfo auditPlanInfo = new AuditPlanInfo();
                auditPlanInfo.setPlanId(plan.getId());
                auditPlanInfo.setUserId(atUser);
                auditPlanInfo.setOpinion(opinion);
                auditPlanInfo.setNextUserId(atNextUser);
                auditPlanInfo.setCreateTime(time);
                auditPlanInfo.setTaskCode(status);
                auditPlanInfoServiceImpl.save(auditPlanInfo);
            }

            params.remove("bizKey");
            params.remove("opinion");
            params.remove("userId");
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
        processDefKey = auditFlowDefKey;
        PlanCheckListNew plan = planCheckListService.selectById(Integer.valueOf(businessId));
        if (plan.getAuditObjectIdNew() != null && !(plan.getAuditObjectIdNew().equals(plan.getImplementingAgencyId()))) {
            processDefKey = auditFlowDefKey + "_1";
        }
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
            processDefKey = auditFlowDefKey;
            PlanCheckListNew plan = planCheckListService.selectById(Integer.valueOf(businessId));
            if (plan.getAuditObjectIdNew() != null && !(plan.getAuditObjectIdNew().equals(plan.getImplementingAgencyId()))) {
                processDefKey = auditFlowDefKey + "_1";
            }
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
                roleName = "内审实施人员角色";
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

    /**
     * @param
     * @return
     */
    @GetMapping("/days")
    public String initDays(Integer planId) {
        float days = 0f;
        if (planId != null && planId != 0) {
            PlanCheckListNew plan = planCheckListService.selectById(planId);
            Date currentTime = new Date();
            Date startTime = plan.getStartTime() == null? currentTime : plan.getStartTime();
            String auditStatus1 = plan.getAuditStatus1();
            ArrayList<String> strArray = new ArrayList(Arrays.asList("1004", "1005", "1007", "1008", "1009", "1017", "1014"));
            if (strArray.contains(auditStatus1)) { // 1005 1007 1008 1009 1017 1014
                long diff = currentTime.getTime() - startTime.getTime();
                double f1 = new BigDecimal((float)diff/(1000 * 60 * 60 * 24)).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
                days = (float) Math.ceil(f1);
                if (days <= 0) {
                    days = 1;
                }
//                days = diff;
                PlanTimeTemp planTimeTemp = planTimeTempService.getByPlanId(planId);
                planTimeTemp.setDays(days);
                planTimeTempService.updateById(planTimeTemp);
            }
        }
        return String.valueOf((int)days);
    }

    @PostMapping("/editBatchAuditUser")
    public R<Boolean> editBatchAuditUser(
            @RequestParam(name = "params", required = true) String params,
            @RequestParam(name = "auditUser", required = true) String auditUser) {
        R<Boolean> r = new R<>();
        //获取代办
        Map<String, Object> map = new HashMap<>();
        String[] paramsArray = params.split(",");
        for (String param : paramsArray) {
            String id = param.split("_")[0];
            String taskId = param.split("_")[1];
            PlanCheckListNew plan = planCheckListService.selectById(Integer.valueOf(id));
            plan.setAuditUserId(Integer.valueOf(auditUser));
            //更新计划
            plan.setAuditStatus1("1005");
            planCheckListService.updatePlanById(plan);

            //审计对象一般员工
            PlanInfo planInfo = new PlanInfo();
            planInfo.setUserId(plan.getAuditUserId());
            planInfo.setStatusUser("1006"); //待完善
            planInfo.setPlanId(plan.getId());
            planInfo.setType(1);
            planInfoService.save(planInfo);
            //审计对象管理员
            planInfoService.updateProjectByPlanUserId(id, String.valueOf(plan.getAuditAdminId()), "1006");


            map.put("auditStatus", 1005);
            map.put("auditUserAssignee", plan.getAuditUserId());
            //流程
            r = activitiService.complete(taskId, map);
        }
        return r.setData(true);
    }

    @PostMapping("/editBatchDown")
    public R<Boolean> editBatchDown(
            @RequestParam(name = "params", required = true) String params,
            @RequestParam(name = "auditUser", required = true) String auditUser) {
        R<Boolean> r = new R<>();
        //获取代办
        Map<String, Object> map = new HashMap<>();
        String[] paramsArray = params.split(",");
        for (String param : paramsArray) {
            String id = param.split("_")[0];
            String taskId = param.split("_")[1];
            PlanCheckListNew plan = planCheckListService.selectById(Integer.valueOf(id));
            plan.setAuditUserId(Integer.valueOf(auditUser));
            //更新计划
            plan.setAuditStatus1("1005");
            planCheckListService.updatePlanById(plan);

            //审计对象一般员工
            PlanInfo planInfo = new PlanInfo();
            planInfo.setUserId(plan.getAuditUserId());
            planInfo.setStatusUser("1006"); //待完善
            planInfo.setPlanId(plan.getId());
            planInfo.setType(1);
            planInfoService.save(planInfo);
            //审计对象管理员
            planInfoService.updateProjectByPlanUserId(id, String.valueOf(plan.getAuditAdminId()), "1006");


            map.put("auditStatus", 1005);
            map.put("auditUserAssignee", plan.getAuditUserId());
            //流程
            r = activitiService.complete(taskId, map);
        }
        return r.setData(true);
    }

    @PostMapping("/editBatchArchive")
    public R<Boolean> editBatchArchive(
            @RequestParam(name = "params", required = true) String params,
            @RequestParam(name = "opinion", required = true) String opinion) {
        R<Boolean> r = new R<>();
        //获取代办
        Map<String, Object> map = new HashMap<>();
        String[] paramsArray = params.split(",");
        for (String param : paramsArray) {
            String id = param.split("_")[0];
            String taskId = param.split("_")[1];
            PlanCheckListNew plan = planCheckListService.selectById(Integer.valueOf(id));
            plan.setRectifyEvaluation(opinion);
            //项目实施结束
            plan.setStatus("1003"); //实施结束
            //项目归档时间
            plan.setArchiveTime(new Date());
            //更新计划
            plan.setAuditStatus1("1013");
            planCheckListService.updatePlanById(plan);

            map.put("auditStatus", 1013);
            //流程
            r = activitiService.complete(taskId, map);
        }
        return r.setData(true);
    }

    @PostMapping("/editBatchDownDeptById")
    public R<Boolean> editBatchDownDeptById(
            @RequestParam(name = "params", required = true) String params,
            @RequestParam(name = "auditStatus", required = true) String auditStatus,
            @RequestParam(name = "auditObjectId", required = true) String auditObjectId) {
        R<Boolean> r = new R<>();
        //获取代办
        Map<String, Object> map = new HashMap<>();
        String[] paramsArray = params.split(",");
        for (String param : paramsArray) {
            String id = param.split("_")[0];
            String taskId = param.split("_")[1];

            PlanCheckListNew plan = planCheckListService.selectById(Integer.valueOf(id));
            if (!auditObjectId.equals(plan.getAuditObjectId())) {
//                //删除之前的审计对象管理员
//                List listOld = (List)userCenterService.getUsersByRoleNameAndDept(Integer.valueOf(plan.getAuditObjectId()), "审计对象负责人员角色").getData();
//                if (listOld.size() < 1) {
//                    return r.setData(false);
//                }
//                for (int i = 0; i < listOld.size(); i++) {
//                    Map m = (Map) listOld.get(i);
//                    planInfoService.deleteProjectByPlanUserId(id, String.valueOf(m.get("userId")));
//                }

                plan.setAuditObjectId(auditObjectId);
                //更新计划
                plan.setAuditStatus1(auditStatus);
                planCheckListService.updatePlanById(plan);

                //审计对象管理员更新
                List list = (List)userCenterService.getUsersByRoleNameAndDept(Integer.valueOf(plan.getAuditObjectId()), "审计对象负责人员角色").getData();
                if (list.size() < 1) {
                    return r.setData(false);
                }
                //流程中人员更改
                List<Integer> auditLeaderAssigneeList = new ArrayList<>();
                //审计对象管理员
                for (int i = 0; i < list.size(); i++) {
                    Map m = (Map) list.get(i);
                    PlanInfo planInfo3 = new PlanInfo();
                    planInfo3.setUserId(Integer.valueOf(String.valueOf(m.get("userId"))));
                    planInfo3.setStatusUser("1004"); //待完善
                    planInfo3.setPlanId(plan.getId());
                    planInfo3.setType(1);
                    planInfoService.save(planInfo3);
                    auditLeaderAssigneeList.add(Integer.valueOf(String.valueOf(m.get("userId"))));
                }
                map.put("auditLeaderAssigneeList", auditLeaderAssigneeList);
            } else {
                //内审人员
                R<List> auditUserInnerListR = activitiService.getTaskVariable(taskId, "auditUserInnerList");
                List auditUserInnerList = auditUserInnerListR.getData();
                for (int i = 0; i < auditUserInnerList.size(); i++) {
                    planInfoService.updateProjectByPlanUserId(String.valueOf(Integer.valueOf(id)), String.valueOf(auditUserInnerList.get(i)), "1004");
                }
            }
            map.put("auditStatus", Integer.valueOf(auditStatus));
            //流程
            r = activitiService.complete(taskId, map);


        }
        return r.setData(true);
    }
}
