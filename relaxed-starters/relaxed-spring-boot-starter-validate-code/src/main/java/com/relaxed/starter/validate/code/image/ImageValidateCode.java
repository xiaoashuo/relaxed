package com.relaxed.starter.validate.code.image;

import com.relaxed.extend.validate.code.domain.ValidateCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.awt.image.BufferedImage;

/**
 * @author Yakir
 * @Topic ImageValidateCode
 * @Description
 * @date 2022/6/12 16:42
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ImageValidateCode extends ValidateCode {

	private transient BufferedImage image;

	public ImageValidateCode(BufferedImage image, String code, long expireInSeconds) {
		super(code, expireInSeconds);
		this.image = image;
	}

}
