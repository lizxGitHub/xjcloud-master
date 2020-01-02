package gov.pbc.xjcloud.common.data.tenant;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class XjcloudFeignTenantConfiguration {
	@Bean
	public RequestInterceptor xjcloudFeignTenantInterceptor() {
		return new XjcloudFeignTenantInterceptor();
	}
}
