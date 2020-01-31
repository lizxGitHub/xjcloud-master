package gov.pbc.xjcloud.provider.contract;

import gov.pbc.xjcloud.common.swagger.annotation.EnableCustomSwagger2;
import gov.pbc.xjcloud.provider.contract.config.FeignClientConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableEurekaClient
@EnableFeignClients(basePackages = "gov.pbc.xjcloud.provider",defaultConfiguration = FeignClientConfig.class)
//@EnableDiscoveryClient
@SpringBootApplication
@EnableEurekaServer
@EnableCustomSwagger2
@MapperScan("gov.pbc.xjcloud.provider.contract.mapper")
public class XjcloudProviderAuditApplication {

    public static void main(String[] args) {
        SpringApplication.run(XjcloudProviderAuditApplication.class, args);
    }

}
