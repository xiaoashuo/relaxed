package com.relaxed.extend.wechat.message;

import com.relaxed.extend.wechat.enums.MessageTypeEnum;
import com.relaxed.extend.wechat.markdown.MarkdownBuilder;
import com.relaxed.extend.wechat.request.WechatParams;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Set;

/**
 * 企业微信Markdown消息类。 用于发送Markdown格式的消息，支持丰富的文本格式。
 *
 * @author Yakir
 * @since 1.0
 */
@Getter
@Setter
@Accessors(chain = true)
public class WechatMarkDownMessage extends AbstractWechatMessage {

	/**
	 * Markdown内容构建器
	 */
	private MarkdownBuilder text;

	@Override
	public MessageTypeEnum getType() {
		return MessageTypeEnum.MARKDOWN;
	}

	@Override
	public WechatParams put(WechatParams params) {
		return params.setMarkdown(new WechatParams.Markdown().setContent(text.build()));
	}

}
