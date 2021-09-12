package com.relaxed.common.risk.biz.service;

import com.relaxed.common.model.domain.PageParam;
import com.relaxed.common.model.domain.PageResult;
import com.relaxed.common.risk.model.entity.Abstraction;
import com.relaxed.common.risk.model.qo.AbstractionQO;
import com.relaxed.common.risk.model.vo.AbstractionVO;
import com.relaxed.extend.mybatis.plus.service.ExtendService;

import java.util.List;

/**
 * <p>
 * 业务层
 * </p>
 *
 * @author Yakir
 * @since 2021-08-29T18:48:19.507
 */
public interface AbstractionService extends ExtendService<Abstraction> {

	/**
	 * 分页查询
	 * @param pageParam {@link PageParam}
	 * @param abstractionQO {@link AbstractionQO}
	 * @return {@link PageResult<AbstractionVO>}
	 */
	PageResult<AbstractionVO> selectByPage(PageParam pageParam, AbstractionQO abstractionQO);

	/**
	 * 根据模型id获取特征列表
	 * @author yakir
	 * @date 2021/8/29 18:59
	 * @param modelId
	 * @return java.util.List<com.relaxed.common.risk.model.vo.AbstractionVO>
	 */
	List<AbstractionVO> listByModelId(Long modelId);

}