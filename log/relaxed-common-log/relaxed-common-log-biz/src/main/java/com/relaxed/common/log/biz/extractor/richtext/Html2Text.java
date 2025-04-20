package com.relaxed.common.log.biz.extractor.richtext;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * HTML 转文本工具类，用于将 HTML 内容转换为纯文本格式。 该类继承自 {@link HTMLEditorKit.ParserCallback}，通过解析 HTML
 * 标签和内容， 生成格式化的纯文本，并特殊处理图片标签。
 *
 * @author Yakir
 * @since 1.0.0
 */
public class Html2Text extends HTMLEditorKit.ParserCallback {

	/**
	 * 行分隔符
	 */
	private String lineBreak = "\n";

	/**
	 * 文本内容构建器
	 */
	private StringBuilder stringBuilder = new StringBuilder();

	/**
	 * 将 HTML 内容转换为简单文本
	 * @param html HTML 内容
	 * @return 转换后的纯文本，如果转换失败则返回空字符串
	 */
	public static String simpleHtml(String html) {
		try {
			if (html.isEmpty()) {
				return "";
			}
			Html2Text parser = new Html2Text();
			parser.parse(html);
			return parser.getText() != null ? parser.getText() : "";
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解析 HTML 内容
	 * @param html HTML 内容
	 * @throws IOException 解析过程中可能出现的 IO 异常
	 */
	private void parse(String html) throws IOException {
		Reader reader = new StringReader(html);
		ParserDelegator parsers = new ParserDelegator();
		parsers.parse(reader, this, Boolean.TRUE);
	}

	/**
	 * 处理文本内容
	 * @param text 文本内容
	 * @param pos 文本位置
	 */
	@Override
	public void handleText(char[] text, int pos) {
		stringBuilder.append(text);
	}

	/**
	 * 处理开始标签
	 * @param t HTML 标签
	 * @param a 标签属性
	 * @param pos 标签位置
	 */
	@Override
	public void handleStartTag(HTML.Tag t, MutableAttributeSet a, int pos) {
		if (stringBuilder.length() != 0 && t.isBlock() && !stringBuilder.toString().endsWith(lineBreak)) {
			stringBuilder.append(lineBreak);
		}
	}

	/**
	 * 处理结束标签
	 * @param t HTML 标签
	 * @param pos 标签位置
	 */
	@Override
	public void handleEndTag(HTML.Tag t, int pos) {
		if (stringBuilder.length() != 0 && t.isBlock() && !stringBuilder.toString().endsWith(lineBreak)) {
			stringBuilder.append(lineBreak);
		}
	}

	/**
	 * 处理行结束符
	 * @param eol 行结束符
	 */
	@Override
	public void handleEndOfLineString(String eol) {
		if (stringBuilder.length() - lineBreak.length() > 0) {
			stringBuilder.delete(stringBuilder.length() - lineBreak.length(), stringBuilder.length());
		}
	}

	/**
	 * 处理简单标签（如 img）
	 * @param t HTML 标签
	 * @param a 标签属性
	 * @param pos 标签位置
	 */
	@Override
	public void handleSimpleTag(HTML.Tag t, MutableAttributeSet a, int pos) {
		// 处理图片标签
		if (HTML.Tag.IMG.equals(t)) {
			stringBuilder.append(Constant.imgLeftPlaceholder).append(a.toString()).append(Constant.imgRightPlaceholder);
		}
	}

	/**
	 * 获取转换后的文本内容
	 * @return 转换后的文本内容
	 */
	private String getText() {
		return stringBuilder.toString();
	}

}
