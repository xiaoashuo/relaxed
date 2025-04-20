package com.relaxed.common.log.biz.service.impl;

import com.relaxed.common.log.biz.service.IOperatorGetService;

/**
 * 默认操作者获取服务实现类 该实现类提供了获取操作者信息的基本实现 默认返回 null，需要根据具体业务场景进行扩展
 *
 * @author Yakir
 */
public class DefaultOperatorGetServiceImpl implements IOperatorGetService {

	@Override
	public String getOperator() {
		return null;
	}

}
