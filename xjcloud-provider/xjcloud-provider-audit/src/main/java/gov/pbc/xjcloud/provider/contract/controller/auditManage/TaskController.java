package gov.pbc.xjcloud.provider.contract.controller.auditManage;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import gov.pbc.xjcloud.common.core.util.R;
import gov.pbc.xjcloud.provider.activiti.api.vo.ActVO;
import gov.pbc.xjcloud.provider.contract.feign.activiti.AuditActivitiService;
import gov.pbc.xjcloud.provider.contract.vo.ac.ActAuditVO;
import gov.pbc.xjcloud.provider.usercenter.api.feign.RemoteUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

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
    public R<List<ActAuditVO>> LustToDo(Map<String, Object> params) {
        try {
            if (!params.containsKey("size")) {
                params.put("size", 10);
            }
            if (!params.containsKey("current")) {
                params.put("current", 1);
            }
            R<ModelMap> actTaskMap = activitiService.todo(auditFlowDefKey, params);
            List<ActAuditVO> resultList = new ArrayList<>();
            LinkedHashMap<String,Object> actTaskMapData = actTaskMap.getData();
            ((List<Map>)actTaskMapData.get("records")).stream().filter(Objects::nonNull).forEach(actVO -> {
                String actJsonStr = JSONObject.toJSONString(actVO);
                ActAuditVO actAuditVO = JSONUtil.toBean(actJsonStr, ActAuditVO.class);
                R<Integer> auditStatus = activitiService.getTaskVariable(actAuditVO.getTaskId(), "auditStatus");
                actAuditVO.setAuditStatus(auditStatus.getData());
                resultList.add(actAuditVO);
            });
            return new R().setData(resultList);
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
    @PostMapping("complete")
    public R complete(String taskId, Map<String, Object> params) {
        R complete = activitiService.complete(taskId, String.valueOf(params));
        return complete;
    }

}
