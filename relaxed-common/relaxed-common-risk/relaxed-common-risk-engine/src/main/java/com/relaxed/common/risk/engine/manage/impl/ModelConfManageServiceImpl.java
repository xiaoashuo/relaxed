package com.relaxed.common.risk.engine.manage.impl;

import com.relaxed.common.risk.engine.manage.ModelConfManageService;
import com.relaxed.common.risk.engine.model.vo.ModelConfParamVO;
import com.relaxed.common.risk.engine.model.vo.ModelConfVO;
import com.relaxed.common.risk.engine.service.ModelConfParamService;
import com.relaxed.common.risk.engine.service.ModelConfService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Yakir
 * @Topic ModelConfManageServiceImpl
 * @Description
 * @date 2021/8/31 10:01
 * @Version 1.0
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class ModelConfManageServiceImpl implements ModelConfManageService {

	private final ModelConfService modelConfService;

	private final ModelConfParamService modelConfParamService;

	@Override
	public ModelConfVO getByModelId(Long modelId) {
		ModelConfVO modelConfVO = modelConfService.getByModelId(modelId);
		List<ModelConfParamVO> modelConfParamVOS = modelConfParamService.listByModelConfId(modelConfVO.getId());
		modelConfVO.setParams(modelConfParamVOS);
		return modelConfVO;
	}

}
