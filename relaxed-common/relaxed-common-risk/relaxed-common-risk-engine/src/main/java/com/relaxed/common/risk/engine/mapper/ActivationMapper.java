package com.relaxed.common.risk.engine.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.relaxed.common.risk.engine.model.entity.Activation;

import com.relaxed.extend.mybatis.plus.mapper.ExtendMapper;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Yakir
 * @since 2021-08-29T18:48:19.435
 */
public interface ActivationMapper extends ExtendMapper<Activation> {

	/**
	 * 查询列表
	 * @author yakir
	 * @date 2021/8/31 11:00
	 * @param modelId
	 * @return java.util.List<com.relaxed.common.risk.engine.model.entity.Activation>
	 */
	default List<Activation> listByModelId(Long modelId) {
		return this.selectList(Wrappers.lambdaQuery(Activation.class).eq(Activation::getModelId, modelId));
	}

}
