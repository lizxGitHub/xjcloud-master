package gov.pbc.xjcloud.provider.contract.schedule;

import com.alibaba.fastjson.JSONObject;
import gov.pbc.xjcloud.provider.contract.utils.HttpRequestUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
@EnableScheduling
public class UsernameSchedule {

    private Map<String, String> userMap = new ConcurrentHashMap<>();


    @Value("${audit.tenant-url}")
    private String tenantUrl;

    @Scheduled(fixedRate = 1000 * 60 )
    public void initUsername() {
        String responseJSONText = HttpRequestUtil.sendGet(tenantUrl, null);
        JSONObject responseJSON = JSONObject.parseObject(responseJSONText);
        List<Map<String, Object>> dataList = (List<Map<String, Object>>) responseJSON.get("data");
        Map<String, String> collectUsername = dataList.stream().filter(Objects::nonNull).collect(Collectors.toMap(e -> {
            String username = (String) e.get("username");
            int lastIndex = username.lastIndexOf("/");
            return username.substring(lastIndex+1);
        }, e -> (String) e.get("username"),(e1,e2)->e1));
        userMap.clear();
        userMap.putAll(collectUsername);
//        userMap.entrySet().forEach(e->{
//            System.out.println(String.format("%s 用户名:%s登录名",e.getKey(),e.getValue()));
//        });
    }

    public String getUsername(String username) {
        if (userMap.containsKey(username)) {
            return userMap.get(username);
        }
        throw new NullPointerException("该用户不存在");
    }

}
