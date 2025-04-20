package com.relaxed.extend.dingtalk.message;

import com.relaxed.extend.dingtalk.enums.ActionBtnOrientationEnum;
import com.relaxed.extend.dingtalk.enums.MessageTypeEnum;
import com.relaxed.extend.dingtalk.markdown.MarkdownBuilder;
import com.relaxed.extend.dingtalk.request.DingTalkParams;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * 钉钉动作卡片消息实现类。 用于发送可交互的卡片消息，支持标题、Markdown内容、按钮布局和自定义按钮组。 可以配置单个按钮或多个按钮，每个按钮都可以设置独立的跳转链接。
 *
 * @author lingting
 * @since 1.0
 */
@Getter
@Setter
@Accessors(chain = true)
public class DingTalkActionCardMessage extends AbstractDingTalkMessage {

	/**
	 * 卡片消息标题 会在钉钉客户端的会话列表上显示的标题
	 */
	private String title;

	/**
	 * Markdown格式的消息内容构建器 用于构建卡片正文内容，支持Markdown语法
	 */
	private MarkdownBuilder text;

	/**
	 * 按钮排列方向 默认为横向排列（{@link ActionBtnOrientationEnum#HORIZONTAL}）
	 */
	private ActionBtnOrientationEnum orientation = ActionBtnOrientationEnum.HORIZONTAL;

	/**
	 * 单个按钮的标题文本 当不配置按钮组时使用此配置
	 */
	private String singleTitle;

	/**
	 * 单个按钮的跳转链接 当不配置按钮组时使用此配置
	 */
	private String singleUrl;

	/**
	 * 自定义按钮组 当配置了按钮组时，单按钮的配置（singleTitle和singleUrl）将被忽略
	 */
	private List<Button> buttons = new ArrayList<>();

	/**
	 * 添加自定义按钮到按钮组。
	 * @param title 按钮标题
	 * @param url 按钮跳转链接
	 * @return 当前消息实例
	 */
	public DingTalkActionCardMessage addButton(String title, String url) {
		buttons.add(new Button(title, url));
		return this;
	}

	@Override
	public MessageTypeEnum getType() {
		return MessageTypeEnum.ACTION_CARD;
	}

	@Override
	public DingTalkParams put(DingTalkParams params) {
		DingTalkParams.ActionCard card = new DingTalkParams.ActionCard().setTitle(title).setText(text.build())
				.setBtnOrientation(orientation.getOrientation());

		// 当按钮组为空时使用单按钮配置
		if (buttons.isEmpty()) {
			card.setSingleTitle(singleTitle).setSingleUrl(singleUrl);
		}
		else {
			card.setButtons(buttons);
		}
		return params.setActionCard(card);
	}

	/**
	 * 动作卡片按钮配置类。 用于定义卡片消息中的可点击按钮，包括按钮标题和跳转链接。
	 */
	@Getter
	@AllArgsConstructor
	public static class Button {

		/**
		 * 按钮标题文本 会显示在卡片消息的按钮上
		 */
		private final String title;

		/**
		 * 按钮点击后的跳转链接 当点击按钮时会跳转到这个URL
		 */
		private final String actionURL;

	}

}
