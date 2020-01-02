package gov.pbc.xjcloud.common.security.feign;

import com.netflix.hystrix.HystrixCommand;
import feign.Feign;
import feign.RequestInterceptor;
import feign.hystrix.HystrixFeign;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.FeignContext;
import org.springframework.cloud.security.oauth2.client.AccessTokenContextRelay;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;

@Configuration
@ConditionalOnClass(Feign.class)
public class XjcloudFeignConfiguration {

	@Bean
	@ConditionalOnProperty("security.oauth2.client.client-id")
	public RequestInterceptor oauth2FeignRequestInterceptor(OAuth2ClientContext oAuth2ClientContext,
															OAuth2ProtectedResourceDetails resource,
															AccessTokenContextRelay accessTokenContextRelay) {
		return new XjcloudFeignClientInterceptor(oAuth2ClientContext, resource, accessTokenContextRelay);
	}

	@Configuration
	@ConditionalOnClass({HystrixCommand.class, HystrixFeign.class})
	protected static class HystrixFeignConfiguration {
		@Bean
		@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
		@ConditionalOnProperty("feign.hystrix.enabled")
		public Feign.Builder feignHystrixBuilder(FeignContext feignContext) {
			return XjcloudHystrixFeign.builder(feignContext)
					.decode404()
					.errorDecoder(new XjcloudFeignErrorDecoder());
		}
	}

}
