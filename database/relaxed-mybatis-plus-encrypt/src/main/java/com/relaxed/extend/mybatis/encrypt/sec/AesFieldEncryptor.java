package com.relaxed.extend.mybatis.encrypt.sec;

import com.baomidou.mybatisplus.core.toolkit.AES;
import com.relaxed.extend.mybatis.encrypt.FieldEncryptor;
import com.relaxed.extend.mybatis.encrypt.FieldSecurityProperties;
import lombok.RequiredArgsConstructor;

/**
 * AES字段加密器实现类 使用AES算法对字段进行加密和解密操作 支持通过配置文件自定义加密密钥
 *
 * @author Yakir
 */
@RequiredArgsConstructor
public class AesFieldEncryptor implements FieldEncryptor {

	private final FieldSecurityProperties.AES aesProperties;

	@Override
	public String secType() {
		return aesProperties.SEC_FLAG;
	}

	@Override
	public String encrypt(String value) {
		return AES.encrypt(value, aesProperties.getKey());
	}

	@Override
	public String decrypt(String value) {
		return AES.decrypt(value, aesProperties.getKey());
	}

}
