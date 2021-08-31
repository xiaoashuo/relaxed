package com.relaxed.common.risk.engine.service;

import com.relaxed.common.risk.engine.model.dto.RuleHistoryDTO;
import com.relaxed.common.risk.engine.model.qo.RuleHistoryQO;
import com.relaxed.common.risk.engine.model.vo.RuleHistoryVO;
import com.relaxed.common.risk.engine.model.entity.RuleHistory;
import com.relaxed.extend.mybatis.plus.service.ExtendService;

import com.relaxed.common.model.domain.PageParam;
import com.relaxed.common.model.domain.PageResult;

/**
 * <p>
 * 业务层
 * </p>
 *
 * @author Yakir
 * @since 2021-08-31T11:30:23.317
 */
public interface RuleHistoryService extends ExtendService<RuleHistory> {

	/**
	 * 分页查询
	 * @param pageParam {@link PageParam}
	 * @param ruleHistoryQO {@link RuleHistoryQO}
	 * @return {@link PageResult<RuleHistoryVO>}
	 */
	PageResult<RuleHistoryVO> selectByPage(PageParam pageParam, RuleHistoryQO ruleHistoryQO);

}