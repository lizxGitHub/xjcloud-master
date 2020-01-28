package gov.pbc.xjcloud.provider.contract.controller.userManage;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import gov.pbc.xjcloud.provider.contract.utils.HttpRequestUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("audit-api/user")
public class UserManageController {

    @Value("${audit.user-page-url}")
    private String userPageUrl;

    @Value("${audit.token-name:Authorization}")
    private String tokenName;

    @RequestMapping("page")
    public JSONObject getUserInfo(HttpServletRequest request) {
        Map<String, String> headers = Maps.newHashMap();
        String tokenValue = request.getHeader(tokenName);
        headers.put(tokenName, tokenValue);
        String s = "";
        try {
            s = HttpRequestUtil.sendGet(userPageUrl, new String(), headers);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONObject jsStr = JSONObject.parseObject(s);
        return jsStr;
    }

}
