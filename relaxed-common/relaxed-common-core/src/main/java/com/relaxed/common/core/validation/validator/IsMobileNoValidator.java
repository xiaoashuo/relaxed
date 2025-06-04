package com.relaxed.common.core.validation.validator;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.relaxed.common.core.validation.constraints.IsMobileNo;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 手机号效验具体实现
 *
 * @author Yakir
 */
public class IsMobileNoValidator implements ConstraintValidator<IsMobileNo, String> {

	private static final String DEFAULT_PATTERN = "(?:0|86|\\+86)?1\\d{10}";

	/**
	 * 字段是否必填
	 */
	private boolean required;

	/**
	 * 正则模式
	 */
	private String pattern;

	@Override
	public void initialize(IsMobileNo constraintAnnotation) {
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
