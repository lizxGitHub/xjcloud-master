package gov.pbc.xjcloud.common.data.tenant;

import com.alibaba.ttl.TransmittableThreadLocal;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TenantContextHolder {

	private final ThreadLocal<Integer> THREAD_LOCAL_TENANT = new TransmittableThreadLocal<>();


	/**
	 * TTL 设置租户ID
	 *
	 * @param tenantId
	 */
	void setTenantId(Integer tenantId) {
		THREAD_LOCAL_TENANT.set(tenantId);
	}

	/**
	 * 获取TTL中的租户ID
	 *
	 * @return
	 */
	public Integer getTenantId() {
		Integer tenantId = THREAD_LOCAL_TENANT.get();
		if (null == tenantId)
			tenantId = 1;
		return tenantId;
	}

	public void clear() {
		THREAD_LOCAL_TENANT.remove();
	}
}
