package com.relaxed.extend.wechat.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.relaxed.extend.wechat.message.Article;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Set;

/**
 * 企业微信消息参数类。 用于封装发送到企业微信的各种消息类型的参数。
 *
 * @author Yakir
 * @since 1.0
 */
@Getter
@Setter
@Accessors(chain = true)
public class WechatParams {

	@JsonProperty("msgtype")
	private String type;

	private Text text;

	private Markdown markdown;

	private News news;

	/**
	 * 文本消息参数类
	 */
	@Data
	@Accessors(chain = true)
	public static class Text {

		/**
		 * 消息内容
		 */
		private String content;

		/**
		 * 需要@的用户ID列表
		 */
		@JsonProperty("mentioned_list")
		private Set<String> mentionedList;

		/**
		 * 需要@的手机号列表
		 */
		@JsonProperty("mentioned_mobile_list")
		private Set<String> mentionedMobileList;

	}

	/**
	 * Markdown消息参数类
	 */
	@Data
	@Accessors(chain = true)
	public static class Markdown {

		/**
		 * markdown内容，最长不超过4096个字节，必须是utf8编码
		 */
		private String content;

	}

	/**
	 * 图文消息参数类
	 */
	@Data
	@Accessors(chain = true)
	public static class News {

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

		/**
		 * 图文消息，一个图文消息支持1到8条图文
		 */
		private List<Article> articles;

	}

	@Override
	@SneakyThrows
	public String toString() {
		return new ObjectMapper().writeValueAsString(this);
	}

}
