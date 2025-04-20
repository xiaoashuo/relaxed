package com.relaxed.common.core.validation.constraints;

import com.relaxed.common.core.validation.validator.EnumValueValidatorOfInt;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 整数枚举值校验注解，用于验证字段值是否在指定的整数集合中
 *
 * @author Yakir
 * @since 1.0.0
 */
@Target({ FIELD, TYPE })
@Retention(RUNTIME)
@Repeatable(OneOfInts.List.class)
@Documented
@Constraint(validatedBy = { EnumValueValidatorOfInt.class })
public @interface OneOfInts {

	String message() default "value must match one of the values in the list: {value}";

	int[] value();

	boolean allowNull() default false;

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	@Target({ TYPE })
	@Retention(RUNTIME)
	@Documented
	@interface List {

		OneOfInts[] value();

	}

}
