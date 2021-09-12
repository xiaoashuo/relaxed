package com.relaxed.common.risk.engine.service;

import com.relaxed.common.risk.model.vo.ModelVO;

import java.util.List;
import java.util.Map;

/**
 * @author Yakir
 * @Topic DataListManageService
 * @Description
 * @date 2021/8/30 11:19
 * @Version 1.0
 */
public interface DataListManageService {

	/**
	 * 构建数据列表map
	 * @author yakir
	 * @date 2021/9/7 10:01
	 * @param modelId
	 * @param dataListMap
	 */
	void buildDataListMap(Long modelId, Map<String, Object> dataListMap);

	/**
	 * 得到数据列表记录map
	 * @author yakir
	 * @date 2021/8/31 17:10
	 * @param modelId
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 */
	Map<String, Object> getDataListMap(Long modelId);

	/**
	 * 根据状态展现数据列表
	 * @author yakir
	 * @date 2021/9/7 10:00
	 * @param status
	 * @return java.util.List<com.relaxed.common.risk.model.vo.ModelVO>
	 */
	List<ModelVO> listByStatus(Integer status);

}
