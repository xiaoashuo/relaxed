package com.relaxed.extend.dingtalk.message;

import com.relaxed.extend.dingtalk.enums.MessageTypeEnum;
import com.relaxed.extend.dingtalk.request.DingTalkParams;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 钉钉链接消息实现类。 用于发送带有链接跳转的消息，支持标题、描述文本、图片和跳转链接的设置。 消息会以卡片形式展示在钉钉会话中。
 *
 * @author lingting
 * @since 1.0
 */
@Getter
@Setter
@Accessors(chain = true)
public class DingTalkLinkMessage extends AbstractDingTalkMessage {

	/**
	 * 消息描述文本 会在卡片消息中显示的描述信息
	 */
	private String text;

	/**
	 * 消息标题 会在卡片消息中显示的标题文本
	 */
	private String title;

	/**
	 * 图片URL地址 会在卡片消息中显示的图片，建议使用宽比高大的图片
	 */
	private String picUrl;

	/**
	 * 点击消息跳转的URL 当点击消息时会跳转到这个链接
	 */
	private String messageUrl;

	@Override
	public MessageTypeEnum getType() {
		return MessageTypeEnum.LINK;
	}

	@Override
	public DingTalkParams put(DingTalkParams params) {
		return params.setLink(
				new DingTalkParams.Link().setText(text).setTitle(title).setPicUrl(picUrl).setMessageUrl(messageUrl));
	}

}
