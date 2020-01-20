package gov.pbc.xjcloud.provider.contract.service.activiti;


import com.baomidou.mybatisplus.extension.api.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "xjcloud-provider-activiti")
public interface AuditActivitiService {

    @PutMapping("/process/start/{processDefKey}/{businessId}")
    R start(@PathVariable("processDefKey") String processDefKey, @PathVariable("businessId") Integer businessId, @RequestParam(value = "vars") String vars);

}
