package com.relaxed.common.risk.engine.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;

import java.math.BigDecimal;

/**
 * @author Yakir
 * @Topic ScoreUtil
 * @Description
 * @date 2021/8/31 12:09
 * @Version 1.0
 */
@RequiredArgsConstructor
@Getter
public enum ScoreUtil {

	/**
	 * 加法
	 */
	ADD("+") {
		@Override
		BigDecimal compute(BigDecimal x, BigDecimal y) {
			return x.add(y);
		}
	},
	/**
	 * 减法
	 */
	SUB("-") {
		@Override
		BigDecimal compute(BigDecimal x, BigDecimal y) {
			return x.subtract(y);
		}
	},
	/**
	 * 乘法
	 */
	MUL("*") {
		@Override
		BigDecimal compute(BigDecimal x, BigDecimal y) {
			return x.multiply(y);
		}
	},
	/**
	 * 除法
	 */
	DIV("/") {
		@Override
		BigDecimal compute(BigDecimal x, BigDecimal y) {
			return x.divide(y).setScale(2, 4);
		}
	},
	NONE("none") {
		@Override
		BigDecimal compute(BigDecimal x, BigDecimal y) {
			return null;
		}
	},;

	/**
	 * 标记
	 */
	private final String symbol;

	/**
	 * 计算函数
	 * @param x
	 * @param y
	 * @return
	 */
	abstract BigDecimal compute(BigDecimal x, BigDecimal y);

	/**
	 * 执行分数计算操作
	 * @author yakir
	 * @date 2021/8/31 14:20
	 * @param option
	 * @param x
	 * @param y
	 * @return java.math.BigDecimal
	 */
	public static BigDecimal exec(String option, BigDecimal x, BigDecimal y) {
		ScoreUtil scoreUtil = ScoreUtil.valueOf(option);
		Assert.notNull(scoreUtil, "score util way can not be null.");
		return scoreUtil.compute(x, y);
	}

}
