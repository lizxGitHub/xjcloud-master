package gov.pbc.xjcloud.provider.contract.controller.auditManage;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import gov.pbc.xjcloud.provider.contract.entity.PlanCheckList;
import gov.pbc.xjcloud.provider.contract.enumutils.PlanStatusEnum;
import gov.pbc.xjcloud.provider.contract.feign.activiti.AuditActivitiService;
import gov.pbc.xjcloud.provider.contract.service.auditManage.PlanManagementService;
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
            return new R().setData(actTaskMap);
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
            PlanCheckList plan = planManagementService.getById(planId);
            if (PlanStatusEnum.FILE.getCode() == status && StringUtils.isNotBlank(planId)) {
                plan.setStatus(String.valueOf(PlanStatusEnum.FILE.getCode()));
                planManagementService.updateById(plan);
            } else if (PlanStatusEnum.DELAY_APPLY.getCode() == status && StringUtils.isNotBlank(planId)) {
                plan.setStatus(String.valueOf(PlanStatusEnum.DELAY_APPLY.getCode()));
                String date = params.get("delayDate").toString();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                plan.setDelayDate(sdf.parse(date));
                planManagementService.updateById(plan);
            } else if (PlanStatusEnum.COMPLETE_TOBE_AUDIT.getCode() == status && StringUtils.isNotBlank(planId)) {
                plan.setStatus(String.valueOf(PlanStatusEnum.DELAY_APPLY.getCode()));
                if(null != params.get("delayDate")){
                    String date = params.get("delayDate").toString();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    plan.setDelayDate(sdf.parse(date));
                }

                if(params.get("delayOpt").equals("reject")){
                    plan.setDelayDate(null);
                }
                planManagementService.updateById(plan);

            }
            params.remove("bizKey");
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
