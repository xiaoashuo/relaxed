package com.relaxed.common.risk.engine.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.relaxed.common.risk.engine.model.entity.ModelConf;

import com.relaxed.extend.mybatis.plus.mapper.ExtendMapper;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Yakir
 * @since 2021-08-31T09:58:08.656
 */
public interface ModelConfMapper extends ExtendMapper<ModelConf> {

	/**
	 * 根据模型id获取模型配置
	 * @author yakir
	 * @date 2021/8/31 10:04
	 * @param modelId
	 * @return com.relaxed.common.risk.engine.model.entity.ModelConf
	 */
	default ModelConf getByModelId(Long modelId) {
		return this.selectOne(Wrappers.lambdaQuery(ModelConf.class).eq(ModelConf::getModelId, modelId));
	}

}
