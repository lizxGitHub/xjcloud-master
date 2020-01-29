package gov.pbc.xjcloud.provider.contract.feign.user;

import gov.pbc.xjcloud.common.core.util.R;
import gov.pbc.xjcloud.provider.contract.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 远程调用
 */
@FeignClient(value = "xjcloud-provider-usercenter",configuration =FeignClientConfig.class )
public interface UserCenterService {


    @GetMapping({"/depts/{deptId}/role/{roleName}"})
    R getUsersByRoleNameAndDept(@PathVariable("deptId") Integer paramInteger, @PathVariable("roleName") String paramString);

}
