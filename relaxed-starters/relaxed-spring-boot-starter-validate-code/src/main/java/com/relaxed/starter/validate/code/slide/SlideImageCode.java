package com.relaxed.starter.validate.code.slide;

import com.relaxed.extend.validate.code.domain.ValidateCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Yakir
 * @Topic SlideImageCode
 * @Description
 * @date 2022/6/12 16:57
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SlideImageCode extends ValidateCode {

	private double heightYPercentage;

	private transient String srcImg;

	private transient String markImg;

	public SlideImageCode(double heightYPercentage, String srcImg, String markImg, String code, long expireInSeconds) {
		super(code, expireInSeconds);
		this.heightYPercentage = heightYPercentage;
		this.srcImg = srcImg;
		this.markImg = markImg;
	}

}
