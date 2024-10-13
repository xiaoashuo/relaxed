package com.relaxed.extend.mybatis.encrypt;

/**
 * @author Yakir
 * @Topic FieldEncryptor
 * @Description
 * @date 2024/10/12 17:59
 * @Version 1.0
 */
public interface FieldEncryptor {

	/** 对数据进行加密 */
	String encrypt(String value);

	/** 对数据进行解密 */
	String decrypt(String value);

}
