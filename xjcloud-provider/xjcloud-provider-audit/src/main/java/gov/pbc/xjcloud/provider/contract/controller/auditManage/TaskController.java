package gov.pbc.xjcloud.provider.contract.controller.auditManage;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import gov.pbc.xjcloud.provider.contract.entity.PlanCheckList;
import gov.pbc.xjcloud.provider.contract.entity.auditManage.AuditPlanInfo;
import gov.pbc.xjcloud.provider.contract.enumutils.PlanStatusEnum;
import gov.pbc.xjcloud.provider.contract.feign.activiti.AuditActivitiService;
import gov.pbc.xjcloud.provider.contract.service.auditManage.PlanManagementService;
import gov.pbc.xjcloud.provider.contract.service.impl.auditManage.AuditPlanInfoServiceImpl;
import gov.pbc.xjcloud.provider.contract.utils.PageUtil;
import gov.pbc.xjcloud.provider.contract.utils.R;
import gov.pbc.xjcloud.provider.contract.vo.ac.ActAuditVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
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
    private PlanManagementService planManagementService;

    @Resource
    private AuditPlanInfoServiceImpl auditPlanInfoServiceImpl;

    /**
     * 流程页面
     * @param query
     * @return
     */
    @GetMapping(value = {"/flow/page", ""})
    public com.baomidou.mybatisplus.extension.api.R<Page<PlanCheckList>> flowPage(PlanCheckList query, Page<PlanCheckList> page) {
        PageUtil.initPage(page);
        try {
            //获取代办
            Map<String, Object> params = new HashMap<>();
            params.put("size", 1000);
            params.put("current", 1);
            gov.pbc.xjcloud.provider.contract.utils.R<LinkedHashMap<String, Object>> actTaskMap = activitiService.todo(auditFlowDefKey, params);
            LinkedHashMap<String, Object> actTaskMapData = actTaskMap.getData();
            List<Map<String, String>> resultList = (List<Map<String, String>>) actTaskMapData.get("records");
            page = planManagementService.selectPlanCheckList(page, query);
            page.getRecords().stream().filter(e -> e.getImplementingAgencyId() != null && e.getAuditObjectId() != null).forEach(e -> {
                if (resultList.size() > 0) {
                    for(Map<String, String> taskInfo : resultList) {
                        if (Integer.valueOf(taskInfo.get("bizKey")) == e.getId()) {
                            String taskId = taskInfo.get("taskId");
                            e.setTaskId(taskId);
                            R<Integer> auditStatus = activitiService.getTaskVariable(taskId, "auditStatus");
                            e.setAuditStatus(String.valueOf(auditStatus.getData()));
                            break;
                        }
                    }
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
        R complete = null;
        try {
            //修改状态
            String planId = (String) params.get("bizKey");
            int status = (int) params.get("auditStatus");
            String statusStr = params.get("auditStatus").toString();
            String mark = "";
            if (params.get("mark") != null) {
                mark = params.get("mark").toString();
            }
            PlanCheckList plan = planManagementService.getById(planId);
            if (PlanStatusEnum.PLAN_IMP_REJECT.getCode() == status && StringUtils.isNotBlank(planId)) {
                //实施部门一般员工
                auditPlanInfoServiceImpl.updateByPlanUserId(planId, String.valueOf(plan.getImpUserId()), "1004");
                //实施部门管理员
                auditPlanInfoServiceImpl.updateByPlanUserId(planId, String.valueOf(plan.getImpAdminId()), "1002");
                if (StringUtils.isNotBlank(mark) && "markOne".equals(mark)) {
                    //审计对象管理员
                    auditPlanInfoServiceImpl.updateByPlanUserId(planId, String.valueOf(plan.getAuditAdminId()), "1002");
                }
            } else if (PlanStatusEnum.PLAN_TOBE_AUDITED.getCode() == status && StringUtils.isNotBlank(planId)) {
                //实施部门一般员工
                auditPlanInfoServiceImpl.updateByPlanUserId(planId, String.valueOf(plan.getImpUserId()), "1003");
                //实施部门管理员
                auditPlanInfoServiceImpl.updateByPlanUserId(planId, String.valueOf(plan.getImpAdminId()), "1001");
            } else if (PlanStatusEnum.PLAN_IMP_PASS.getCode() == status && StringUtils.isNotBlank(planId)) {
                //实施部门一般员工
                auditPlanInfoServiceImpl.updateByPlanUserId(planId, String.valueOf(plan.getImpUserId()), "1001");
                //实施部门管理员
                auditPlanInfoServiceImpl.updateByPlanUserId(planId, String.valueOf(plan.getImpAdminId()), "1003");
            }
            params.remove("bizKey");
            plan.setAuditStatus(statusStr);
            Map<String, Object> planMap = transBean2Map(plan);
            params.putAll(planMap);
            complete = activitiService.complete(taskId, params);
        } catch (Exception e) {
            e.printStackTrace();
        }

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

}
