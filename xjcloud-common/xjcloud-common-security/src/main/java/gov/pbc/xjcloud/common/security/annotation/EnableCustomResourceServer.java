package gov.pbc.xjcloud.common.security.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import gov.pbc.xjcloud.common.security.component.XjcloudResourceServerAutoConfiguration;
import gov.pbc.xjcloud.common.security.component.XjcloudSecurityBeanDefinitionRegistrar;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Import({XjcloudResourceServerAutoConfiguration.class, XjcloudSecurityBeanDefinitionRegistrar.class})
public @interface EnableCustomResourceServer {

	/**
	 * false：上下文获取用户名
	 * true： 上下文获取全部用户信息
	 *
	 * @return
	 */
	boolean details() default false;
}
