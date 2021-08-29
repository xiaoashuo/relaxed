package com.relaxed.common.risk.engine.service;

import com.relaxed.common.risk.engine.model.dto.ActivationDTO;
import com.relaxed.common.risk.engine.model.vo.ActivationVO;
import com.relaxed.common.risk.engine.model.entity.Activation;
import com.relaxed.extend.mybatis.plus.service.ExtendService;

import com.relaxed.common.model.domain.PageParam;
import com.relaxed.common.model.domain.PageResult;

/**
 * <p>
 * 业务层
 * </p>
 *
 * @author Yakir
 * @since 2021-08-29T18:48:19.435
 */
public interface ActivationService extends ExtendService<Activation> {

	/**
	 * 分页查询
	 * @param pageParam {@link PageParam}
	 * @param activationQO {@link ActivationQO}
	 * @return {@link PageResult<ActivationVO>}
	 */
	PageResult<ActivationVO> selectByPage(PageParam pageParam, ActivationQO activationQO);

}