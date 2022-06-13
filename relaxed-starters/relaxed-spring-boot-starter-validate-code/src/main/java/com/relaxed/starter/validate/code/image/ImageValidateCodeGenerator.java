package com.relaxed.starter.validate.code.image;

import com.relaxed.extend.validate.code.domain.ValidateCode;
import com.relaxed.extend.validate.code.generator.ValidateCodeGenerator;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * @author Yakir
 * @Topic ImageValidateCodeGenerator
 * @Description
 * @date 2022/6/12 16:43
 * @Version 1.0
 */
public class ImageValidateCodeGenerator implements ValidateCodeGenerator {

	private ImageProperties imageProperties;

	public ImageValidateCodeGenerator(ImageProperties imageProperties) {
		this.imageProperties = imageProperties;
	}

	@Override
	public ValidateCode createValidateCode(HttpServletRequest request) {
		int height = ServletRequestUtils.getIntParameter(request, "height", imageProperties.getHeight());
		int width = ServletRequestUtils.getIntParameter(request, "width", imageProperties.getWidth());
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		Graphics g = image.getGraphics();

		Random random = new Random();

		g.setColor(getRandColor(200, 250));
		g.fillRect(0, 0, width, height);
		g.setFont(new Font("Times New Roman", Font.ITALIC, 20));
		g.setColor(getRandColor(160, 200));
		for (int i = 0; i < 155; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			g.drawLine(x, y, x + xl, y + yl);
		}

		String sRand = "";
		for (int i = 0; i < imageProperties.getLength(); i++) {
			String rand = String.valueOf(random.nextInt(10));
			sRand += rand;
			g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
			g.drawString(rand, 13 * i + 6, 16);
		}

		g.dispose();

		return new ImageValidateCode(image, sRand, imageProperties.getExpiredInSecond());
	}

	/**
	 * 生成随机背景条纹
	 * @param fc
	 * @param bc
	 * @return
	 */
	private Color getRandColor(int fc, int bc) {
		Random random = new Random();
		if (fc > 255) {
			fc = 255;
		}
		if (bc > 255) {
			bc = 255;
		}
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}

}
