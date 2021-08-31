package com.relaxed.common.risk.engine.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.relaxed.common.risk.engine.model.entity.Activation;
import com.relaxed.common.risk.engine.model.entity.Rule;

import com.relaxed.extend.mybatis.plus.mapper.ExtendMapper;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Yakir
 * @since 2021-08-31T11:30:23.273
 */
public interface RuleMapper extends ExtendMapper<Rule> {

	/**
	 * 查询列表
	 * @author yakir
	 * @date 2021/8/31 11:47
	 * @param activationId
	 * @return java.util.List<com.relaxed.common.risk.engine.model.entity.Rule>
	 */
	default List<Rule> listByActivationId(Long activationId) {
		return this.selectList(Wrappers.lambdaQuery(Rule.class).eq(Rule::getActivationId, activationId));
	}

}
