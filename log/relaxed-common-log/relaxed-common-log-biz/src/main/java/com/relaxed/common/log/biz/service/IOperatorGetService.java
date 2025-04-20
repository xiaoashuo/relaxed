package com.relaxed.common.log.biz.service;

/**
 * 操作者获取服务接口，用于获取当前操作的用户信息 该接口定义了获取操作者信息的基本规范，实现类需要提供具体的获取逻辑 支持从当前会话、安全上下文或其他来源获取操作者信息
 *
 * @author Yakir
 */
public interface IOperatorGetService {

	/**
	 * 获取当前操作者的标识信息 该方法用于在记录业务操作日志时获取执行操作的用户信息
	 * @return 操作者标识，通常是用户ID、用户名或其他唯一标识
	 */
	String getOperator();

}
