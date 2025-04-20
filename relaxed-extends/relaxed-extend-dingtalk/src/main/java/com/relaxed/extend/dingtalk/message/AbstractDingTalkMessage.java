package com.relaxed.extend.dingtalk.message;

import com.relaxed.extend.dingtalk.enums.MessageTypeEnum;
import com.relaxed.extend.dingtalk.request.DingTalkParams;

import java.util.HashSet;
import java.util.Set;

/**
 * 钉钉消息抽象基类。 实现了钉钉消息的通用功能，包括@人员设置和消息生成等基础功能。 具体的消息类型需要继承此类并实现特定的消息内容处理逻辑。
 *
 * @author lingting
 * @since 1.0
 */
public abstract class AbstractDingTalkMessage implements DingTalkMessage {

	/**
	 * 需要@的人员手机号码集合
	 */
	private final Set<String> atPhones = new HashSet<>();

	/**
	 * 是否@所有人的标志
	 */
	private boolean atAll = false;

	/**
	 * 设置@所有人。 设置后，消息将会@群内所有人。
	 * @return 当前消息实例
	 */
	public AbstractDingTalkMessage atAll() {
		atAll = true;
		return this;
	}

	/**
	 * 添加需要@的人员手机号。 消息发送时将会@指定的手机号对应的群成员。
	 * @param phone 需要@的人员手机号
	 * @return 当前消息实例
	 */
	public AbstractDingTalkMessage addPhone(String phone) {
		atPhones.add(phone);
		return this;
	}

	/**
	 * 获取消息类型。 子类需要实现此方法以指定具体的消息类型。
	 * @return 消息类型枚举值
	 */
	public abstract MessageTypeEnum getType();

	/**
	 * 设置消息特定参数。 子类需要实现此方法以设置各自特定的消息参数。
	 * @param params 包含通用参数的参数对象
	 * @return 设置完成的参数对象
	 */
	public abstract DingTalkParams put(DingTalkParams params);

	@Override
	public String generate() {
		DingTalkParams params = put(new DingTalkParams().setType(getType().getVal())
				.setAt(new DingTalkParams.At().setAtAll(atAll).setAtMobiles(atPhones)));
		return params.toString();
	}

}
