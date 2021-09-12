package com.relaxed.common.risk.engine.service.impl;

import com.relaxed.common.risk.biz.service.ActivationService;
import com.relaxed.common.risk.engine.service.ActivationManageService;
import com.relaxed.common.risk.model.vo.ActivationVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Yakir
 * @Topic ActivationManageServiceImpl
 * @Description
 * @date 2021/8/31 10:42
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ActivationManageServiceImpl implements ActivationManageService {

	private final ActivationService activationService;

	@Override
	public List<ActivationVO> listByModelId(Long modelId) {
		return activationService.listByModelId(modelId);
	}

}
