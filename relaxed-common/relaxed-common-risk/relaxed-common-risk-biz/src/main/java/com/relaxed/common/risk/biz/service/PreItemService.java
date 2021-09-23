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
	 * 添加预处理字段
	 * @author yakir
	 * @date 2021/9/12 17:31
	 * @param preItem
	 * @return boolean
	 */
	boolean add(PreItem preItem);

	/**
	 * 删除预处理项
	 * @author yakir
	 * @date 2021/9/12 17:54
	 * @param modelId
	 * @param id
	 * @return boolean
	 */
	boolean del(Long modelId, Long id);

	/**
	 * 编辑预处理项
	 * @author yakir
	 * @date 2021/9/12 17:56
	 * @param preItem
	 * @return boolean
	 */
	boolean edit(PreItem preItem);

}