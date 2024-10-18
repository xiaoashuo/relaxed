package com.relaxed.extend.mybatis.encrypt.sec;

import com.baomidou.mybatisplus.core.toolkit.AES;
import com.relaxed.extend.mybatis.encrypt.FieldEncryptor;
import com.relaxed.extend.mybatis.encrypt.FieldSecurityProperties;
import lombok.RequiredArgsConstructor;

/**
 * @author Yakir
 * @Topic AesFieldEncryptor
 * @Description
 * @date 2024/10/18 13:55
 * @Version 1.0
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
