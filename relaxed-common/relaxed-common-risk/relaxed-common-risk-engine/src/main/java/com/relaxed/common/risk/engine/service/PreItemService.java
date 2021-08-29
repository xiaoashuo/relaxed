package com.relaxed.common.risk.engine.service;

import com.relaxed.common.risk.engine.model.dto.PreItemDTO;
import com.relaxed.common.risk.engine.model.qo.PreItemQO;
import com.relaxed.common.risk.engine.model.vo.PreItemVO;
import com.relaxed.common.risk.engine.model.entity.PreItem;
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
 * @since 2021-08-29T13:57:50.664
 */
public interface PreItemService extends ExtendService<PreItem> {

	/**
	 * 分页查询
	 * @param pageParam {@link PageParam}
	 * @param preItemQO {@link PreItemQO}
	 * @return {@link PageResult<PreItemVO>}
	 */
	PageResult<PreItemVO> selectByPage(PageParam pageParam, PreItemQO preItemQO);

	/**
	 * 根据模型id查询预处理项
	 * @author yakir
	 * @date 2021/8/29 14:15
	 * @param modelId
	 * @return java.util.List<com.relaxed.common.risk.engine.model.vo.PreItemVO>
	 */
	List<PreItemVO> getByModelId(Long modelId);

}