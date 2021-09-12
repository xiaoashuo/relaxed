package com.relaxed.common.risk.repository.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.relaxed.common.risk.model.entity.Model;
import com.relaxed.extend.mybatis.plus.mapper.ExtendMapper;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Yakir
 * @since 2021-08-29T09:04:20.388
 */
public interface ModelMapper extends ExtendMapper<Model> {

	/**
	 * 根据状态查询列表
	 * @author yakir
	 * @date 2021/8/29 10:15
	 * @param status
	 * @return java.util.List<com.relaxed.common.risk.model.entity.Model>
	 */
	default List<Model> listByStatus(Integer status) {
		return this.selectList(Wrappers.lambdaQuery(Model.class).eq(Model::getStatus, status));
	}

	/**
	 * 根据guid查询model
	 * @author yakir
	 * @date 2021/8/29 10:39
	 * @param guid
	 * @return com.relaxed.common.risk.model.entity.Model
	 */
	default Model getByGuid(String guid) {
		return this.selectOne(Wrappers.lambdaQuery(Model.class).eq(Model::getGuid, guid));
	}

	/**
	 * 根据模型名称查询
	 * @author yakir
	 * @date 2021/9/12 17:05
	 * @param modelName
	 * @return com.relaxed.common.risk.model.entity.Model
	 */
	default Model getByModelName(String modelName){
		return this.selectOne(Wrappers.lambdaQuery(Model.class).eq(Model::getModelName, modelName));

	}

}
