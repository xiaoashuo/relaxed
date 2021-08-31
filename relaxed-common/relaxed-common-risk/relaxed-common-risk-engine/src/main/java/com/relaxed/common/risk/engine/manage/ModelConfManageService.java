package com.relaxed.common.risk.engine.manage;

import com.relaxed.common.risk.engine.model.vo.ModelConfVO;

/**
 * @author Yakir
 * @Topic ModelConfManageService
 * @Description
 * @date 2021/8/31 10:01
 * @Version 1.0
 */
public interface ModelConfManageService {

	/**
	 * 根据model id 获取model conf
	 * @author yakir
	 * @date 2021/8/31 10:02
	 * @param modelId
	 * @return com.relaxed.common.risk.engine.model.vo.ModelConfVO
	 */
	ModelConfVO getByModelId(Long modelId);

}
