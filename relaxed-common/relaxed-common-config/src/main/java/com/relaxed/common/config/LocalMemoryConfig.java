package com.relaxed.common.config;

import cn.hutool.core.lang.Dict;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @author Yakir
 * @Topic MemoryConfig
 * @Description
 * @date 2021/9/2 9:54
 * @Version 1.0
 */
public class LocalMemoryConfig implements Config<String, Object> {

	/**
	 * 系统配置的Map缓存
	 */
	private static final Dict CONFIG_CONTAINER = Dict.create();

	@Override
	public void initConfig(Map<String, Object> configs) {
		if (configs == null || configs.size() == 0) {
			return;
		}
		CONFIG_CONTAINER.putAll(configs);
	}

	@Override
	public Map<String, Object> getAllConfigs() {
		return CONFIG_CONTAINER;
	}

	@Override
	public Set<String> getAllConfigKeys() {
		return CONFIG_CONTAINER.keySet();
	}

	@Override
	public void put(String key, Object val) {
		CONFIG_CONTAINER.put(key, val);
	}

	@Override
	public void del(String key) {
		CONFIG_CONTAINER.remove(key);
	}

	@Override
	public Object get(String key) {
		return CONFIG_CONTAINER.get(key);
	}

	@Override
	public Object get(String key, Object defaultValue) {
		return Optional.ofNullable(CONFIG_CONTAINER.get(key)).orElseGet(() -> defaultValue);
	}

}
