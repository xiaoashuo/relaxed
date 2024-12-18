package com.relaxed.common.log.biz.util;

import cn.hutool.core.util.NumberUtil;
import org.javers.core.diff.custom.CustomValueComparator;

import java.math.BigDecimal;

/**
 * @author Yakir
 * @Topic NumberValueComparator
 * @Description
 * @date 2024/4/29 18:09
 * @Version 1.0
 */
public class BigDecimalValueComparator implements CustomValueComparator<BigDecimal> {

	@Override
	public boolean equals(BigDecimal a, BigDecimal b) {
		return NumberUtil.equals(a, b);
	}

	@Override
	public String toString(BigDecimal value) {
		return value.stripTrailingZeros().toPlainString();
	}

}
