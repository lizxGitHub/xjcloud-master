package gov.pbc.xjcloud.provider.contract.controller.login;

import com.baomidou.mybatisplus.extension.enums.ApiErrorCode;
import gov.pbc.xjcloud.common.core.util.R;
import gov.pbc.xjcloud.provider.contract.utils.HttpRequestUtil;
import lombok.val;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.ribbon.apache.HttpClientUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@RestController
@RequestMapping("audit-api/login")
public class LoginController {

    @Value("${audit.auth-url}")
    private String authUrl;

    @Value("${audit.Authorization}")
    private String authorization;

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("token")
    public R<String> getToken(@RequestParam Map<String, String> params) {
        R<String> r = new R<>();
        try {
            StringBuffer sb = new StringBuffer();
            params.entrySet().stream().forEach(e -> {
                sb.append(e.getKey() + "=" + e.getValue() + "&");
            });
            sb.deleteCharAt(sb.length() - 1);
            Map<String, String> headers = new HashMap<>();
            headers.put("Authorization", authorization);
            headers.put("TENANT_ID", "1");
            headers.put("isToken", "false");
            String s = HttpRequestUtil.sendPost(authUrl, sb.toString(), headers);
            r.setData(s);
        } catch (Exception e) {
            r.setMsg(e.getMessage());
            r.setCode(Long.valueOf(ApiErrorCode.FAILED.getCode()).intValue());
            e.printStackTrace();
        }
        return r;
    }

    public String getParams(Map<String, String> map) {
        Objects.nonNull(map);
        Function<Map<String, String>, String> f1 = (t) -> {
            StringBuffer sb = new StringBuffer();
            t.entrySet().stream().forEach(e -> {
                sb.append(e.getKey() + "=" + e.getValue() + "&");
            });
            sb.deleteCharAt(sb.length() - 1);
            return t.toString();
        };
        return f1.apply(map);
    }

}
