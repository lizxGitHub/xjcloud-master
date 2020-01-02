package gov.pbc.xjcloud.provider.contract.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("provider")
public interface ProviderService {
    @GetMapping("/hello")
    String hello();
}
