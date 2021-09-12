package com.relaxed.common.risk.biz.service;

import com.relaxed.common.model.domain.PageParam;
import com.relaxed.common.model.domain.PageResult;
import com.relaxed.common.risk.model.entity.ModelConf;
import com.relaxed.common.risk.model.qo.ModelConfQO;
import com.relaxed.common.risk.model.vo.ModelConfVO;
import com.relaxed.extend.mybatis.plus.service.ExtendService;

/**
 * <p>
 * 业务层
 * </p>
 *
 * @author Yakir
 * @since 2021-08-31T09:58:08.656
 */
public interface ModelConfService extends ExtendService<ModelConf> {

	/**
	 * 分页查询
	 * @param pageParam {@link PageParam}
	 * @param modelConfQO {@link ModelConfQO}
	 * @return {@link PageResult<ModelConfVO>}
	 */
	PageResult<ModelConfVO> selectByPage(PageParam pageParam, ModelConfQO modelConfQO);

	/**
	 * 根据模型id获取模型配置
	 * @author yakir
	 * @date 2021/8/31 10:03
	 * @param modelId
	 * @return com.relaxed.common.risk.model.vo.ModelConfVO
	 */
	ModelConfVO getByModelId(Long modelId);

}