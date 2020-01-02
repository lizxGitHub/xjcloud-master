package gov.pbc.xjcloud.common.data.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *控制数据级权限，和UpdateDataScope注解搭配使用
 * 此注解在mapper层使用
 *
 */
@Target({ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataScope {
	
	boolean actived() default true;
	
	String scopeName() default "dept_id";
	
	String permission() default "";

}
