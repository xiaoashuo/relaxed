package com.relaxed.common.risk.repository.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.relaxed.common.risk.model.entity.PreItem;
import com.relaxed.extend.mybatis.plus.mapper.ExtendMapper;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Yakir
 * @since 2021-08-29T13:57:50.664
 */
public interface PreItemMapper extends ExtendMapper<PreItem> {

	/**
	 * 根据模型id查询预处理项
	 * @author yakir
	 * @date 2021/8/29 14:16
	 * @param modelId
	 * @return java.util.List<com.relaxed.common.risk.model.entity.PreItem>
	 */
	default List<PreItem> getByModelId(Long modelId) {
		return this.selectList(Wrappers.lambdaQuery(PreItem.class).eq(PreItem::getModelId, modelId));
	}

	/**
	 * 查询预处理字段 根据模型id 与 目标字段描述
	 * @author yakir
	 * @date 2021/9/12 17:32
	 * @param modelId
	 * @param destField
	 * @return com.relaxed.common.risk.model.entity.PreItem
	 */
	default PreItem selectOne(Long modelId, String destField) {
		return this.selectOne(Wrappers.lambdaQuery(PreItem.class).eq(PreItem::getModelId, modelId)
				.eq(PreItem::getDestField, destField));
	}

	/**
	 * 查询预处理字段 根据model id 与id
	 * @author yakir
	 * @date 2021/9/12 18:09
	 * @param modelId
	 * @param id
	 * @return com.relaxed.common.risk.model.entity.PreItem
	 */
	default PreItem selectOne(Long modelId, Long id) {
		return this
				.selectOne(Wrappers.lambdaQuery(PreItem.class).eq(PreItem::getModelId, modelId).eq(PreItem::getId, id));
	}

	/**
	 * 查询一个根据model id
	 * @author yakir
	 * @date 2021/9/12 17:59
	 * @param modelId
	 * @return com.relaxed.common.risk.model.entity.PreItem
	 */
	default PreItem selectOne(Long modelId) {
		return this.selectOne(Wrappers.lambdaQuery(PreItem.class).eq(PreItem::getModelId, modelId));
	}

}
