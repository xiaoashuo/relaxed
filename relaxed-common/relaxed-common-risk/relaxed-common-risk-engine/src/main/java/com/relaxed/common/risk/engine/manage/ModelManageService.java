package com.relaxed.common.risk.engine.manage;

import com.relaxed.common.risk.engine.model.vo.ModelVO;

import java.util.Collection;
import java.util.Map;

/**
 * @author Yakir
 * @Topic ModelManageService
 * @Description
 * @date 2021/8/29 10:48
 * @Version 1.0
 */
public interface ModelManageService {

	/**
	 * 根据id查询
	 * @author yakir
	 * @date 2021/8/29 10:23
	 * @param id
	 * @return com.relaxed.common.risk.engine.model.vo.ModelVO
	 */
	ModelVO getById(Long id);

	/**
	 * 根据guid 获取model
	 * @author yakir
	 * @date 2021/8/29 10:32
	 * @param guid
	 * @return com.relaxed.common.risk.engine.model.vo.ModelVO
	 */
	ModelVO getByGuid(String guid);

	/**
	 * 根据状态查询
	 * @author yakir
	 * @date 2021/9/6 18:31
	 * @param status
	 * @return java.util.Map<java.lang.String,java.lang.Long>
	 */
	Map<String, Long> listByStatus(Integer status);

}
