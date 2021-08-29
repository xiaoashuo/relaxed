package com.relaxed.common.risk.engine.manage.impl;

import com.relaxed.common.risk.engine.core.handler.FieldValidateHandler;
import com.relaxed.common.risk.engine.manage.FieldManageService;
import com.relaxed.common.risk.engine.manage.FieldValidateService;
import com.relaxed.common.risk.engine.model.vo.FieldVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Yakir
 * @Topic FieldValidServiceImpl
 * @Description
 * @date 2021/8/29 13:01
 * @Version 1.0
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class FieldValidServiceImpl implements FieldValidateService {

	private final FieldManageService fieldManageService;

	private final FieldValidateHandler fieldValidateHandler;

	@Override
	public Map<String, String> validate(Long modelId, Map data) {
		List<FieldVO> fieldVos = fieldManageService.getFieldVos(modelId);
		Map<String, String> result = new HashMap<>();
		for (FieldVO fieldVo : fieldVos) {
			FieldValidateHandler.FieldValidResult validResult = fieldValidateHandler.valid(fieldVo, data);
			if (validResult.isValid()) {
				continue;
			}
			result.put(validResult.getFieldName(), validResult.getErrorDesc());
		}
		return result;
	}

}
