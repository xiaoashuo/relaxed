package com.relaxed.test.cache.redis.subscribe;

import com.relaxed.common.redis.distributor.subscribe.SubscribeType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * SubscribeEnum
 *
 * @author Yakir
 */
@RequiredArgsConstructor
@Getter
public enum SubscribeEnum implements SubscribeType {

	/**
	 * 账单填账 订阅发布
	 */
	PUB_SUB_TRADE_FILL("relaxed:trade:bill-fill", "发布订阅账单填账"),

	;

	private final String channel;

	private final String desc;

}
