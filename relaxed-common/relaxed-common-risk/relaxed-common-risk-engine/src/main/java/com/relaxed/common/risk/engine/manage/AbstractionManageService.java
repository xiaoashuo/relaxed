package com.relaxed.common.risk.engine.manage;

import com.relaxed.common.risk.engine.model.vo.AbstractionVO;

import java.util.List;

/**
 * @author Yakir
 * @Topic AbstractionManageService
 * @Description 特征管理service
 * @date 2021/8/29 18:52
 * @Version 1.0
 */
public interface AbstractionManageService {

	/**
	 * 获取特征提取列表
	 * @author yakir
	 * @date 2021/8/29 18:54
	 * @param modelId
	 * @return java.util.List<com.relaxed.common.risk.engine.model.vo.AbstractionVO>
	 */
	List<AbstractionVO> getByModelId(Long modelId);

}
