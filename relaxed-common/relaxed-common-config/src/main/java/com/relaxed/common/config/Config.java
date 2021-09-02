package com.relaxed.common.config;

import java.util.Map;
import java.util.Set;

/**
 * @author Yakir
 * @Topic Config
 * @Description
 * @date 2021/9/2 9:54
 * @Version 1.0
 */
public interface Config<K, V> {

	/**
	 * 初始化配置
	 * @author yakir
	 * @date 2021/9/2 10:01
	 * @param configs
	 */
	void initConfig(Map<K, V> configs);

	/**
	 * 获取所有配置信息
	 * @author yakir
	 * @date 2021/9/2 10:01
	 * @return java.util.Map<K,V>
	 */
	Map<K, V> getAllConfigs();

	/**
	 * 获取所有的keys
	 * @author yakir
	 * @date 2021/9/2 10:02
	 * @return java.util.Set<java.lang.String>
	 */
	Set<String> getAllConfigKeys();

	/**
	 * 添加一个配置
	 * @author yakir
	 * @date 2021/9/2 10:02
	 * @param key
	 * @param val
	 */
	void put(K key, V val);

	/**
	 * 删除配置
	 * @author yakir
	 * @date 2021/9/2 10:03
	 * @param key
	 */
	void del(K key);

	/**
	 * 获取配置
	 * @author yakir
	 * @date 2021/9/2 10:03
	 * @param key
	 * @return V
	 */
	V get(K key);

	/**
	 * 获取配置 获取不到返回默认值
	 * @author yakir
	 * @date 2021/9/2 10:03
	 * @param key
	 * @param defaultValue
	 * @return V
	 */
	V get(K key, V defaultValue);

}
