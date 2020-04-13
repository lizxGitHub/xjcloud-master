package gov.pbc.xjcloud.provider.contract.controller;

import com.baomidou.mybatisplus.extension.enums.ApiErrorCode;
import gov.pbc.xjcloud.provider.contract.feign.user.UserCenterService;
import gov.pbc.xjcloud.provider.contract.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * 公共请求
 *
 * @author paungmiao@163.com
 * @date 2020年1月20日17:54:09
 */
@RestController
@RequestMapping("audit-api/common")
public class CommonController {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Resource
    private UserCenterService userCenterService;

    @Value("${audit.gate-way.service-id:xjcloud-gateway}")
    private String gateServiceId;

    @GetMapping("gateway")
    public R<String> getGateWay() {

        List<ServiceInstance> serviceInstances = discoveryClient.getInstances(gateServiceId);
        ServiceInstance serviceInstance = serviceInstances.stream().filter(Objects::nonNull).findFirst().get();
        String gateway = serviceInstance.getUri().toString();
        return new R<>(gateway);

    }

    @GetMapping("/depts/{id}")
    public R dept(@PathVariable Integer id){
        try {
            return userCenterService.dept(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new R().setCode(1).setData(new HashMap<>());
    }

}
