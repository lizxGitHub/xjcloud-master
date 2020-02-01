package gov.pbc.xjcloud.provider.contract.feign.activiti;

import gov.pbc.xjcloud.common.core.util.R;
import gov.pbc.xjcloud.provider.contract.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 远程调用
 */
@FeignClient(value = "xjcloud-provider-activiti",configuration = FeignClientConfig.class )
public interface AuditActivitiService {

    @PutMapping("/process/start/{processDefKey}/{businessId}")
    R start(@PathVariable("processDefKey") String processDefKey, @PathVariable("businessId") Integer businessId, @RequestParam(value = "vars") String vars);

}
