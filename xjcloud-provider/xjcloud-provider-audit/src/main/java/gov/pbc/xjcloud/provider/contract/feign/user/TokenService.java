package gov.pbc.xjcloud.provider.contract.feign.user;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import gov.pbc.xjcloud.common.core.util.R;
import gov.pbc.xjcloud.provider.contract.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(value = "xjcloud-auth",configuration = FeignClientConfig.class )
public interface TokenService {

    @PostMapping({"/token/page"})
    R<Page> getTokenPage(@RequestBody Map<String, Object> paramMap, @RequestHeader("from") String paramString);

    @DeleteMapping({"/token/{token}"})
    R<Boolean> removeTokenById(@PathVariable("token") String paramString1, @RequestHeader("from") String paramString2);

}
