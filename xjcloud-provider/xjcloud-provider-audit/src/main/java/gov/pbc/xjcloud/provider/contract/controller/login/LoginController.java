package gov.pbc.xjcloud.provider.contract.controller.login;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.enums.ApiErrorCode;
import com.google.common.collect.Maps;
import gov.pbc.xjcloud.common.core.util.R;
import gov.pbc.xjcloud.provider.contract.utils.HttpRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@RestController
@RequestMapping("audit-api/login")
public class LoginController {

    @Value("${audit.auth-url}")
    private String authUrl;

    @Value("${audit.user-url}")
    private String userUrl;

    @Value("${audit.Authorization}")
    private String authorization;

    @Value("${audit.token-name:Authorization}")
    private String tokenName;

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
    @RequestMapping("user/info")
    public JSONObject getUserInfo(HttpServletRequest request) {
        Map<String, String> headers = Maps.newHashMap();
        String tokenValue = request.getHeader(tokenName);
        headers.put(tokenName, tokenValue);
        String s = null;
        try {
            s = HttpRequestUtil.sendGet(userUrl, new String(), headers);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject jsStr = JSONObject.parseObject(s);
        return jsStr;
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
