package gov.pbc.xjcloud.provider.contract.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import gov.pbc.xjcloud.common.core.constant.SecurityConstants;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author paungmiao@163.com
 * @date 2020年1月20日16:27:00
 */
@Configuration
public class FeignClientConfig implements RequestInterceptor {

    @Value("${audit.token-name:Authorization}")
    private String tokenName;

    public FeignClientConfig() {

    }

    @Override
    public void apply(RequestTemplate template) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return;
        }
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        String headerValue = request.getHeader(tokenName);
        if (StringUtils.isBlank(headerValue)) {
            throw new NullArgumentException("无法获取认证信息");
        }
        template.header(tokenName, headerValue);
        template.header(SecurityConstants.FROM,SecurityConstants.FROM_IN);
//        Enumeration<String> headerNames = request.getHeaderNames();
//        if (headerNames != null) {
//            while (headerNames.hasMoreElements()) {
//                String name = headerNames.nextElement();
//                Enumeration<String> values = request.getHeaders(name);
//                while (values.hasMoreElements()) {
//                    String value = values.nextElement();
//                    System.out.println(name + "---" + value);
//                    template.header(name, value);
//                }
//            }
//        }
    }
}