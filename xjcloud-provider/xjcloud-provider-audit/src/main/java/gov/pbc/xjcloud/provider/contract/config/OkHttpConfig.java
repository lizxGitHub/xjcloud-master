package gov.pbc.xjcloud.provider.contract.config;

import feign.Feign;
import feign.codec.Encoder;
import feign.form.FormEncoder;
import okhttp3.ConnectionPool;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@ConditionalOnClass(Feign.class)
@AutoConfigureBefore(FeignAutoConfiguration.class)
public class OkHttpConfig {
//    @Bean
//    public Encoder encoder() {
//        return new FormEncoder();
//    }

    /**
     * feign远程调用请求方式配置，在配置文件feign'client'okHttp开启
     * @return
     */
    @Bean
    public okhttp3.OkHttpClient okHttpClient() {
        return new okhttp3.OkHttpClient.Builder()
                //设置连接超时
                .connectTimeout(100, TimeUnit.SECONDS)
                //设置读超时
                .readTimeout(100, TimeUnit.SECONDS)
                //设置写超时
                .writeTimeout(100, TimeUnit.SECONDS)
                //是否自动重连
                .retryOnConnectionFailure(true)
                .connectionPool(new ConnectionPool(10, 5L, TimeUnit.MINUTES))
                .build();
    }
}