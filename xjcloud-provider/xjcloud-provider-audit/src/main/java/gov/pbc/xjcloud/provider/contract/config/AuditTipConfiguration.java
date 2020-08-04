package gov.pbc.xjcloud.provider.contract.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 提醒配置类
 * @author PaungMiao
 * @date 2020年8月4日09:19:59
 */
@Configuration
@ConfigurationProperties(prefix = "audit.tip")
@Data
public class AuditTipConfiguration {
    /**
     * activiti设置提醒key
     */
    private String key;
    /**
     * 获取token的用户名
     */
    private String username;
    /**
     * 获取token的密码(密文   )
     */
    private String password;
    /**
     * 行长角色名称
     */
    private String bankLeaderRole;


}
