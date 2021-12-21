package com.relaxed.extend.dingtalk.message;

import com.relaxed.extend.dingtalk.enums.MessageTypeEnum;
import com.relaxed.extend.dingtalk.markdown.MarkdownBuilder;
import com.relaxed.extend.dingtalk.request.DingTalkParams;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author lingting 2020/6/10 22:13
 */
@Getter
@Setter
@Accessors(chain = true)
public class DingTalkMarkDownMessage extends AbstractDingTalkMessage {

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 内容
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
