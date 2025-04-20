package com.relaxed.extend.mybatis.encrypt;

/**
 * 字段加密器接口 定义字段加密和解密的标准方法，支持不同加密算法的实现
 *
 * @author Yakir
 */
public interface FieldEncryptor {

	/**
	 * 获取加密算法类型
	 * @return 加密算法类型标识
	 */
	String secType();

	/**
	 * 对字符串数据进行加密
	 * @param value 需要加密的原始数据
	 * @return 加密后的数据
	 */
	String encrypt(String value);

	/**
	 * 对加密后的数据进行解密
	 * @param value 需要解密的加密数据
	 * @return 解密后的原始数据
	 */
	String decrypt(String value);

}
