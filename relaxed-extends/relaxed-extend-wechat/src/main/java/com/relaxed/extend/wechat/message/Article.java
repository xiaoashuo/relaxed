package com.relaxed.extend.wechat.message;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 企业微信图文消息文章类。 用于定义图文消息中的单篇文章内容。
 *
 * @author Yakir
 * @since 1.0
 */
@Getter
@Setter
@Accessors(chain = true)
public class Article {

	/**
	 * 标题，不超过128个字节，超过会自动截断
	 */
	private String title;

	/**
	 * 描述，不超过512个字节，超过会自动截断
	 */
	private String description;

	/**
	 * 点击后跳转的链接
	 */
	private String url;

	/**
	 * 图文消息的图片链接，支持JPG、PNG格式，较好的效果为大图 1068*455，小图150*150
	 */
	private String picurl;

}
