package com.relaxed.starter.validate.code.slide;

import com.relaxed.extend.validate.code.ValidateCodeException;
import com.relaxed.extend.validate.code.domain.ValidateCodeType;
import com.relaxed.extend.validate.code.processor.AbstractValidateCodeProcessor;
import com.relaxed.extend.validate.code.repository.ValidateCodeRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Yakir
 * @Topic SlideImageCodeProcessor
 * @Description
 * @date 2022/6/12 16:58
 * @Version 1.0
 */
public class SlideImageCodeProcessor extends AbstractValidateCodeProcessor<SlideImageCode> {

	public SlideImageCodeProcessor(@Autowired SlideImageProperties slideImageProperties,
			@Autowired ValidateCodeRepository validateCodeRepository) {
		super(new SlideImageCodeGenerator(slideImageProperties), validateCodeRepository, slideImageProperties);
	}

	@Override
	protected ValidateCodeType getValidateCodeType() {
		return ValidateCodeType.SLIDE;
	}

	@Override
	protected void send(HttpServletRequest request, HttpServletResponse response, SlideImageCode validateCode)
			throws ValidateCodeException {
		double heightY = validateCode.getHeightYPercentage();
		try {
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			// 输出想要成功
			// response.getOutputStream()
			// .write(
			// CommonResponse.successInstance(
			// new SlideValidateCodeImage(
			// heightY,
			// validateCode.getSrcImg(),
			// validateCode.getMarkImg()))
			// .toJson()
			// .getBytes());
		}
		catch (Exception e) {
			throw new ValidateCodeException("图片验证码生成失败");
		}
	}

	/**
	 * 滑动验证码验证
	 * @param code
	 * @param validateCode
	 * @return
	 */
	@Override
	protected boolean validate(String code, SlideImageCode validateCode) {
		try {
			// String[] location =
			// StringUtils.splitByWholeSeparatorPreserveAllTokens(code, ":");
			// double x1 = CastUtil.castDouble(location[0]);
			// double y1 = CastUtil.castDouble(location[1]);
			// String sessionCode = validateCode.getCode();
			// String[] sessionLocation =
			// StringUtils.splitByWholeSeparatorPreserveAllTokens(sessionCode, ":");
			// double x2 = CastUtil.castDouble(sessionLocation[0]);
			// double y2 = CastUtil.castDouble(sessionLocation[1]);
			// double distance = Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2),
			// 2));
			// return distance < 0.06;
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	private static class SlideValidateCodeImage {

		private double heightY;

		private String srcImg;

		private String markImg;

	}

}
