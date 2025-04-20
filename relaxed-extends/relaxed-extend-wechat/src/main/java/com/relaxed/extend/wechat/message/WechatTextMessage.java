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
 * 企业微信文本消息类。 用于发送文本类型的消息，支持@指定成员或所有人。
 *
 * @author Yakir
 * @since 1.0
 */
@Getter
@Accessors(chain = true)
public class WechatTextMessage extends AbstractWechatMessage {

	/**
	 * 文本内容，最长不超过2048个字节，必须是utf8编码
	 */
	private String content;

	/**
	 * userid的列表，提醒群中的指定成员(@某个成员)，@all表示提醒所有人
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

	/**
	 * 设置消息内容
	 * @param content 消息内容
	 * @return 当前对象
	 */
	public WechatTextMessage setContent(String content) {
		this.content = content;
		return this;
	}

	/**
	 * 设置@所有人
	 * @return 当前对象
	 */
	public WechatTextMessage atAll() {
		mentionedList.add("@all");
		return this;
	}

	/**
	 * 添加需要@的用户ID
	 * @param mentioned 用户ID
	 * @return 当前对象
	 */
	public WechatTextMessage addMentioned(String mentioned) {
		mentionedList.add(mentioned);
		return this;
	}

	/**
	 * 添加需要@的手机号
	 * @param mentionedMobile 手机号
	 * @return 当前对象
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
