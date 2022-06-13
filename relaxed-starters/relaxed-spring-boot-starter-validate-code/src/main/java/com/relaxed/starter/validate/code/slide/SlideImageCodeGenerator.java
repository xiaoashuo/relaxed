package com.relaxed.starter.validate.code.slide;

import com.relaxed.extend.validate.code.domain.ValidateCode;
import com.relaxed.extend.validate.code.generator.ValidateCodeGenerator;
import com.relaxed.starter.validate.code.util.SlideImageUtil;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

/**
 * @author Yakir
 * @Topic SlideImageCodeGenerator
 * @Description
 * @date 2022/6/12 16:57
 * @Version 1.0
 */
public class SlideImageCodeGenerator implements ValidateCodeGenerator {

	private SlideImageProperties slideImageProperties;

	public SlideImageCodeGenerator(SlideImageProperties slideImageProperties) {
		this.slideImageProperties = slideImageProperties;
	}

	@Override
	public ValidateCode createValidateCode(HttpServletRequest request) {
		try (InputStream in = getOriginImage()) {
			SlideImageUtil.SlideImage slideImage = SlideImageUtil.getVerifyImage(ImageIO.read(in));
			int width = slideImage.getWidth();
			int x = slideImage.getX();
			int height = slideImage.getHeight();
			int y = slideImage.getY();
			double widthXPercentage = width / (x * 1.0);
			double heightYPercentage = height / (y * 1.0);
			String code = widthXPercentage + ":" + heightYPercentage;
			return new SlideImageCode(heightYPercentage, slideImage.getSrcImg(), slideImage.getMarkImg(), code,
					slideImageProperties.getExpiredInSecond());
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private InputStream getOriginImage() throws IOException {
		// 从resources下的slideimg文件夹中随机获取一张图片进行处理
		ClassPathResource classPathResource = new ClassPathResource("slideimg");
		File dirFile = classPathResource.getFile();
		File[] listFiles = dirFile.listFiles();
		int index = new Random().nextInt(listFiles.length);
		return new FileInputStream(listFiles[index]);
	}

}
