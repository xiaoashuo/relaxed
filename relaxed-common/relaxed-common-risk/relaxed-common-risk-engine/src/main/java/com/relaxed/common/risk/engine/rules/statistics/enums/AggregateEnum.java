package com.relaxed.common.risk.engine.rules.statistics.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Yakir
 * @Topic AggregateEnum
 * @Description
 * @date 2021/8/30 14:53
 * @Version 1.0
 */
@Getter
@RequiredArgsConstructor
public enum AggregateEnum implements IAggregateFunction {

	/**
	 * 统计函数
	 */
	COUNT(1, "COUNT", "统计函数"),
	/**
	 * 去重统计函数
	 */
	DISTINCT_COUNT(2, "DISTINCT_COUNT", "去重统计函数"),
	/**
	 * 求和函数
	 */
	SUM(3, "SUM", "求和函数"),
	/**
	 * 平均数函数
	 */
	AVERAGE(4, "AVERAGE", "平均数函数"),
	/**
	 * 最大值函数
	 */
	MAX(5, "MAX", "最大值函数"),
	/**
	 * 最小值函数
	 */
	MIN(6, "MIN", "最小值函数"),
	/**
	 * 标准差函数
	 */
	SD(7, "SD", "标准差函数"),
	/**
	 * 方差函数
	 */
	VARIANCE(8, "VARIANCE", "方差函数"),
	/**
	 * 偏离率函数
	 */
	DEVIATION(9, "DEVIATION", "偏离率函数"),
	/**
	 * 中位数函数
	 */
	MEDIAN(10, "MEDIAN", "中位数函数"),;

	private final Integer order;

	private final String function;

	private final String desc;

	public static IAggregateFunction of(Integer order) {
		for (AggregateEnum value : values()) {
			if (value.getOrder().equals(order)) {
				return value;
			}
		}
		return null;
	}

}
