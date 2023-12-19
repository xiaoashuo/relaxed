package com.relaxed.common.log.operation.aspect;

import com.relaxed.common.log.operation.service.IOperatorGetService;
import org.springframework.expression.spel.ast.Operator;

import java.util.Optional;

/**
 * @author Yakir
 * @Topic DefaultOperatorGetServiceImpl
 * @Description
 * @date 2023/12/14 11:29
 * @Version 1.0
 */
public class DefaultOperatorGetServiceImpl implements IOperatorGetService {

//	@Override
//	public Operator getUser() {
//		// UserUtils 是获取用户上下文的方法
//		// return Optional.ofNullable(UserUtils.getUser())
//		// .map(a -> new Operator(a.getName(), a.getLogin()))
//		// .orElseThrow(()->new IllegalArgumentException("user is null"));
//
//		return null;
//	}


	@Override
	public String getOperatorId() {
		return null;
	}
}
