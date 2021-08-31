package com.relaxed.common.risk.engine.manage;

import com.relaxed.common.risk.engine.model.qo.RuleQO;
import com.relaxed.common.risk.engine.model.vo.ActivationVO;
import com.relaxed.common.risk.engine.model.vo.RuleVO;

import java.util.List;

/**
 * @author Yakir
 * @Topic RuleManageService
 * @Description
 * @date 2021/8/31 11:33
 * @Version 1.0
 */
public interface RuleManageService {

	/**
	 * 查询规则列表
	 * @author yakir
	 * @date 2021/8/31 11:42
	 * @param activationId
	 * @return java.util.List<com.relaxed.common.risk.engine.model.vo.RuleVO>
	 */
	List<RuleVO> listRule(Long activationId);

}
