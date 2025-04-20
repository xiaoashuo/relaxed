package com.relaxed.extend.dingtalk.message;

import com.relaxed.extend.dingtalk.enums.MessageTypeEnum;
import com.relaxed.extend.dingtalk.markdown.MarkdownBuilder;
import com.relaxed.extend.dingtalk.request.DingTalkParams;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 钉钉 Markdown 消息实现类。 用于发送支持 Markdown 格式的钉钉消息，支持标题、文本样式、图片、链接等丰富的消息内容。
 *
 * @author lingting
 * @since 1.0
 */
@Getter
@Setter
@Accessors(chain = true)
public class DingTalkMarkDownMessage extends AbstractDingTalkMessage {

	/**
	 * 消息标题 会在钉钉客户端的会话列表上显示的标题
	 */
	private String title;

	/**
	 * Markdown 格式的消息内容构建器 用于构建支持 Markdown 语法的消息内容，包括标题、加粗、斜体、图片、链接等格式
	 */
	private MarkdownBuilder text;

	@Override
	public MessageTypeEnum getType() {
		return MessageTypeEnum.MARKDOWN;
	}

	@Override
	public DingTalkParams put(DingTalkParams params) {
		return params.setMarkdown(new DingTalkParams.Markdown().setTitle(title).setText(text.build()));
	}

}
