package com.relaxed.extend.wechat.message;

import com.relaxed.extend.wechat.enums.MessageTypeEnum;
import com.relaxed.extend.wechat.request.WechatParams;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Yakir
 * @Topic AbstractWechatMessage
 * @Description 消息基础类
 * @date 2022/6/15 11:47
 * @Version 1.0
 */
public abstract class AbstractWechatMessage implements WechatMessage {

	/**
	 * 获取消息类型
	 * @return 返回消息类型
	 * @author lingting 2020-06-10 22:12:30
	 */
	public abstract MessageTypeEnum getType();

	/**
	 * 设置非公有属性
	 * @param params 已设置完公有参数的参数类
	 * @return 已设置完成的参数类
	 * @author lingting 2020-06-10 22:11:04
	 */
	public abstract WechatParams put(WechatParams params);

	@Override
	public String generate() {
		WechatParams params = put(new WechatParams().setType(getType().getVal()));
		return params.toString();
	}

}
