package com.relaxed.common.redis.distributor;

import com.relaxed.common.redis.distributor.subscribe.SubscribeHandle;

/**
 * EventDistributor 事件分发者 将动作分发出去 交由订阅者做后续处理
 *
 * @author Yakir
 */
public interface EventDistributor {

	/**
	 * 风控分发
	 * @param channel
	 * @param message
	 */
	void distribute(String channel, String message);

	/**
	 * 风控订阅者
	 * @param channel
	 * @param subscribeHandle
	 */
	void subscribe(String channel, SubscribeHandle subscribeHandle);

}
