package gov.pbc.xjcloud.provider.contract.controller.auditManage;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import gov.pbc.xjcloud.common.core.util.R;
import gov.pbc.xjcloud.provider.contract.feign.activiti.AuditActivitiService;
import gov.pbc.xjcloud.provider.contract.vo.ac.ActAuditVO;
import gov.pbc.xjcloud.provider.usercenter.api.feign.RemoteUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    RemoteUserService remoteUserService;

    @Value("${audit.flow-key:auditApply}")
    private String auditFlowDefKey;


    /**
     * 分页获取流程待办任务
     *
     * @param params 分页参数
     * @return
     */
    @GetMapping("todo")
    public R<Map<String,Object>> LustToDo(Map<String, Object> params) {
        try {
            if (!params.containsKey("size")) {
                params.put("size", 10);
            }
            if (!params.containsKey("current")) {
                params.put("current", 1);
            }
            R<LinkedHashMap<String,Object>> actTaskMap = activitiService.todo(auditFlowDefKey, params);
            LinkedHashMap<String,Object> actTaskMapData = actTaskMap.getData();
            List<ActAuditVO> resultList = ((List<Map>)actTaskMapData.get("records")).stream().filter(Objects::nonNull).map(actVO -> {
                String actJsonStr = JSONObject.toJSONString(actVO);
                ActAuditVO actAuditVO = JSONUtil.toBean(actJsonStr, ActAuditVO.class);
                R<Integer> auditStatus = activitiService.getTaskVariable(actAuditVO.getTaskId(), "auditStatus");
                actAuditVO.setAuditStatus(auditStatus.getData());
                return actAuditVO;
            }).collect(Collectors.toList());
            actTaskMap.getData().put("records",resultList);
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
            complete = activitiService.complete(taskId, params);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return complete;
    }

    /**
     * 获取流程图
     * @param processDefKey
     * @param businessId
     * @return
     */
    @GetMapping("getTaskView")
    public ResponseEntity taskView(@RequestParam(name = "processDefKey", required = true) String processDefKey, @RequestParam(name = "businessId", required = true) String businessId) {
        return activitiService.getTaskView(processDefKey, businessId);
    }

}
