package com.relaxed.common.risk.engine.manage;

/**
 * @author Yakir
 * @Topic ModelMongoManageService
 * @Description
 * @date 2021/8/29 16:14
 * @Version 1.0
 */
public interface ModelEventManageService {

	/**
	 * 存储事件模型
	 * @author yakir
	 * @date 2021/8/29 16:17
	 * @param modelId 模型id
	 * @param jsonString 参数信息
	 * @param attachJson 预处理参数信息
	 * @param isAllowDuplicate 是否覆盖
	 * @return boolean
	 */
	boolean save(Long modelId, String jsonString, String attachJson, boolean isAllowDuplicate);

}
