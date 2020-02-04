package gov.pbc.xjcloud.provider.contract.feign.activiti;

import com.alibaba.fastjson.JSONObject;
import gov.pbc.xjcloud.common.core.util.R;
import gov.pbc.xjcloud.provider.contract.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 远程调用activity
 */
@FeignClient(value = "xjcloud-provider-activiti",configuration = FeignClientConfig.class )
public interface AuditActivitiService {

    @PutMapping({"/process/start/{processDefKey}/{businessId}"})
    R start(@PathVariable("processDefKey") String paramString1, @PathVariable("businessId") Integer paramInteger, @RequestParam("vars") String paramString2);

    @GetMapping({"/task/todo/{processDefKey}"})
    R todo(@PathVariable("processDefKey") String paramString, @RequestParam Map<String, Object> paramMap);

    @PostMapping({"/task/{taskId}"})
    R complete(@PathVariable("taskId") String paramString1, @RequestBody  Map<String, Object> params);

    @DeleteMapping({"/task/{processDefKey}/{businessId}"})
    R cancel(@PathVariable("processDefKey") String paramString1, @PathVariable("businessId") String paramString2);

    @GetMapping({"/task/history/{processDefKey}/{businessId}"})
    R history(@PathVariable("processDefKey") String paramString1, @PathVariable("businessId") String paramString2);

    @GetMapping({"/task/{taskId}/var/{varName}"})
    R getTaskVariable(@PathVariable("taskId") String taskId, @PathVariable("varName") String varName);

    @GetMapping({"/process/var/{processDefKey}/{businessId}/{varName}"})
    R getVariable(@PathVariable("processDefKey") String paramString1, @PathVariable("businessId") String paramString2, @PathVariable("varName") String paramString3);

    @GetMapping({"/task/view/{processDefKey}/{businessId}"})
    ResponseEntity getTaskView(@PathVariable("processDefKey") String paramString1, @PathVariable("businessId") String paramString2);
}
