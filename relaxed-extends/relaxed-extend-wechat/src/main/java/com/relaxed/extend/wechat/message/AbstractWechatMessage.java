package com.relaxed.extend.wechat.message;

import com.relaxed.extend.wechat.enums.MessageTypeEnum;
import com.relaxed.extend.wechat.request.WechatParams;

import java.util.HashSet;
import java.util.Set;

/**
 * 企业微信消息抽象基类。 提供了消息生成的基本实现，具体的消息类型需要继承此类并实现抽象方法。
 *
 * @author Yakir
 * @since 1.0
 */
public abstract class AbstractWechatMessage implements WechatMessage {

	/**
	 * 获取消息类型
	 * @return 消息类型枚举
	 */
	public abstract MessageTypeEnum getType();

	/**
	 * 设置消息参数
	 * @param params 已设置完基本参数的参数对象
	 * @return 设置完成后的参数对象
	 */
	public abstract WechatParams put(WechatParams params);

	@Override
	public String generate() {
		WechatParams params = put(new WechatParams().setType(getType().getVal()));
		return params.toString();
	}

}
