package com.relaxed.oauth2.auth.extension;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * 预验证器持有者 用于管理和获取不同类型的预验证器实例 通过Map结构存储预验证器，支持根据验证类型快速获取对应的验证器
 *
 * @author Yakir
 * @since 1.0
 */
@RequiredArgsConstructor
public class PreValidatorHolder {

	/**
	 * 预验证器映射表 key: 验证类型 value: 对应的预验证器实例
	 */
	private final Map<String, PreValidator> preValidatorMap;

	/**
	 * 根据验证类型获取对应的预验证器
	 * @param supportType 验证类型
	 * @return 对应的预验证器实例，如果不存在则返回null
	 */
	public PreValidator getByType(String supportType) {
		return preValidatorMap.get(supportType);
	}

}
