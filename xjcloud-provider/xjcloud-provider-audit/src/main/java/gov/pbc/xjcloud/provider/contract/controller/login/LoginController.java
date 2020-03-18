package gov.pbc.xjcloud.provider.contract.controller.login;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.enums.ApiErrorCode;
import com.google.common.collect.Maps;
import gov.pbc.xjcloud.provider.contract.schedule.UsernameSchedule;
import gov.pbc.xjcloud.provider.contract.service.authority.AuthorityService;
import gov.pbc.xjcloud.provider.contract.utils.HttpRequestUtil;
import gov.pbc.xjcloud.provider.contract.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
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

    @Resource
    private AuthorityService authorityService;

    @Autowired
    private UsernameSchedule usernameSchedule;

    @GetMapping("token")
    public R<String> getToken(@RequestParam Map<String, String> params) {
        R<String> r = new R<>();
        try {
            StringBuffer sb = new StringBuffer();
            params.entrySet().stream().forEach(e -> {
                try {
                    if (e.getKey().equals("password")) {
                        e.setValue(URLEncoder.encode(e.getValue(), "UTF-8"));
                    }
                    if (e.getKey().equals("username")) { //登陆过滤
                        e.setValue(URLEncoder.encode(usernameSchedule.getUsername(e.getValue()), "UTF-8"));
                    }
                } catch (UnsupportedEncodingException ex) {
                    ex.printStackTrace();
                }
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

    @GetMapping("/getPermission")
    public R getDeptInfoById(@RequestParam(name = "roleIds", required = true) String roleIds) {
        R r = new R<>();
        String[] roleIdArray = roleIds.split(",");
        int[] array = Arrays.asList(roleIdArray).stream().mapToInt(Integer::parseInt).toArray();
        List<String> permissionList = authorityService.getMenuByRoleId(array);
        return r.setData(permissionList);
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

    public static void main(String[] args) throws UnsupportedEncodingException {
        System.out.println(URLEncoder.encode("123456", "UTF-8"));
    }

}
