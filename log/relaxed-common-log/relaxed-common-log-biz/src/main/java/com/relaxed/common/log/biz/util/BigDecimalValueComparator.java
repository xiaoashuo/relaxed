package com.relaxed.common.log.biz.util;

import org.javers.core.diff.custom.CustomValueComparator;
import org.javers.core.metamodel.object.ValueObjectId;

import java.math.BigDecimal;

/**
 * BigDecimal 值比较器 该比较器用于在对象比较时处理 BigDecimal 类型的值 主要功能包括： 1. 比较两个 BigDecimal 值是否相等 2. 处理
 * null 值的情况 3. 支持自定义比较逻辑
 *
 * @author Yakir
 */
public class BigDecimalValueComparator implements CustomValueComparator<BigDecimal> {

	/**
	 * 比较两个 BigDecimal 值是否相等 如果两个值都为 null，则认为相等 如果只有一个值为 null，则认为不相等 否则使用 compareTo 方法比较
	 * @param left 第一个值
	 * @param right 第二个值
	 * @return 如果相等返回 true，否则返回 false
	 */
	@Override
	public boolean equals(BigDecimal left, BigDecimal right) {
		if (left == null && right == null) {
			return true;
		}
		if (left == null || right == null) {
			return false;
		}
		return left.compareTo(right) == 0;
	}

	/**
	 * 获取值的字符串表示 用于在比较结果中显示
	 * @param value 要格式化的值
	 * @return 格式化后的字符串
	 */
	@Override
	public String toString(BigDecimal value) {
		return value == null ? "" : value.toString();
	}

}
