package gov.pbc.xjcloud.provider.contract.aop;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import gov.pbc.xjcloud.provider.contract.config.AuditTipConfiguration;
import gov.pbc.xjcloud.provider.contract.config.AuthorizationContextHolder;
import gov.pbc.xjcloud.provider.contract.schedule.UsernameSchedule;
import gov.pbc.xjcloud.provider.contract.utils.HttpRequestUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author PaungMiao
 * @date 2020年8月4日09:39:45
 */
@Slf4j
@Aspect
@Component
public class AopTip {

    @Value("${audit.auth-url}")
    private String authUrl;

    @Value("${audit.user-url}")
    private String userUrl;

    @Value("${audit.Authorization}")
    private String authorization;

    @Value("${audit.token-name:Authorization}")
    private String tokenName;

    @Resource
    private AuditTipConfiguration tipConfiguration;

    @Resource
    private UsernameSchedule usernameSchedule;


//    @Pointcut("execution(* gov.pbc.xjcloud.provider.contract.feign.activiti.AuditActivitiService.start(..))")
//    public void pointCut() {
//        log.info(String.format("切面定义：%s", this.getClass().getName()));
//    }
//
//    @SneakyThrows
//    @Pointcut("execution(* gov.pbc.xjcloud.provider.contract.schedule.PlanOutTimeSchedule.getBankLeader(..))")
//    public void getBankLeader() {
//        log.info(String.format("切面定义：%s", this.getClass().getName()));
//        iniToken();
//    }
//
//    @Before("getBankLeader()")
    public void iniToken() {
        StringBuilder sb = new StringBuilder();
        Map<String, String> params = Maps.newHashMap();
        params.put("scope","server");
        params.put("grant_type","password");
        params.put("username",tipConfiguration.getUsername());
        params.put("password",tipConfiguration.getPassword());
        params.entrySet().stream().forEach(e -> {
            try {
                e.setValue(URLEncoder.encode(e.getValue(), "UTF-8"));
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
        headers.put("grant_type", "password");
        headers.put("scope", "server");
        String s = HttpRequestUtil.sendPost(authUrl, sb.toString(), headers);
        /**
         * s:{"access_token":"6173ab6d-fca1-4658-965f-270a196df48f",
         * "token_type":"bearer",
         * "refresh_token":"2d68fcba-2d36-46dd-80b4-caceb6f5e8b3",
         * "expires_in":4360,
         * "scope":"server"}
         */
        JSONObject res = JSONObject.parseObject(s);
        StringBuffer token = new StringBuffer("Bearer");
        String access_token = res.getString("access_token");
        AuthorizationContextHolder.setAuthorization(token.append(" ").append(access_token).toString());
    }


//    @After("pointCut()")
    public void after() {
        AuthorizationContextHolder.clear();
    }

//    @After("getBankLeader()")
    public void aftergetBankLeader() {
        AuthorizationContextHolder.clear();
    }
}
