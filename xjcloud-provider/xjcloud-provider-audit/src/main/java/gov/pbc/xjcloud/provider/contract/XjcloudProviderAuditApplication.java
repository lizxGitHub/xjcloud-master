package gov.pbc.xjcloud.provider.contract;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableEurekaClient
@EnableFeignClients
@SpringBootApplication
@MapperScan("gov.pbc.xjcloud.provider.contract.mapper")
public class XjcloudProviderAuditApplication {

    public static void main(String[] args) {
        SpringApplication.run(XjcloudProviderAuditApplication.class, args);
    }

}
