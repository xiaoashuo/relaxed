package com.relaxed.common.redis.distributor.subscribe;

/**
 * SubscribeHandle
 *
 * @author Yakir
 */
public interface SubscribeHandle {

	/**
	 * 当前订阅者类型
	 * @return com.relaxed.common.redis.distributor.subscribe.SubscribeType
	 */
	SubscribeType type();

	/**
	 * 消息订阅接收者
	 * @param channel 渠道
	 * @param message 消息
	 */
	void onMessage(String channel, String message);

}
