package com.relaxed.common.risk.engine.service;

import java.util.Map;

/**
 * @author Yakir
 * @Topic FieldValidateService
 * @Description
 * @date 2021/8/29 13:01
 * @Version 1.0
 */
public interface FieldValidateService {

	/**
	 * 验证字段
	 * @author yakir
	 * @date 2021/8/29 12:21
	 * @param modelId 模型id
	 * @param data 事件数据
	 * @return java.util.Map<java.lang.String,java.lang.String>
	 */
	Map<String, String> validate(Long modelId, Map data);

}
