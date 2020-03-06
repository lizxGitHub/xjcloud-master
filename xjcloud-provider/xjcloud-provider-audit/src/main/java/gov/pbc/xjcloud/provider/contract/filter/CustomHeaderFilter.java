package gov.pbc.xjcloud.provider.contract.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;

/**
 * 自定义请求头拦截器
 * @author paungmiao@163.com
 * @date 21点01分
 */
@WebFilter(filterName = "customHeaderFilter",urlPatterns = "/*")
@Slf4j
@Component
public class CustomHeaderFilter implements Filter {

    @Value("${audit.custom-header-prefix:auditHeader}")
    private String customHeaderPrefix;


    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HeaderMapRequestWrapper requestWrapper = new HeaderMapRequestWrapper(req);
        //获得请求参数中的包含默认请求头前缀
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            System.out.println(name);
            String value = request.getParameter(name);
            if (StringUtils.isNotBlank(value) && name.startsWith(customHeaderPrefix)) {
                requestWrapper.addHeader(name.replace(customHeaderPrefix,""), value);
            }
        }
        chain.doFilter(requestWrapper, response); // Goes to default servlet.

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("custom filter init.........................");
    }
}
