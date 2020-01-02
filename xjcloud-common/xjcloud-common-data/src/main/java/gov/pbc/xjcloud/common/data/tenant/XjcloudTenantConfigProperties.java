package gov.pbc.xjcloud.common.data.tenant;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "xjcloud.tenant")
public class XjcloudTenantConfigProperties {

	/**
	 * 维护租户列名称
	 */
	private String column="tenant_id";

	/**
	 * 多租户的数据表集合
	 */
	private List<String> tables = new ArrayList<>();
}
