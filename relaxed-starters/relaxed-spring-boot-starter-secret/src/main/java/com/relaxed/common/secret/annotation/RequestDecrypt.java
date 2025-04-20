package com.relaxed.common.secret.annotation;

import java.lang.annotation.*;

/**
 * 请求解密注解 用于标记需要解密的请求处理方法或类 支持在消息转换器前后进行解密处理
 *
 * @author Yakir
 * @since 1.0
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RequestDecrypt {

	/**
	 * 是否在消息转换器之前处理消息解密 如果为true，将在消息转换器处理之前对原始请求内容进行解密
	 * @return 如果需要在消息转换器之前解密返回true，否则返回false
	 */
	boolean pre() default false;

	/**
	 * 是否在消息转换器之后处理参数解密 如果为true，将在消息转换器处理之后对已转换的请求体进行解密
	 * @return 如果需要在消息转换器之后解密返回true，否则返回false
	 */
	boolean post() default true;

}
