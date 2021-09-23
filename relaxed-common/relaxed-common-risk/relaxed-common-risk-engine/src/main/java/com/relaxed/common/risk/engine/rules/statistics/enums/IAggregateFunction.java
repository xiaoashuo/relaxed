package com.relaxed.common.risk.engine.rules.statistics.enums;

/**
 * @author Yakir
 * @Topic AggregateFunction
 * @Description
 * @date 2021/8/30 14:52
 * @Version 1.0
 */
public interface IAggregateFunction {

	/**
	 * 聚合函数顺序
	 * @author yakir
	 * @date 2021/8/30 14:53
	 * @return java.lang.Integer
	 */
	Integer getOrder();

	/**
	 * 聚合函数
	 * @author yakir
	 * @date 2021/8/30 14:52
	 * @return java.lang.Integer
	 */
	String getFunction();

	/**
	 * 得到函数描述
	 * @author yakir
	 * @date 2021/8/30 14:52
	 * @return java.lang.String
	 */
	String getDesc();

}
