package gov.pbc.xjcloud.common.minio;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import gov.pbc.xjcloud.common.minio.service.MinioTemplate;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@EnableConfigurationProperties({MinioProperties.class})
public class MinioAutoConfiguration {
	private final MinioProperties properties;

	@Bean
	@ConditionalOnMissingBean(MinioTemplate.class)
	@ConditionalOnProperty(name = "minio.url")
	MinioTemplate template() {
		return new MinioTemplate(
				properties.getUrl(),
				properties.getAccessKey(),
				properties.getSecretKey()
		);
	}
}
