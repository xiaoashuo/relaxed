package com.relaxed.common.core.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * RSA 加密解密工具类，提供以下功能： 1. RSA 密钥对生成和管理 2. 公钥加密/私钥解密 3. 私钥加密/公钥解密 4. 数字签名生成和验证 5.
 * 支持多种编码格式（Base64、Hex） 6. 支持多种字符集的加解密
 *
 * 本工具类基于 hutool-crypto 实现，提供了更便捷的 RSA 操作方法。 使用示例： <pre>
 * // 生成密钥对
 * KeyPair keyPair = RSAUtil.generateRasPair();
 *
 * // 使用公钥加密
 * String encrypted = RSAUtil.encryptBase64ByPublicKey(keyPair.getPublic(), "Hello".getBytes());
 *
 * // 使用私钥解密
 * String decrypted = RSAUtil.decryptStrToStrByPrivateKey(keyPair.getPrivate(), encrypted);
 * </pre>
 *
 * @author Yakir
 * @since 1.0
 */
public class RSAUtil {

	/**
	 * 获取 RSA 私钥对象
	 * @param privateKeyStr 私钥字符串，支持 Hex 或 Base64 编码格式
	 * @return RSA 私钥对象
	 */
	public static PrivateKey getPrivateKey(String privateKeyStr) {
		return SecureUtil.generatePrivateKey(SignAlgorithm.SHA1withRSA.getValue(), SecureUtil.decode(privateKeyStr));
	}

	/**
	 * 获取 RSA 公钥对象
	 * @param publicKeyStr 公钥字符串，支持 Hex 或 Base64 编码格式
	 * @return RSA 公钥对象
	 */
	public static PublicKey getPublicKey(String publicKeyStr) {
		return SecureUtil.generatePublicKey(SignAlgorithm.SHA1withRSA.getValue(), SecureUtil.decode(publicKeyStr));
	}

	/**
	 * 生成 RSA 密钥对
	 * @return RSA 密钥对，包含公钥和私钥
	 */
	public static KeyPair generateRasPair() {
		return SecureUtil.generateKeyPair(SignAlgorithm.SHA1withRSA.getValue());
	}

	/**
	 * 创建 RSA 加解密对象
	 *
	 * 说明： 1. 如果私钥和公钥都为空，将生成新的密钥对 2. 可以只传入私钥或公钥，此时只能进行相应的加密或解密操作 3. 同时传入私钥和公钥时，可以进行所有 RSA
	 * 相关操作
	 * @param privateKey RSA 私钥对象，可以为 null
	 * @param publicKey RSA 公钥对象，可以为 null
	 * @return RSA 加解密对象
	 */
	public static RSA rsa(PrivateKey privateKey, PublicKey publicKey) {
		return new RSA(privateKey, publicKey);
	}

	/******************************** 私钥加密 公钥解密 ************************************/
	/**
	 * 使用私钥加密数据
	 * @param privateKey RSA 私钥对象
	 * @param data 待加密的数据
	 * @return 加密后的字节数组
	 */
	public static byte[] encryptByPrivateKey(PrivateKey privateKey, byte[] data) {
		return rsa(privateKey, null).encrypt(data, KeyType.PrivateKey);
	}

	/**
	 * 使用私钥加密数据并返回 Base64 编码的字符串
	 * @param privateKey RSA 私钥对象
	 * @param data 待加密的数据
	 * @return Base64 编码的加密结果
	 */
	public static String encryptBase64ByPrivateKey(PrivateKey privateKey, byte[] data) {
		return Base64.encode(encryptByPrivateKey(privateKey, data));
	}

	/**
	 * 使用私钥加密数据并返回十六进制字符串
	 * @param privateKey RSA 私钥对象
	 * @param data 待加密的数据
	 * @return 十六进制编码的加密结果
	 */
	public static String encryptHexStrByPrivateKey(PrivateKey privateKey, byte[] data) {
		return HexUtil.encodeHexStr(encryptByPrivateKey(privateKey, data));
	}

	/**
	 * 使用公钥解密数据
	 * @param publicKey RSA 公钥对象
	 * @param data 待解密的数据
	 * @return 解密后的字节数组
	 */
	public static byte[] decryptByPublicKey(PublicKey publicKey, byte[] data) {
		return rsa(null, publicKey).decrypt(data, KeyType.PublicKey);
	}

	/**
	 * 使用公钥解密数据并转换为字符串
	 * @param publicKey RSA 公钥对象
	 * @param charset 字符集，如果为 null 则使用系统默认字符集
	 * @param data 待解密的数据
	 * @return 解密后的字符串
	 */
	public static String decryptToStrByPublicKey(PublicKey publicKey, String charset, byte[] data) {
		return StrUtil.str(decryptByPublicKey(publicKey, data), charset);
	}

	/**
	 * 使用公钥解密数据并使用默认字符集转换为字符串
	 * @param publicKey RSA 公钥对象
	 * @param data 待解密的数据
	 * @return 解密后的字符串
	 */
	public static String decryptToStrByPublicKey(PublicKey publicKey, byte[] data) {
		return decryptToStrByPublicKey(publicKey, null, data);
	}

	/**
	 * 使用公钥解密 Base64 或十六进制编码的数据
	 * @param publicKey RSA 公钥对象
	 * @param charset 字符集，如果为 null 则使用系统默认字符集
	 * @param data Base64 或十六进制编码的加密数据
	 * @return 解密后的字符串
	 */
	public static String decryptStrToStrByPublicKey(PublicKey publicKey, String charset, String data) {
		return decryptToStrByPublicKey(publicKey, charset, SecureUtil.decode(data));
	}

	/**
	 * 使用公钥解密 Base64 或十六进制编码的数据，使用默认字符集
	 * @param publicKey RSA 公钥对象
	 * @param data Base64 或十六进制编码的加密数据
	 * @return 解密后的字符串
	 */
	public static String decryptStrToStrByPublicKey(PublicKey publicKey, String data) {
		return decryptStrToStrByPublicKey(publicKey, null, data);
	}

	/******************************** 公钥加密 私钥解密 ************************************/
	/**
	 * 使用公钥加密数据
	 * @param publicKey RSA 公钥对象
	 * @param data 待加密的数据
	 * @return 加密后的字节数组
	 */
	public static byte[] encryptByPublicKey(PublicKey publicKey, byte[] data) {
		return rsa(null, publicKey).encrypt(data, KeyType.PublicKey);
	}

	/**
	 * 使用公钥加密数据并返回 Base64 编码的字符串
	 * @param publicKey RSA 公钥对象
	 * @param data 待加密的数据
	 * @return Base64 编码的加密结果
	 */
	public static String encryptBase64ByPublicKey(PublicKey publicKey, byte[] data) {
		return Base64.encode(encryptByPublicKey(publicKey, data));
	}

	/**
	 * 使用公钥加密数据并返回十六进制字符串
	 * @param publicKey RSA 公钥对象
	 * @param data 待加密的数据
	 * @return 十六进制编码的加密结果
	 */
	public static String encryptHexStrByPublicKey(PublicKey publicKey, byte[] data) {
		return HexUtil.encodeHexStr(encryptByPublicKey(publicKey, data));
	}

	/**
	 * 使用私钥解密数据
	 * @param privateKey RSA 私钥对象
	 * @param data 待解密的数据
	 * @return 解密后的字节数组
	 */
	public static byte[] decryptByPrivateKey(PrivateKey privateKey, byte[] data) {
		return rsa(privateKey, null).decrypt(data, KeyType.PrivateKey);
	}

	/**
	 * 使用私钥解密数据并转换为字符串
	 * @param privateKey RSA 私钥对象
	 * @param charset 字符集，如果为 null 则使用系统默认字符集
	 * @param data 待解密的数据
	 * @return 解密后的字符串
	 */
	public static String decryptToStrByPrivateKey(PrivateKey privateKey, String charset, byte[] data) {
		return StrUtil.str(decryptByPrivateKey(privateKey, data), charset);
	}

	/**
	 * 使用私钥解密数据并使用默认字符集转换为字符串
	 * @param privateKey RSA 私钥对象
	 * @param data 待解密的数据
	 * @return 解密后的字符串
	 */
	public static String decryptToStrByPrivateKey(PrivateKey privateKey, byte[] data) {
		return decryptToStrByPrivateKey(privateKey, null, data);
	}

	/**
	 * 使用私钥解密 Base64 或十六进制编码的数据
	 * @param privateKey RSA 私钥对象
	 * @param charset 字符集，如果为 null 则使用系统默认字符集
	 * @param data Base64 或十六进制编码的加密数据
	 * @return 解密后的字符串
	 */
	public static String decryptStrToStrByPrivateKey(PrivateKey privateKey, String charset, String data) {
		return decryptToStrByPrivateKey(privateKey, charset, SecureUtil.decode(data));
	}

	/**
	 * 使用私钥解密 Base64 或十六进制编码的数据，使用默认字符集
	 * @param privateKey RSA 私钥对象
	 * @param data Base64 或十六进制编码的加密数据
	 * @return 解密后的字符串
	 */
	public static String decryptStrToStrByPrivateKey(PrivateKey privateKey, String data) {
		return decryptStrToStrByPrivateKey(privateKey, null, data);
	}

	/******************************** 数字签名 ************************************/
	/**
	 * 创建数字签名对象
	 * @param privateKey RSA 私钥对象，用于生成签名
	 * @param publicKey RSA 公钥对象，用于验证签名
	 * @return 数字签名对象
	 */
	public static Sign sign(PrivateKey privateKey, PublicKey publicKey) {
		return new Sign(SignAlgorithm.SHA1withRSA, privateKey, publicKey);
	}

	/**
	 * 使用私钥对数据进行签名
	 * @param privateKey RSA 私钥对象
	 * @param data 待签名的数据
	 * @return 签名结果字节数组
	 */
	public static byte[] signToByte(PrivateKey privateKey, byte[] data) {
		return sign(privateKey, null).sign(data);
	}

	/**
	 * 使用私钥对数据进行签名并返回 Base64 编码的字符串
	 * @param privateKey RSA 私钥对象
	 * @param data 待签名的数据
	 * @return Base64 编码的签名结果
	 */
	public static String signToBase64Str(PrivateKey privateKey, byte[] data) {
		return Base64.encode(signToByte(privateKey, data));
	}

	/**
	 * 使用私钥对数据进行签名并返回十六进制字符串
	 * @param privateKey RSA 私钥对象
	 * @param data 待签名的数据
	 * @return 十六进制编码的签名结果
	 */
	public static String signToHexStr(PrivateKey privateKey, byte[] data) {
		return HexUtil.encodeHexStr(signToByte(privateKey, data));
	}

	/**
	 * 使用公钥验证签名
	 * @param publicKey RSA 公钥对象
	 * @param data 原始数据
	 * @param sign 签名数据
	 * @return 验证结果，true 表示验证通过
	 */
	public static boolean verify(PublicKey publicKey, byte[] data, byte[] sign) {
		return sign(null, publicKey).verify(data, sign);
	}

	/**
	 * 使用公钥验证 Base64 或十六进制编码的签名
	 * @param publicKey RSA 公钥对象
	 * @param data 原始数据
	 * @param sign Base64 或十六进制编码的签名数据
	 * @return 验证结果，true 表示验证通过
	 */
	public static boolean verify(PublicKey publicKey, byte[] data, String sign) {
		return verify(publicKey, data, SecureUtil.decode(sign));
	}

}
