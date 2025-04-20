package com.relaxed.common.core.validation.validator;

import com.relaxed.common.core.validation.constraints.OneOfInts;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 整数枚举值校验器，用于验证字段值是否在指定的整数集合中
 *
 * @author housl
 * @since 1.0.0
 */
public class EnumValueValidatorOfInt implements ConstraintValidator<OneOfInts, Integer> {

	private int[] ints;

	private boolean allowNull;

	@Override
	public void initialize(OneOfInts constraintAnnotation) {
		ints = constraintAnnotation.value();
		allowNull = constraintAnnotation.allowNull();
	}

	@Override
	public boolean isValid(Integer value, ConstraintValidatorContext context) {
		if (value == null) {
			return allowNull;
		}
		for (int anInt : ints) {
			if (anInt == value) {
				return true;
			}
		}
		return false;
	}

}
