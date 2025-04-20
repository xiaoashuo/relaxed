package com.relaxed.common.core.validation.validator;

import com.relaxed.common.core.validation.constraints.OneOfStrings;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 字符串枚举值校验器，用于验证字段值是否在指定的字符串集合中
 *
 * @author housl
 * @since 1.0.0
 */
public class EnumValueValidatorOfString implements ConstraintValidator<OneOfStrings, String> {

	private String[] stringList;

	private boolean allowNull;

	@Override
	public void initialize(OneOfStrings constraintAnnotation) {
		stringList = constraintAnnotation.value();
		allowNull = constraintAnnotation.allowNull();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null) {
			return allowNull;
		}
		for (String strValue : stringList) {
			if (strValue.equals(value)) {
				return true;
			}
		}
		return false;
	}

}
