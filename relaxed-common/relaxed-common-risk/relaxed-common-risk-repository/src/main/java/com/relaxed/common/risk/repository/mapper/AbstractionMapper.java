package com.relaxed.common.risk.repository.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.relaxed.common.risk.model.entity.Abstraction;
import com.relaxed.extend.mybatis.plus.mapper.ExtendMapper;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Yakir
 * @since 2021-08-29T18:48:19.507
 */
public interface AbstractionMapper extends ExtendMapper<Abstraction> {

	/**
	 * 根据模型id获取特征
	 * @author yakir
	 * @date 2021/8/29 18:59
	 * @param modelId
	 * @return java.util.List<com.relaxed.common.risk.model.entity.Abstraction>
	 */
	default List<Abstraction> listByModelId(Long modelId) {
		return this.selectList(Wrappers.lambdaQuery(Abstraction.class).eq(Abstraction::getModelId, modelId));
	}

}
