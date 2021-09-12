package com.relaxed.common.risk.engine.service;

import com.relaxed.common.risk.model.vo.PreItemVO;

import java.util.List;
import java.util.Map;

/**
 * @author Yakir
 * @Topic PreItemManageService
 * @Description
 * @date 2021/8/29 14:01
 * @Version 1.0
 */
public interface PreItemManageService {

	/**
	 * 预处理参数转换
	 * @param modelId 模型id
	 * @param jsonInfo 上下文参数信息
	 * @return 预处理后字段map
	 */
	Map<String, Object> prepare(Long modelId, Map<String, Object> jsonInfo);

	/**
	 * 查询模型 预处理参数 根据模型id
	 * @author yakir
	 * @date 2021/8/29 14:08
	 * @param modelId
	 * @return java.util.List<com.relaxed.common.risk.model.vo.PreItemVO>
	 */
	List<PreItemVO> getByModelId(Long modelId);

}
