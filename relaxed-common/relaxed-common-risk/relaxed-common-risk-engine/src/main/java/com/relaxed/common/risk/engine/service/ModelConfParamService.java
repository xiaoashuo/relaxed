package com.relaxed.common.risk.engine.service;

import com.relaxed.common.risk.engine.model.dto.ModelConfParamDTO;
import com.relaxed.common.risk.engine.model.qo.ModelConfParamQO;
import com.relaxed.common.risk.engine.model.vo.ModelConfParamVO;
import com.relaxed.common.risk.engine.model.entity.ModelConfParam;
import com.relaxed.extend.mybatis.plus.service.ExtendService;

import com.relaxed.common.model.domain.PageParam;
import com.relaxed.common.model.domain.PageResult;

import java.util.List;

/**
 * <p>
 * 业务层
 * </p>
 *
 * @author Yakir
 * @since 2021-08-31T09:58:08.892
 */
public interface ModelConfParamService extends ExtendService<ModelConfParam> {

	/**
	 * 分页查询
	 * @param pageParam {@link PageParam}
	 * @param modelConfParamQO {@link ModelConfParamQO}
	 * @return {@link PageResult<ModelConfParamVO>}
	 */
	PageResult<ModelConfParamVO> selectByPage(PageParam pageParam, ModelConfParamQO modelConfParamQO);

	/**
	 * 根据model conf id 查询参数列表
	 * @author yakir
	 * @date 2021/8/31 10:27
	 * @param modelConfId
	 * @return java.util.List<com.relaxed.common.risk.engine.model.vo.ModelConfParamVO>
	 */
	List<ModelConfParamVO> listByModelConfId(Long modelConfId);

}