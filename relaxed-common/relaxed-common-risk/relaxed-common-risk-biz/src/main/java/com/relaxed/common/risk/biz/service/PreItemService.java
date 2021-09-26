package com.relaxed.common.risk.biz.service;

import com.relaxed.common.model.domain.PageParam;
import com.relaxed.common.model.domain.PageResult;
import com.relaxed.common.risk.model.entity.PreItem;
import com.relaxed.common.risk.model.qo.PreItemQO;
import com.relaxed.common.risk.model.vo.PreItemVO;
import com.relaxed.extend.mybatis.plus.service.ExtendService;

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
	 * @return java.util.List<com.relaxed.common.risk.model.vo.PreItemVO>
	 */
	List<PreItemVO> listByModelId(Long modelId);

	/**
	 * 查询一条数据
	 * @author yakir
	 * @date 2021/9/26 13:39
	 * @param modelId
	 * @param destField
	 * @return com.relaxed.common.risk.model.entity.PreItem
	 */
	PreItem getOne(Long modelId, String destField);

	/**
	 * 查询一条数据
	 * @author yakir
	 * @date 2021/9/26 13:41
	 * @param modelId
	 * @param preItemId
	 * @return com.relaxed.common.risk.model.entity.PreItem
	 */
	PreItem getOne(Long modelId, Long preItemId);

}