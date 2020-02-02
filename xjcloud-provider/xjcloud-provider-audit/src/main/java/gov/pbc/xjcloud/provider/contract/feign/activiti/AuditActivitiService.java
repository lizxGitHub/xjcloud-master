package gov.pbc.xjcloud.provider.contract.feign.activiti;

import gov.pbc.xjcloud.common.core.util.R;
import gov.pbc.xjcloud.provider.contract.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
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
    R complete(@PathVariable("taskId") String paramString1, @RequestParam("vars") String paramString2);

    @DeleteMapping({"/task/{processDefKey}/{businessId}"})
    R cancel(@PathVariable("processDefKey") String paramString1, @PathVariable("businessId") String paramString2);

    @GetMapping({"/task/history/{processDefKey}/{businessId}"})
    R history(@PathVariable("processDefKey") String paramString1, @PathVariable("businessId") String paramString2);

    @GetMapping({"/process/var/{processDefKey}/{businessId}/{varName}"})
    R getVariable(@PathVariable("processDefKey") String paramString1, @PathVariable("businessId") String paramString2, @PathVariable("varName") String paramString3);

}
