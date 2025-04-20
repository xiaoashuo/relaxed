package com.relaxed.common.xss.toolkit;

import cn.hutool.core.util.StrUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Safelist;

/**
 * HTML工具类 提供HTML文本的清理和转换功能，主要用于XSS防护 使用Jsoup库进行HTML解析和清理
 *
 * @author Yakir
 * @since 1.0
 */
public final class HtmlKit {

	/**
	 * 私有构造函数，防止实例化
	 */
	private HtmlKit() {
	}

	/**
	 * HTML白名单配置 使用Jsoup的Safelist.relaxed()作为基础配置 允许所有标签，但会过滤掉不安全的属性和内容
	 */
	private static final Safelist WHITELIST = Safelist.relaxed();

	static {
		// 富文本编辑时一些样式是使用style来进行实现的
		// 比如红色字体 style="color:red;", 所以需要给所有标签添加style属性
		// 注意：style属性会有注入风险 <img STYLE="background-image:url(javascript:alert('XSS'))">
		WHITELIST.addAttributes(":all", "style", "class");
		// 保留a标签的target属性
		WHITELIST.addAttributes("a", "target");
		// 支持img为base64
		WHITELIST.addProtocols("img", "src", "data");

		// 保留相对路径, 保留相对路径时，必须提供对应的baseUri属性，否则依然会被删除
		// WHITELIST.preserveRelativeLinks(false);

		// 移除a标签和img标签的一些协议限制，这会导致xss防注入失效，如<img src=javascript:alert("xss")>
		// 虽然可以重写WhiteList#isSafeAttribute来处理，但是有隐患，所以暂时不支持相对路径
		// WHITELIST.removeProtocols("a", "href", "ftp", "http", "https", "mailto");
		// WHITELIST.removeProtocols("img", "src", "http", "https");
	}

	/**
	 * HTML转纯文本，保留换行样式
	 * @param html HTML字符串
	 * @param mergeLineBreak 是否合并换行符
	 * @return 保留换行格式的纯文本
	 */
	public static String toText(String html, boolean mergeLineBreak) {
		if (StrUtil.isBlank(html)) {
			return html;
		}
		Document document = Jsoup.parse(html);
		// 使html()保留换行和空格
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
	 * HTML转纯文本，保留换行样式，默认合并换行符
	 * @param html HTML字符串
	 * @return 保留换行格式的纯文本
	 */
	public static String toText(String html) {
		return toText(html, true);
	}

	/**
	 * 清理不安全的HTML标签，保留换行符 使用默认的白名单配置进行清理
	 * @param bodyHtml HTML文本
	 * @return 清理后的HTML文本
	 * @see Safelist#relaxed()
	 */
	public static String cleanUnSafe(String bodyHtml) {
		return cleanUnSafe(bodyHtml, WHITELIST);
	}

	/**
	 * 清理不安全的HTML标签，保留换行符 使用指定的白名单配置进行清理
	 * @param bodyHtml HTML文本
	 * @param whitelist 白名单配置
	 * @return 清理后的HTML文本
	 */
	public static String cleanUnSafe(String bodyHtml, Safelist whitelist) {
		return Jsoup.clean(bodyHtml, "", whitelist, new Document.OutputSettings().prettyPrint(false));
	}

}
