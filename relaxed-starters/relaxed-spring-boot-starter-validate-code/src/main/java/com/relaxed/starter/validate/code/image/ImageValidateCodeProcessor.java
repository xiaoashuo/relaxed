package com.relaxed.starter.validate.code.image;

import cn.hutool.core.util.StrUtil;

import com.relaxed.extend.validate.code.ValidateCodeException;
import com.relaxed.extend.validate.code.domain.ValidateCodeType;
import com.relaxed.extend.validate.code.processor.AbstractValidateCodeProcessor;
import com.relaxed.extend.validate.code.repository.ValidateCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Yakir
 * @Topic ImageValidateCodeProcessor
 * @Description
 * @date 2022/6/12 17:04
 * @Version 1.0
 */
public class ImageValidateCodeProcessor extends AbstractValidateCodeProcessor<ImageValidateCode> {

	/** 生成的图片的格式 */
	private static final String JPEG_IMAGE_TYPE = "JPEG";

	public ImageValidateCodeProcessor(@Autowired ImageProperties imageProperties,
			@Autowired ValidateCodeRepository repository) {
		super(new ImageValidateCodeGenerator(imageProperties), repository, imageProperties);
	}

	@Override
	protected ValidateCodeType getValidateCodeType() {
		return ValidateCodeType.IMAGE;
	}

	@Override
	protected void send(HttpServletRequest request, HttpServletResponse response, ImageValidateCode validateCode)
			throws ValidateCodeException {
		try {
			ImageIO.write(validateCode.getImage(), JPEG_IMAGE_TYPE, response.getOutputStream());
		}
		catch (IOException e) {
			throw new ValidateCodeException("IMAGE_CODE_CREATE_FAIL");
		}
	}

	@Override
	protected boolean validate(String code, ImageValidateCode validateCode) {
		return StrUtil.equalsIgnoreCase(code, validateCode.getCode());
	}

}
