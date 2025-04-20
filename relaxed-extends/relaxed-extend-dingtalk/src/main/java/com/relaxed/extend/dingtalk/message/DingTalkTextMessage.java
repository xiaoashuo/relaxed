package com.relaxed.extend.dingtalk.message;

import com.relaxed.extend.dingtalk.enums.MessageTypeEnum;
import com.relaxed.extend.dingtalk.request.DingTalkParams;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 钉钉文本消息实现类。 用于发送纯文本类型的钉钉消息，支持@指定人员或@所有人。
 *
 * @author lingting
 * @since 1.0
 */
@Getter
@Setter
@Accessors(chain = true)
public class DingTalkTextMessage extends AbstractDingTalkMessage {

	/**
	 * 消息文本内容
	 */
	private String content;

	@Override
	public MessageTypeEnum getType() {
		return MessageTypeEnum.TEXT;
	}

	@Override
	public DingTalkParams put(DingTalkParams params) {
		return params.setText(new DingTalkParams.Text().setContent(content));
	}

}
