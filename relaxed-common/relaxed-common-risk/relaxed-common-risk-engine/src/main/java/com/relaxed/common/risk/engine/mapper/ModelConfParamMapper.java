package com.relaxed.common.risk.engine.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.relaxed.common.risk.engine.model.entity.ModelConfParam;

import com.relaxed.extend.mybatis.plus.mapper.ExtendMapper;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Yakir
 * @since 2021-08-31T09:58:08.892
 */
public interface ModelConfParamMapper extends ExtendMapper<ModelConfParam> {

	/**
	 * 根据模型配置id 获取所有参数
	 * @author yakir
	 * @date 2021/8/31 10:28
	 * @param modelConfId
	 * @return java.util.List<com.relaxed.common.risk.engine.model.entity.ModelConfParam>
	 */
	default List<ModelConfParam> listByModelConfId(Long modelConfId) {
		return this.selectList(Wrappers.lambdaQuery(ModelConfParam.class).eq(ModelConfParam::getMoldId, modelConfId));
	}

}
