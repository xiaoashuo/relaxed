package com.relaxed.common.risk.engine.core.distributor.subscribe;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Yakir
 * @Topic SubscribeEnum
 * @Description
 * @date 2021/8/29 9:56
 * @Version 1.0
 */
@RequiredArgsConstructor
@Getter
public enum SubscribeEnum {

	/**
	 * model 订阅发布
	 */
	PUB_SUB_MODEL_CHANNEL("relaxed:channel:model", "发布订阅model"),
	/**
	 * field 订阅发布
	 */
	PUB_SUB_FIELD_CHANNEL("relaxed:channel:field", "发布订阅field"),
	/**
	 * 预处理项
	 */
	PUB_SUB_PRE_ITEM_CHANNEL("relaxed:channel:pre:item", "发布订阅预处理项"),
	/**
	 * 特征处理
	 */
	PUB_SUB_ABSTRACTION_CHANNEL("relaxed:channel:abstraction", "发布订阅特征处理"),

	;

	private final String channel;

	private final String desc;

}
