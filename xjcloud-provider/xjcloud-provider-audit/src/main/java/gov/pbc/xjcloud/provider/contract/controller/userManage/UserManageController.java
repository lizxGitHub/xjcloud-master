package gov.pbc.xjcloud.provider.contract.controller.userManage;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import gov.pbc.xjcloud.common.core.util.R;
import gov.pbc.xjcloud.provider.contract.feign.user.UserCenterService;
import gov.pbc.xjcloud.provider.contract.utils.HttpRequestUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("audit-api/user")
public class UserManageController {

    @Value("${audit.users-url}")
    private String usersUrl;

    @Value("${audit.token-name:Authorization}")
    private String tokenName;

    @Resource
    private UserCenterService userCenterService;

    @RequestMapping("page")
    public JSONObject getUserInfo(HttpServletRequest request) {
        Map<String, String> headers = Maps.newHashMap();
        String tokenValue = request.getHeader(tokenName);
        headers.put(tokenName, tokenValue);
        String s = "";
        String url = usersUrl + "/page";
        try {
            s = HttpRequestUtil.sendGet(url, new String(), headers);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONObject jsStr = JSONObject.parseObject(s);
        return jsStr;
    }

    @RequestMapping("/dept/roleName")
    public R getUsers(HttpServletRequest request, @RequestParam(name = "deptId", required = true) Integer deptId, @RequestParam(name = "roleName", required = true) String roleName) {
        Map<String, String> headers = Maps.newHashMap();
        String tokenValue = request.getHeader(tokenName);
        headers.put(tokenName, tokenValue);
//        String s = "";
////        String url = usersUrl + "/dept/" + deptId + "/role/" + roleName;
//        String url = usersUrl + "/dept/10000/role/一般用户";
//        try {
//            s = HttpRequestUtil.sendGet(url, new String(), headers);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        R s = userCenterService.getUsersByRoleNameAndDept(deptId, roleName);

//        JSONObject jsStr = JSONObject.parseObject();
        return s;
    }

}
