package gov.pbc.xjcloud.common.data.tenant;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import gov.pbc.xjcloud.common.core.constant.CommonConstants;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class XjcloudFeignTenantInterceptor implements RequestInterceptor {
	@Override
	public void apply(RequestTemplate requestTemplate) {
		if (TenantContextHolder.getTenantId() == null) {
			log.error("TTL 中的 租户ID为空，feign拦截器 >> 增强失败");
			return;
		}
		requestTemplate.header(CommonConstants.TENANT_ID, TenantContextHolder.getTenantId().toString());
	}
}
