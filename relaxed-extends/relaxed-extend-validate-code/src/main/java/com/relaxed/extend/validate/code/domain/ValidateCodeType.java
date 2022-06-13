package com.relaxed.extend.validate.code.domain;

/**
 * @author Yakir
 * @Topic ValidateCodeType
 * @Description
 * @date 2022/6/12 16:02
 * @Version 1.0
 */
public enum ValidateCodeType {

	/** 短信验证码 */
	SMS {
		@Override
		public String getParamNameOnValidate() {
			return "smsCode";
		}
	},

	/** 图片验证码 */
	IMAGE {
		@Override
		public String getParamNameOnValidate() {
			return "imageCode";
		}
	},
	/** 滑动图片验证码 */
	SLIDE {
		@Override
		public String getParamNameOnValidate() {
			return "slideCode";
		}
	};

	public abstract String getParamNameOnValidate();

}
