package gov.pbc.xjcloud.provider.contract.controller.login;

import cn.hutool.core.util.StrUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * @author PaungMiao
 */
@RequestMapping("audit-api")
@Controller
public class CasLoginController {

    @Value("${audit.token-name:Authorization}")
    private String Authorization;

    @GetMapping("login/cas")
    public String cas() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        String token = request.getHeader(Authorization);
        Cookie errorCookie = new Cookie("audit_error", "token is invalid");
        Cookie tokenCookie = new Cookie(Authorization, null);
        iniTCookie(errorCookie);
        iniTCookie(tokenCookie);
        if (StringUtils.isNotBlank(token)) {
            tokenCookie.setValue(token);
        }
        Cookie[] cookies = request.getCookies();
        System.out.println("========= COOKIE =======");
        if (null != cookies && StringUtils.isBlank(token)) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(Authorization)) {
                    tokenCookie.setValue(cookie.getValue());
                }
            }
        }
        if(StrUtil.isNotBlank(tokenCookie.getValue())){
            response.addCookie(tokenCookie);
        }
        if (StrUtil.isBlank(tokenCookie.getValue())) {
            response.addCookie(errorCookie);
        }
        String page = request.getParameter("page");
        if (StringUtils.isBlank(page)) {
            page = "/system/login";
        }
        return "redirect:http://xjcloud-audit.wlq.pbc.gov:9955/" + page + ".html";
    }

    void iniTCookie(Cookie cookie) {
        Optional.of(cookie).ifPresent(e -> {
            cookie.setPath("/");
            cookie.setDomain(".wlq.pbc.gov");
        });
    }

}
