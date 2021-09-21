package com.relaxed.common.risk.repository.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.relaxed.common.risk.model.entity.Field;
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
	 * @return java.util.List<com.relaxed.common.risk.model.entity.Field>
	 */
	default List<Field> listByModelId(Long modelId) {
		return this.selectList(Wrappers.lambdaQuery(Field.class).eq(Field::getModelId, modelId));
	};

	/**
	 * 查询单字段
	 * @author yakir
	 * @date 2021/9/19 17:24
	 * @param modelId
	 * @param fieldName
	 * @return com.relaxed.common.risk.model.entity.Field
	 */
    default Field selectOne(Long modelId, String fieldName){
    	return this.selectOne(Wrappers.lambdaQuery(Field.class)
		.eq(Field::getModelId,modelId)
		.eq(Field::getFieldName,fieldName));
	}

}
