package com.relaxed.common.secret.annotation;

import java.lang.annotation.*;

/**
 * @author Yakir
 * @Topic ResponseEncrypt
 * @Description
 * @date 2021/11/15 10:08
 * @Version 1.0
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RequestDecrypt {

	/**
	 * 消息转换器之前 处理消息解密
	 * @author yakir
	 * @date 2021/11/15 16:07
	 * @return boolean
	 */
	boolean pre() default false;

	/**
	 * 消息转换器之后 处理参数的解密
	 * @author yakir
	 * @date 2021/11/15 16:08
	 * @return boolean
	 */
	boolean post() default true;

}
