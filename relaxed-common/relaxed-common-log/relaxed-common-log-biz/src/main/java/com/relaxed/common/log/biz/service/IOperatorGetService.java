package com.relaxed.common.log.biz.service;

import org.springframework.expression.spel.ast.Operator;

/**
 * @author Yakir
 * @Topic IOperatorGetService
 * @Description
 * @date 2023/12/14 11:29
 * @Version 1.0
 */
public interface IOperatorGetService {

	/**
	 * 获取操作者信息
	 * @return
	 */
	String getOperatorId();

}
