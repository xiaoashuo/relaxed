package com.relaxed.common.core.validation.validator;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.relaxed.common.core.validation.constraints.IsIdCard;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 证件号效验具体实现
 *
 * @author Yakir
 */
public class IsIdCardValidator implements ConstraintValidator<IsIdCard, String> {

	private static final String DEFAULT_PATTERN = "(^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)";

	/**
	 * 字段是否必填
	 */
	private boolean required;

	/**
	 * 正则模式
	 */
	private String pattern;

	@Override
	public void initialize(IsIdCard constraintAnnotation) {
		this.required = constraintAnnotation.required();
		this.pattern = constraintAnnotation.pattern();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
		if (value == null) {
			return !required;
		}
		String matchPattern = StrUtil.isBlank(pattern) ? DEFAULT_PATTERN : pattern;
		return ReUtil.isMatch(matchPattern, value);
	}

}
