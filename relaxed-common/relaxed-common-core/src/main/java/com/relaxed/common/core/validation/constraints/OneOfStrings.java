package com.relaxed.common.core.validation.constraints;

import com.relaxed.common.core.validation.validator.EnumValueValidatorOfString;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author housl
 * @version 1.0
 */
@Target({ FIELD, TYPE })
@Retention(RUNTIME)
@Repeatable(OneOfStrings.List.class)
@Documented
@Constraint(validatedBy = { EnumValueValidatorOfString.class })
public @interface OneOfStrings {

	String message() default "value must match one of the values in the list: {value}";

	String[] value();

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

		OneOfStrings[] value();

	}

}
