package com.relaxed.common.risk.engine.service;

import com.relaxed.common.risk.engine.model.dto.RuleDTO;
import com.relaxed.common.risk.engine.model.qo.RuleQO;
import com.relaxed.common.risk.engine.model.vo.ActivationVO;
import com.relaxed.common.risk.engine.model.vo.RuleVO;
import com.relaxed.common.risk.engine.model.entity.Rule;
import com.relaxed.extend.mybatis.plus.service.ExtendService;

import com.relaxed.common.model.domain.PageParam;
import com.relaxed.common.model.domain.PageResult;

import java.util.List;

/**
 * <p>
 * 业务层
 * </p>
 *
 * @author Yakir
 * @since 2021-08-31T11:30:23.273
 */
public interface RuleService extends ExtendService<Rule> {

	/**
	 * 分页查询
	 * @param pageParam {@link PageParam}
	 * @param ruleQO {@link RuleQO}
	 * @return {@link PageResult<RuleVO>}
	 */
	PageResult<RuleVO> selectByPage(PageParam pageParam, RuleQO ruleQO);

	/**
	 * 查询列表
	 * @author yakir
	 * @date 2021/8/31 11:46
	 * @param activationId
	 * @return java.util.List<com.relaxed.common.risk.engine.model.vo.RuleVO>
	 */
	List<RuleVO> listByActivationId(Long activationId);

}