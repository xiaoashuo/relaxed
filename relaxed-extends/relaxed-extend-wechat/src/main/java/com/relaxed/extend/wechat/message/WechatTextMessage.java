package com.relaxed.extend.wechat.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.relaxed.extend.wechat.enums.MessageTypeEnum;
import com.relaxed.extend.wechat.request.WechatParams;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Yakir
 * @Topic WechatTextMessage
 * @Description
 * @date 2022/6/15 11:58
 * @Version 1.0
 */
@Getter
@Accessors(chain = true)
public class WechatTextMessage extends AbstractWechatMessage {

	/**
	 * required 文本内容，最长不超过2048个字节，必须是utf8编码
	 */
	private String content;

	/**
	 * userid的列表，提醒群中的指定成员(@某个成员)，@all表示提醒所有人，如果开发者获取不到userid，可以使用mentioned_mobile_list
	 */
	private Set<String> mentionedList = new HashSet<>();

	/**
	 * 手机号列表，提醒手机号对应的群成员(@某个成员)，@all表示提醒所有人
	 */
	private Set<String> mentionedMobileList = new HashSet<>();

	@Override
	public MessageTypeEnum getType() {
		return MessageTypeEnum.TEXT;
	}

	public WechatTextMessage setContent(String content) {
		this.content = content;
		return this;
	}

	public WechatTextMessage atAll() {
		mentionedList.add("@all");
		return this;
	}

	/**
	 * 添加通知人员 根据账号id
	 * @author yakir
	 * @date 2022/6/15 13:57
	 * @param mentioned
	 * @return com.relaxed.extend.wechat.message.WechatTextMessage
	 */
	public WechatTextMessage addMentioned(String mentioned) {
		mentionedList.add(mentioned);
		return this;
	}

	/**
	 * 添加通知人员根据手机号
	 * @author yakir
	 * @date 2022/6/15 13:57
	 * @param mentionedMobile
	 * @return com.relaxed.extend.wechat.message.WechatTextMessage
	 */
	public WechatTextMessage addMentionedMobile(String mentionedMobile) {
		mentionedMobileList.add(mentionedMobile);
		return this;
	}

	@Override
	public WechatParams put(WechatParams params) {
		return params.setText(new WechatParams.Text().setContent(content).setMentionedList(mentionedList)
				.setMentionedMobileList(mentionedMobileList));
	}

}
