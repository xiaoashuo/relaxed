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
 * @author Yakir
 * @Topic OneOfInts
 * @Description
 * @date 2022/8/5 13:37
 * @Version 1.0
 */
@Target({ FIELD, TYPE })
@Retention(RUNTIME)
@Repeatable(OneOfInts.List.class)
@Documented
@Constraint(validatedBy = { EnumValueValidatorOfInt.class })
public @interface OneOfInts {

	String message() default "value must match one of the values in the list: {value}";

	int[] value();

	/**
	 * 允许值为 null, 默认不允许
	 */
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
