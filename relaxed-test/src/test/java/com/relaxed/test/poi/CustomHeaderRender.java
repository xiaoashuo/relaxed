package com.relaxed.test.poi;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.ddr.poi.html.HtmlConstants;
import org.ddr.poi.html.HtmlRenderContext;
import org.ddr.poi.html.tag.HeaderRenderer;
import org.ddr.poi.html.util.CSSStyleUtils;
import org.ddr.poi.html.util.RenderUtils;
import org.jsoup.nodes.Element;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Yakir
 * @Topic CustomHeaderRender
 * @Description 自定义标题样式 标题样式 参考 https://github.com/draco1023/poi-tl-ext/issues/105
 * @date 2024/12/16 13:04
 * @Version 1.0
 */
@Slf4j
public class CustomHeaderRender extends HeaderRenderer {

	String regex = "^\\d+(\\.\\d+)*\\s?.*$";

	public static String removePrefix(String text) {
		// 正则表达式：删除前缀部分
		return StrUtil.removePrefix(text.replaceAll("^\\s*\\d+(\\s*\\.\\s*\\d+)*\\s*", "").trim(), ".");
	}

	@Override
	public boolean renderStart(Element element, HtmlRenderContext context) {
		String text = element.text();
		if (text.matches(regex)) {
			String changeText = "3." + text;
			element.text(removePrefix(changeText));
			// String ret = getMatchString(text);
			// List<String> retArr = StrUtil.split(ret, ".");
			// int retSize = retArr.size() ;
			// 1=2
			/**
			 * wps中heading1是从2开始 office中是从1开始，如果是wps中需要修改为 String headingStyleId = "" +
			 * (index + 2); h1=1位 1-1 =0 2 h2=2位 2-1 =1 3 h3=3位 3-1 =2 4 h4=4位 4-3=1 5
			 */
			// 设置样式
			int index = Integer.parseInt(element.normalName().substring(1)) - 1;
			String headingStyleId = "" + (index + 1 + 1);
			if (index == 3) {
				headingStyleId = "" + (index + 1 + 1 + 1);
			}

			CTP ctp = context.getClosestParagraph().getCTP();
			CTPPr ppr = RenderUtils.getPPr(ctp) != null ? RenderUtils.getPPr(ctp) : ctp.addNewPPr();
			CTString pStyle = ppr.getPStyle();
			if (pStyle == null) {
				pStyle = CTString.Factory.newInstance();
			}
			pStyle.setVal(headingStyleId);
			ppr.setPStyle(pStyle);
		}
		else if (text.equals("更新记录")) {
			int index = Integer.parseInt(element.normalName().substring(1)) - 1;
			String headingStyleId = "" + (index + 1 + 1);
			if (index == 3) {
				headingStyleId = "" + (index + 1 + 1 + 1);
			}

			CTP ctp = context.getClosestParagraph().getCTP();
			CTPPr ppr = RenderUtils.getPPr(ctp) != null ? RenderUtils.getPPr(ctp) : ctp.addNewPPr();
			CTString pStyle = ppr.getPStyle();
			if (pStyle == null) {
				pStyle = CTString.Factory.newInstance();
			}
			pStyle.setVal(headingStyleId);
			ppr.setPStyle(pStyle);
		}

		// return super.renderStart(element, context);
		// 返回不带粗体样式的
		return noBold(element, context);

	}

	private static final String[] FONT_SIZES = { "24pt", "18pt", "14pt", "12pt", "10pt", "7.5pt" };

	private static boolean noBold(Element element, HtmlRenderContext context) {
		int index = Integer.parseInt(element.normalName().substring(1)) - 1 + 1;

		String fontSize = FONT_SIZES[index];
		if (index == 1) {
			fontSize = "16pt";
		}
		String fontSizeStyle = HtmlConstants.inlineStyle(HtmlConstants.CSS_FONT_SIZE, fontSize);

		context.pushInlineStyle(CSSStyleUtils.parse(fontSizeStyle), element.isBlock());

		CTP ctp = context.getClosestParagraph().getCTP();
		CTDecimalNumber ctDecimalNumber = CTDecimalNumber.Factory.newInstance();
		ctDecimalNumber.setVal(BigInteger.valueOf(index));
		RenderUtils.getPPr(ctp).setOutlineLvl(ctDecimalNumber);
		return true;
	}

}
