package gov.pbc.xjcloud.provider.contract.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class SpringBootWebMvcConfigurer implements WebMvcConfigurer {


    @Value("${audit.file.staticAccessPath}")
    private String staticAccessPath;
    @Value("${audit.file.uploadFolder}")
    private String uploadFolder;

    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(staticAccessPath).addResourceLocations("file:/" + uploadFolder);
    }
}