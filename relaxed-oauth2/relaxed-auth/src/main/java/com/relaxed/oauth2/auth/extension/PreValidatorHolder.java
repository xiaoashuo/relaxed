package com.relaxed.oauth2.auth.extension;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Yakir
 * @Topic PreValidatorHolder
 * @Description
 * @date 2022/8/16 17:12
 * @Version 1.0
 */
@RequiredArgsConstructor
public class PreValidatorHolder {

	private final Map<String, PreValidator> preValidatorMap;

	public PreValidator getByType(String supportType) {
		return preValidatorMap.get(supportType);
	}

}
