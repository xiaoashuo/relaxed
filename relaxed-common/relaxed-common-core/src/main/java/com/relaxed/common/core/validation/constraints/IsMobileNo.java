package com.relaxed.common.core.validation.constraints;

import com.relaxed.common.core.validation.validator.IsMobileNoValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 手机号效验
 *
 * @author Yakir
 */
@Target({ FIELD, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = { IsMobileNoValidator.class })
public @interface IsMobileNo {

	/**
	 * 字段是否必填
	 */
	boolean required() default false;

	/**
	 * 错误提示
	 */
	String message() default "手机号格式不合法";

	/**
	 * 正则模式
	 */
	String pattern() default "";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
