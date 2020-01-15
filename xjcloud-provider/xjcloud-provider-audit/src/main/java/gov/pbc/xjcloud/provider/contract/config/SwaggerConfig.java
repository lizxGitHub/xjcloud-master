package gov.pbc.xjcloud.provider.contract.config;

import gov.pbc.xjcloud.common.swagger.annotation.EnableCustomSwagger2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author paungmiao
 * @create 2020年1月15日19:43:13
 */
//@Configuration
//@EnableWebMvc
//@EnableSwagger2
//@ComponentScan(basePackages = { "gov.pbc.xjcloud.provider.contract" })
public class SwaggerConfig extends WebMvcConfigurationSupport {
//    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("审计系统接口文档")//标题
                .description("API描述")//描述
                .termsOfServiceUrl("http://www.google.com.hk")//（不可见）条款地址，公司内部使用的话不需要配
                .contact(new Contact("paungmiao", "", "paungmiao@qq.com"))//作者信息
                .version("2.7.0")//版本号
                .build();
    }
}