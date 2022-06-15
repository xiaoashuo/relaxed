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
 * MarkDown Message
 *
 * @author Yakir
 */
@Getter
@Setter
@Accessors(chain = true)
public class WechatMarkDownMessage extends AbstractWechatMessage {

	/**
	 * 内容
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
