package gov.pbc.xjcloud.provider.contract.feign.dept;

import gov.pbc.xjcloud.provider.contract.utils.R;
import gov.pbc.xjcloud.provider.usercenter.api.entity.SysDept;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("xjcloud-provider-usercenter")
public interface RemoteDeptService {
    @GetMapping({"/depts/{id}"})
    R dept(@PathVariable("id") Integer var1);

    @GetMapping({"/depts/parent/{parentId}/recursive/{recursive}"})
    R children(@PathVariable("parentId") Integer var1, @PathVariable("recursive") boolean var2);

    @GetMapping("/depts/tree/0")
    R tree();
}