package com.relaxed.common.xss.toolkit;

import cn.hutool.core.util.StrUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Safelist;

/**
 * @author Yakir
 * @Topic HtmlUtils
 * @Description
 * @date 2021/6/26 14:16
 * @Version 1.0
 */
public final class HtmlKit {

	private HtmlKit() {
	}

	private static final Safelist WHITELIST = Safelist.relaxed();

	static {

		// 富文本编辑时一些样式是使用 style 来进行实现的
		// 比如红色字体 style="color:red;", 所以需要给所有标签添加 style 属性
		// 注意：style 属性会有注入风险 <img STYLE="background-image:url(javascript:alert('XSS'))">
		WHITELIST.addAttributes(":all", "style", "class");
		// 保留 a 标签的 target 属性
		WHITELIST.addAttributes("a", "target");
		// 支持img 为base64
		WHITELIST.addProtocols("img", "src", "data");

		// 保留相对路径, 保留相对路径时，必须提供对应的 baseUri 属性，否则依然会被删除
		// WHITELIST.preserveRelativeLinks(false);

		// 移除 a 标签和 img 标签的一些协议限制，这会导致 xss 防注入失效，如 <img src=javascript:alert("xss")>
		// 虽然可以重写 WhiteList#isSafeAttribute 来处理，但是有隐患，所以暂时不支持相对路径
		// WHITELIST.removeProtocols("a", "href", "ftp", "http", "https", "mailto");
		// WHITELIST.removeProtocols("img", "src", "http", "https");
	}

	/**
	 * html 转字符串，保留换行样式
	 * @link https://stackoverflow.com/questions/5640334/how-do-i-preserve-line-breaks-when-using-jsoup-to-convert-html-to-plain-text
	 * @param html html字符串
	 * @param mergeLineBreak 是否合并换行符
	 * @return 保留换行格式的纯文本
	 */
	public static String toText(String html, boolean mergeLineBreak) {
		if (StrUtil.isBlank(html)) {
			return html;
		}
		Document document = Jsoup.parse(html);
		// makes html() preserve linebreaks and spacing
		document.outputSettings(new Document.OutputSettings().prettyPrint(true));
		String result = document.wholeText();

		// 合并多个换行
		if (mergeLineBreak) {
			int oldLength;
			do {
				oldLength = result.length();
				result = result.replace('\r', '\n');
				result = result.replace("\n\n", "\n");
			}
			while (result.length() != oldLength);
		}

		return result;
	}

	/**
	 * html 转字符串，保留换行样式，默认合并换行符
	 * @param html html字符串
	 * @return 保留换行格式的纯文本
	 */
	public static String toText(String html) {
		return toText(html, true);
	}

	/**
	 * <p>
	 * 清理不安全的 Html 标签。保留换行符
	 * </p>
	 * 白名单配置参见：{@link HtmlKit#WHITELIST}
	 * @see Safelist#relaxed()
	 * @param bodyHtml HTML 文本
	 * @return 清理后的 HTML 文本
	 */
	public static String cleanUnSafe(String bodyHtml) {
		return cleanUnSafe(bodyHtml, WHITELIST);
	}

	/**
	 * <p>
	 * 清理不安全的 Html 标签。保留换行符
	 * </p>
	 * @param bodyHtml HTML 文本
	 * @param whitelist 白名单配置
	 * @return 清理后的 HTML 文本
	 */
	public static String cleanUnSafe(String bodyHtml, Safelist whitelist) {
		return Jsoup.clean(bodyHtml, "", whitelist, new Document.OutputSettings().prettyPrint(false));
	}

}
