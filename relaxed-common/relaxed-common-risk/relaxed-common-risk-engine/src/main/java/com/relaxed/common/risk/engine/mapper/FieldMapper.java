package com.relaxed.common.risk.engine.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.relaxed.common.risk.engine.model.entity.Field;

import com.relaxed.extend.mybatis.plus.mapper.ExtendMapper;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Yakir
 * @since 2021-08-29T12:14:38.328
 */
public interface FieldMapper extends ExtendMapper<Field> {

	/**
	 * 根据model id查询字段列表
	 * @author yakir
	 * @date 2021/8/29 12:53
	 * @param modelId
	 * @return java.util.List<com.relaxed.common.risk.engine.model.entity.Field>
	 */
	default List<Field> listByModelId(Long modelId) {
		return this.selectList(Wrappers.lambdaQuery(Field.class).eq(Field::getModelId, modelId));
	};

}
