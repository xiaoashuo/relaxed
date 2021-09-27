package com.relaxed.common.risk.biz.service;

import com.relaxed.common.model.domain.PageParam;
import com.relaxed.common.model.domain.PageResult;
import com.relaxed.common.risk.model.entity.Activation;
import com.relaxed.common.risk.model.qo.ActivationQO;
import com.relaxed.common.risk.model.vo.ActivationVO;
import com.relaxed.extend.mybatis.plus.service.ExtendService;

import java.util.List;

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

	/**
	 * 根据modelID查询
	 * @author yakir
	 * @date 2021/8/31 10:51
	 * @param modelId
	 * @return java.util.List<com.relaxed.common.risk.model.vo.ActivationVO>
	 */
	List<ActivationVO> listByModelId(Long modelId);

}