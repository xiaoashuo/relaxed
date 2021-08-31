package com.relaxed.common.risk.engine.manage.impl;

import com.relaxed.common.risk.engine.manage.ActivationManageService;
import com.relaxed.common.risk.engine.model.vo.ActivationVO;
import com.relaxed.common.risk.engine.service.ActivationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author Yakir
 * @Topic ActivationManageServiceImpl
 * @Description
 * @date 2021/8/31 10:42
 * @Version 1.0
 */
@RequiredArgsConstructor
@Slf4j
public class ActivationManageServiceImpl implements ActivationManageService {

	private final ActivationService activationService;

	@Override
	public List<ActivationVO> listByModelId(Long modelId) {
		return activationService.listByModelId(modelId);
	}

}
