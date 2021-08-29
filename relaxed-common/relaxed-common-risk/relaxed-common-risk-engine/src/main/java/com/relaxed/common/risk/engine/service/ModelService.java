package com.relaxed.common.risk.engine.service;

import com.relaxed.common.model.domain.PageParam;
import com.relaxed.common.model.domain.PageResult;
import com.relaxed.common.risk.engine.model.entity.Model;
import com.relaxed.common.risk.engine.model.qo.ModelQO;
import com.relaxed.common.risk.engine.model.vo.ModelVO;

import java.util.List;

/**
 * @author Yakir
 * @Topic ModelService
 * @Description
 * @date 2021/8/29 8:50
 * @Version 1.0
 */
public interface ModelService {

	/**
	 * 查询列表
	 * @author yakir
	 * @date 2021/8/29 9:10
	 * @param pageParam
	 * @param modelQO
	 * @return com.relaxed.common.model.domain.PageResult<com.relaxed.common.risk.engine.model.vo.ModelVO>
	 */
	PageResult<ModelVO> selectByPage(PageParam pageParam, ModelQO modelQO);

	/**
	 * 根据状态查询列表
	 * @author yakir
	 * @date 2021/8/29 10:14
	 * @param status
	 * @return java.util.List<com.relaxed.common.risk.engine.model.vo.ModelVO>
	 */
	List<ModelVO> listByStatus(Integer status);

	/**
	 * 根据guid查询model
	 * @author yakir
	 * @date 2021/8/29 10:52
	 * @param guid
	 * @return com.relaxed.common.risk.engine.model.vo.ModelVO
	 */
	ModelVO getByGuid(String guid);

}
