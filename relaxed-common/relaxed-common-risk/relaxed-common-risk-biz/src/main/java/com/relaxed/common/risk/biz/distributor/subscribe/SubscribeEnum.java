package com.relaxed.common.risk.biz.distributor.subscribe;

import com.relaxed.common.risk.biz.distributor.subscribe.SubscribeType;
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
public enum SubscribeEnum implements SubscribeType {

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
	/**
	 * 规则处理
	 */
	PUB_SUB_RULE_CHANNEL("relaxed:channel:rule", "发布订阅规则"),
	/**
	 * 数据列表channel
	 */
	PUB_SUB_DATALIST_CHANNEL("relaxed:channel:data:list", "发布订阅数据列表"),
	// /**
	// * 黑名单数据列表
	// */
	// PUB_SUB_DATA_LIST_CHANNEL("relaxed:channel:data:list", "发布订阅数据列表"),
	// /**
	// * 黑名单数据元数据
	// */
	// PUB_SUB_DATA_LIST_META_CHANNEL("relaxed:channel:data:list:meta", "发布订阅数据列表元数据"),
	// /**
	// * 黑名单数据列表记录
	// */
	// PUB_SUB_DATA_LIST_RECORD_CHANNEL("relaxed:channel:data:list:record", "发布订阅数据列表记录"),

	;

	private final String channel;

	private final String desc;

}
