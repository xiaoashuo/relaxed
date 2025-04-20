package com.relaxed.extend.dingtalk.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.relaxed.extend.dingtalk.message.DingTalkActionCardMessage;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Set;

/**
 * 钉钉消息参数封装类。 用于构建钉钉机器人消息的请求参数，支持文本、Markdown、链接、动作卡片等多种消息类型。
 *
 * @author lingting
 * @since 1.0
 */
@Getter
@Setter
@Accessors(chain = true)
public class DingTalkParams {

	/**
	 * 消息类型
	 */
	@JsonProperty("msgtype")
	private String type;

	/**
	 * @标记参数
	 */
	private At at;

	/**
	 * 动作卡片参数
	 */
	private ActionCard actionCard;

	/**
	 * 链接消息参数
	 */
	private Link link;

	/**
	 * Markdown消息参数
	 */
	private Markdown markdown;

	/**
	 * 文本消息参数
	 */
	private Text text;

	@Override
	@SneakyThrows
	public String toString() {
		return new ObjectMapper().writeValueAsString(this);
	}

	/**
	 * 文本消息参数类。 用于构建钉钉文本类型消息的内容。
	 */
	@Data
	@Accessors(chain = true)
	public static class Text {

		/**
		 * 消息内容
		 */
		private String content;

	}

	/**
	 * Markdown消息参数类。 用于构建钉钉 Markdown 类型消息的标题和内容。
	 */
	@Data
	@Accessors(chain = true)
	public static class Markdown {

		/**
		 * 消息标题
		 */
		private String title;

		/**
		 * Markdown格式的消息内容
		 */
		private String text;

	}

	/**
	 * 链接消息参数类。 用于构建钉钉链接类型消息的标题、内容、图片和跳转链接。
	 */
	@Data
	@Accessors(chain = true)
	public static class Link {

		/**
		 * 消息内容
		 */
		private String text;

		/**
		 * 消息标题
		 */
		private String title;

		/**
		 * 图片URL
		 */
		private String picUrl;

		/**
		 * 点击消息跳转的URL
		 */
		private String messageUrl;

	}

	/**
	 * 动作卡片消息参数类。 用于构建钉钉动作卡片类型消息的标题、内容和交互按钮。
	 */
	@Data
	@Accessors(chain = true)
	public static class ActionCard {

		/**
		 * 消息标题
		 */
		private String title;

		/**
		 * 消息内容
		 */
		private String text;

		/**
		 * 按钮排列方向 0：按钮竖直排列 1：按钮横向排列
		 */
		private String btnOrientation;

		/**
		 * 单个按钮的标题
		 */
		private String singleTitle;

		/**
		 * 单个按钮的跳转链接
		 */
		@JsonProperty("singleURL")
		private String singleUrl;

		/**
		 * 多个按钮的配置列表
		 */
		@JsonProperty("btns")
		private List<DingTalkActionCardMessage.Button> buttons;

	}

	/**
	 * 用于配置钉钉消息的@提醒功能。
	 */
	@Data
	@Accessors(chain = true)
	public static class At {

		/**
		 * 是否@所有人
		 */
		@JsonProperty("isAtAll")
		private boolean atAll;

		/**
		 * 需要@的手机号列表
		 */
		private Set<String> atMobiles;

	}

}
