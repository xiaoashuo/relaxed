package com.relaxed.extend.dingtalk.markdown;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Markdown文本构建器。 用于构建钉钉消息中的Markdown格式文本，支持标题、引用、加粗、斜体、列表、图片和链接等格式。
 *
 * @author lingting
 * @since 1.0
 */
public class MarkdownBuilder {

	public static final String TITLE_PREFIX = "#";

	public static final String QUOTE_PREFIX = "> ";

	public static final String BOLD_PREFIX = "**";

	public static final String ITALIC_PREFIX = "*";

	public static final String UNORDERED_LIST_PREFIX = "- ";

	public static final String ORDER_LIST_PREFIX = ". ";

	/**
	 * 存放内容
	 */
	private final List<String> content = new ArrayList<>();

	/**
	 * 当前操作行文本
	 */
	private StringBuilder lineTextBuilder;

	public MarkdownBuilder() {
		this.lineTextBuilder = new StringBuilder();
	}

	/**
	 * 添加自定义内容
	 * @param content 自定义内容
	 * @return this
	 */
	public MarkdownBuilder append(String content) {
		lineTextBuilder.append(content);
		return this;
	}

	/**
	 * 有序列表 自动生成索引
	 * @param content 文本
	 * @return this
	 */
	public MarkdownBuilder orderList(String content) {
		// 获取最后一个字符串
		String tmp = "";
		if (!this.content.isEmpty()) {
			tmp = this.content.get(this.content.size() - 1);
		}
		// 索引
		int index = 1;

		// 校验 是否 为有序列表行的正则
		String isOrderListPattern = "^\\d\\. .*";
		if (Pattern.matches(isOrderListPattern, tmp)) {
			// 如果是数字开头
			index = Convert.toInt(tmp.substring(0, tmp.indexOf(ORDER_LIST_PREFIX) - 1));
		}
		return orderList(index, content);
	}

	/**
	 * 有序列表
	 * @param index 索引
	 * @param content 文本
	 * @return this
	 */
	public MarkdownBuilder orderList(int index, String content) {
		lineBreak();
		lineTextBuilder.append(index).append(ORDER_LIST_PREFIX).append(content);
		return this;
	}

	/**
	 * 无序列表
	 * @param content 列表项内容
	 * @return this
	 */
	public MarkdownBuilder unorderedList(String content) {
		// 换行
		lineBreak();
		lineTextBuilder.append(UNORDERED_LIST_PREFIX).append(content);
		return this;
	}

	/**
	 * 添加图片
	 * @param url 图片链接
	 * @return this
	 */
	public MarkdownBuilder pic(String url) {
		return pic(StrUtil.EMPTY, url);
	}

	/**
	 * 添加图片
	 * @param title 图片标题
	 * @param url 图片路径
	 * @return this
	 */
	public MarkdownBuilder pic(String title, String url) {
		lineTextBuilder.append("![").append(title).append("](").append(url).append(")");
		return this;
	}

	/**
	 * 添加链接
	 * @param title 链接标题
	 * @param url 链接地址
	 * @return this
	 */
	public MarkdownBuilder link(String title, String url) {
		lineTextBuilder.append("[").append(title).append("](").append(url).append(")");
		return this;
	}

	/**
	 * 添加斜体文本
	 * @param content 文本内容
	 * @return this
	 */
	public MarkdownBuilder italic(String content) {
		lineTextBuilder.append(ITALIC_PREFIX).append(content).append(ITALIC_PREFIX);
		return this;
	}

	/**
	 * 添加加粗文本
	 * @param content 文本内容
	 * @return this
	 */
	public MarkdownBuilder bold(String content) {
		lineTextBuilder.append(BOLD_PREFIX).append(content).append(BOLD_PREFIX);
		return this;
	}

	/**
	 * 添加引用文本
	 * @param content 引用内容
	 * @return this
	 */
	public MarkdownBuilder quote(String content) {
		lineBreak();
		this.content.add(QUOTE_PREFIX + content);
		return this;
	}

	/**
	 * 添加引用文本并强制换行
	 * @param content 引用内容
	 * @return this
	 */
	public MarkdownBuilder quoteLineBreak(String content) {
		quote(content);
		return forceLineBreak();
	}

	/**
	 * 强制换行
	 * @return this
	 */
	public MarkdownBuilder forceLineBreak() {
		content.add(lineTextBuilder.toString());
		lineTextBuilder = new StringBuilder();
		return this;
	}

	/**
	 * 换行（当已编辑文本长度不为0时换行）
	 * @return this
	 */
	public MarkdownBuilder lineBreak() {
		if (lineTextBuilder.length() != 0) {
			return forceLineBreak();
		}
		return this;
	}

	/**
	 * 生成i级标题
	 * @param i 标题级别（1-5）
	 * @param content 标题内容
	 * @return this
	 */
	private MarkdownBuilder title(int i, String content) {
		// 如果当前操作行已有字符，需要换行
		lineBreak();
		for (int j = 0; j < i; j++) {
			lineTextBuilder.append(TITLE_PREFIX);
		}
		this.content.add(lineTextBuilder.append(" ").append(content).toString());
		lineTextBuilder = new StringBuilder();
		return this;
	}

	/**
	 * 生成一级标题
	 * @param text 标题内容
	 * @return this
	 */
	public MarkdownBuilder title1(String text) {
		return title(1, text);
	}

	/**
	 * 生成二级标题
	 * @param text 标题内容
	 * @return this
	 */
	public MarkdownBuilder title2(String text) {
		return title(2, text);
	}

	/**
	 * 生成三级标题
	 * @param text 标题内容
	 * @return this
	 */
	public MarkdownBuilder title3(String text) {
		return title(3, text);
	}

	/**
	 * 生成四级标题
	 * @param text 标题内容
	 * @return this
	 */
	public MarkdownBuilder title4(String text) {
		return title(4, text);
	}

	/**
	 * 生成五级标题
	 * @param text 标题内容
	 * @return this
	 */
	public MarkdownBuilder title5(String text) {
		return title(5, text);
	}

	@Override
	public String toString() {
		return build();
	}

	/**
	 * 构建Markdown文本
	 * @return 构建完成的Markdown文本
	 */
	public String build() {
		lineBreak();
		StringBuilder res = new StringBuilder();
		content.forEach(line -> res.append(line).append(" \n"));
		return res.toString();
	}

}
