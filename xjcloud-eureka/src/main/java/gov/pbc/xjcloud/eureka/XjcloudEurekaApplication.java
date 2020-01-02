package gov.pbc.xjcloud.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class XjcloudEurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(XjcloudEurekaApplication.class, args);
    }

}
