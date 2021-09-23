package com.relaxed.common.risk.engine.service;

import com.relaxed.common.risk.model.vo.FieldVO;
import com.relaxed.common.risk.model.vo.PreItemVO;

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

	/**
	 * 得到预处理项列表
	 * @author yakir
	 * @date 2021/9/23 14:16
	 * @param modelId
	 * @return java.util.List<com.relaxed.common.risk.model.vo.PreItemVO>
	 */
	List<PreItemVO> getPreItems(Long modelId);

	/**
	 * 获取字段类型map
	 * @author yakir
	 * @date 2021/9/23 14:18
	 * @param modelId
	 * @return java.util.Map<java.lang.String,java.lang.String> eg: k-> username v->STRING
	 */
	Map<String, String> getFieldTypeMap(Long modelId);

}
