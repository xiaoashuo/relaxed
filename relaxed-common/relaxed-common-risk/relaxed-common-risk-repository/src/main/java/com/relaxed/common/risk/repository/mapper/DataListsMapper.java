package com.relaxed.common.risk.repository.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.relaxed.common.risk.model.entity.DataLists;
import com.relaxed.extend.mybatis.plus.mapper.ExtendMapper;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Yakir
 * @since 2021-08-29T18:48:19.389
 */
public interface DataListsMapper extends ExtendMapper<DataLists> {

	/**
	 * 查询列表
	 * @author yakir
	 * @date 2021/8/31 16:27
	 * @param modelIds
	 * @param status
	 * @return java.util.List<com.relaxed.common.risk.model.entity.DataLists>
	 */
	default List<DataLists> list(List<Long> modelIds, Integer status) {
		return this.selectList(Wrappers.lambdaQuery(DataLists.class).eq(DataLists::getStatus, status)
				.in(DataLists::getModelId, modelIds));
	}

	/**
	 * 查询列表
	 * @author yakir
	 * @date 2021/8/31 16:37
	 * @param modelId
	 * @param status
	 * @return java.util.List<com.relaxed.common.risk.model.entity.DataLists>
	 */
	default List<DataLists> list(Long modelId, Integer status) {
		return this.selectList(Wrappers.lambdaQuery(DataLists.class).eq(DataLists::getStatus, status)
				.eq(DataLists::getModelId, modelId));
	}

}
