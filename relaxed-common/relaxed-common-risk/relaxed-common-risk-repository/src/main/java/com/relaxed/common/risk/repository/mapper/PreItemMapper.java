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

}
