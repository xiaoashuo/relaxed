package com.relaxed.extend.mybatis.encrypt;

import cn.hutool.core.lang.Assert;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Yakir
 * @Topic SecurityHolder
 * @Description
 * @date 2024/10/18 13:40
 * @Version 1.0
 */
@Slf4j
public class FieldSecurityHolder {

	private static Map<String, FieldEncryptor> SECURITY_HOLDER = new HashMap<>();

	public static FieldSecurityHolder INSTANCE = new FieldSecurityHolder();

	private FieldSecurityHolder() {
	}

	public FieldEncryptor regByType(String secType, FieldEncryptor fieldEncryptor) {
		Assert.notBlank(secType, "加密算法不能为空");
		Assert.notNull(fieldEncryptor, "类型:{},字段加解密提取器不能为空", secType);
		SECURITY_HOLDER.put(secType, fieldEncryptor);
		log.info("Mybatis字段加密算法:{},执行器注册成功===>{}", secType, fieldEncryptor.getClass().getName());
		return fieldEncryptor;
	}

	public FieldEncryptor getByType(String secType) {
		return SECURITY_HOLDER.get(secType);
	}

}
