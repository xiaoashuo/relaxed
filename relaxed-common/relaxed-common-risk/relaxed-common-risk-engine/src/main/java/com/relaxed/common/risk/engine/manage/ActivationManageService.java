package com.relaxed.common.risk.engine.manage;

import com.relaxed.common.risk.engine.model.vo.ActivationVO;

import java.util.List;

/**
 * @author Yakir
 * @Topic ActivationManageService
 * @Description
 * @date 2021/8/31 10:42
 * @Version 1.0
 */
public interface ActivationManageService {

	/**
	 * 查询规则 根据modelId
	 * @author yakir
	 * @date 2021/8/31 10:49
	 * @param modelId
	 * @return java.util.List<com.relaxed.common.risk.engine.model.vo.ActivationVO>
	 */
	List<ActivationVO> listByModelId(Long modelId);

}
