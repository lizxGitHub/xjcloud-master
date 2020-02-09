package gov.pbc.xjcloud.provider.contract.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;

@Configuration
public class UploadFileConfig {

    @Value("${audit.file.uploadFolder}")
    private String uploadFolder;

    @Bean
    MultipartConfigElement multipartConfigElement(){
        MultipartConfigFactory factory = new MultipartConfigFactory();
//        factory.setLocation(uploadFolder);
        //resolveLazily属性启用是为了推迟文件解析，以在在UploadAction中捕获文件大小异常
        // 单次请求最大上传文件大小
        factory.setMaxRequestSize("10MB");
        return factory.createMultipartConfig();
    }
}
