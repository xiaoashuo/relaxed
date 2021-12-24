package com.relaxed.common.exception.aop;

import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Yakir
 * @Topic ExceptionInterceptorHandler
 * @Description
 * @date 2021/12/24 17:38
 * @Version 1.0
 */
public interface ExceptionStrategy {

	/**
	 * 单线程 嵌套注解多次通知 true 开启 false 关闭 默认为false
	 * @author yakir
	 * @date 2021/12/24 17:40
	 * @return boolean
	 */
	default boolean nestedMulNotice() {
		return false;
	}

}
