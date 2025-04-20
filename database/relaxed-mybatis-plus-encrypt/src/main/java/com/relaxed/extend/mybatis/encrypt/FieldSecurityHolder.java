package com.relaxed.extend.mybatis.encrypt;

import cn.hutool.core.lang.Assert;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * 字段加密器持有者 单例模式，用于管理和维护不同类型的字段加密器 支持注册和获取指定类型的加密器
 *
 * @author Yakir
 */
@Slf4j
public class FieldSecurityHolder {

	/**
	 * 加密器存储容器，key为加密算法类型，value为对应的加密器实例
	 */
	private static Map<String, FieldEncryptor> SECURITY_HOLDER = new HashMap<>();

	/**
	 * 单例实例
	 */
	public static FieldSecurityHolder INSTANCE = new FieldSecurityHolder();

	/**
	 * 私有构造函数，确保单例模式
	 */
	private FieldSecurityHolder() {
	}

	/**
	 * 注册指定类型的加密器
	 * @param secType 加密算法类型
	 * @param fieldEncryptor 字段加密器实例
	 * @return 注册的加密器实例
	 */
	public FieldEncryptor regByType(String secType, FieldEncryptor fieldEncryptor) {
		Assert.notBlank(secType, "加密算法不能为空");
		Assert.notNull(fieldEncryptor, "类型:{},字段加解密提取器不能为空", secType);
		SECURITY_HOLDER.put(secType, fieldEncryptor);
		log.info("Mybatis字段加密算法:{},执行器注册成功===>{}", secType, fieldEncryptor.getClass().getName());
		return fieldEncryptor;
	}

	/**
	 * 获取指定类型的加密器
	 * @param secType 加密算法类型
	 * @return 对应的加密器实例，如果不存在则返回null
	 */
	public FieldEncryptor getByType(String secType) {
		return SECURITY_HOLDER.get(secType);
	}

}
