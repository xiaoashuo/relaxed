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
 * @author Yakir
 * @Topic WechatParams
 * @Description
 * @date 2022/6/15 11:48
 * @Version 1.0
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

	@Data
	@Accessors(chain = true)
	public static class Text {

		private String content;

		@JsonProperty("mentioned_list")
		private Set<String> mentionedList;

		@JsonProperty("mentioned_mobile_list")
		private Set<String> mentionedMobileList;

	}

	@Data
	@Accessors(chain = true)
	public static class Markdown {

		/**
		 * markdown内容，最长不超过4096个字节，必须是utf8编码
		 */
		private String content;

	}

	@Data
	@Accessors(chain = true)
	public static class News {

		/**
		 * 标题，不超过128个字节，超过会自动截断 required true
		 */
		private String title;

		/**
		 * 描述，不超过512个字节，超过会自动截断 required false
		 */
		private String description;

		/**
		 * 点击后跳转的链接。 required true
		 */
		private String url;

		/**
		 * 图文消息的图片链接，支持JPG、PNG格式，较好的效果为大图 1068*455，小图150*150。 required false
		 */
		private String picurl;

		/**
		 * 图文消息，一个图文消息支持1到8条图文 required true
		 */
		private List<Article> articles;

	}

	@Override
	@SneakyThrows
	public String toString() {
		return new ObjectMapper().writeValueAsString(this);
	}

}
