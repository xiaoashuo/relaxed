package com.relaxed.common.risk.engine.service;

import com.relaxed.common.risk.model.vo.FieldVO;

import java.util.List;
import java.util.Map;

/**
 * @author Yakir
 * @Topic FieldManageService
 * @Description
 * @date 2021/8/29 12:19
 * @Version 1.0
 */
public interface FieldManageService {

	/**
	 * 获取字段vo
	 * @author yakir
	 * @date 2021/8/29 13:00
	 * @param modelId
	 * @return java.util.List<com.relaxed.common.risk.model.vo.FieldVO>
	 */
	List<FieldVO> getFieldVos(Long modelId);

}
