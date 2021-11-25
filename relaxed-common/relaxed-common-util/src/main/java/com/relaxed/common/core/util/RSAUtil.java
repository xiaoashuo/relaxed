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
 * @author Yakir
 * @Topic RSAUtil
 * @Description RSA 工具类
 * @date 2021/11/19 11:29
 * @Version 1.0
 */
public class RSAUtil {

	/**
	 * 获取私钥
	 * @author yakir
	 * @date 2021/11/19 9:59
	 * @param privateKeyStr 私钥Hex或Base64表示
	 * @return java.security.PrivateKey
	 */
	public static PrivateKey getPrivateKey(String privateKeyStr) {
		return SecureUtil.generatePrivateKey(SignAlgorithm.SHA1withRSA.getValue(), SecureUtil.decode(privateKeyStr));
	}

	/**
	 * 获取公钥
	 * @author yakir
	 * @date 2021/11/19 10:00
	 * @param publicKeyStr 公钥Hex或Base64表示
	 * @return java.security.PublicKey
	 */
	public static PublicKey getPublicKey(String publicKeyStr) {
		return SecureUtil.generatePublicKey(SignAlgorithm.SHA1withRSA.getValue(), SecureUtil.decode(publicKeyStr));
	}

	/**
	 * 生成Rsa对
	 * @author yakir
	 * @date 2021/11/19 10:03
	 * @return java.security.KeyPair
	 */
	public static KeyPair generateRasPair() {
		return SecureUtil.generateKeyPair(SignAlgorithm.SHA1withRSA.getValue());
	}

	/**
	 *
	 * 构造 <br>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 * @author yakir
	 * @date 2021/11/19 10:11
	 * @param privateKey privateKey 私钥
	 * @param publicKey publicKey 公钥
	 * @return cn.hutool.crypto.asymmetric.RSA
	 */
	public static RSA rsa(PrivateKey privateKey, PublicKey publicKey) {
		return new RSA(privateKey, publicKey);
	}

	/******************************** 私钥加密 公钥解密 ************************************/
	/**
	 * 通过私钥编码
	 * @author yakir
	 * @date 2021/11/19 10:16
	 * @param privateKey
	 * @param data
	 * @return byte[]
	 */
	public static byte[] encryptByPrivateKey(PrivateKey privateKey, byte[] data) {
		return rsa(privateKey, null).encrypt(data, KeyType.PrivateKey);
	}

	/**
	 * 通过私钥编码返回base64
	 * @author yakir
	 * @date 2021/11/19 10:17
	 * @param privateKey
	 * @param data
	 * @return java.lang.String base64 str
	 */
	public static String encryptBase64ByPrivateKey(PrivateKey privateKey, byte[] data) {
		return Base64.encode(encryptByPrivateKey(privateKey, data));
	}

	/**
	 * 通过私钥编码返回HexStr
	 * @author yakir
	 * @date 2021/11/19 10:17
	 * @param privateKey
	 * @param data
	 * @return java.lang.String HexStr
	 */
	public static String encryptHexStrByPrivateKey(PrivateKey privateKey, byte[] data) {
		return HexUtil.encodeHexStr(encryptByPrivateKey(privateKey, data));
	}

	/**
	 * 公钥解密
	 * @author yakir
	 * @date 2021/11/19 10:19
	 * @param publicKey
	 * @param data
	 * @return byte[]
	 */
	public static byte[] decryptByPublicKey(PublicKey publicKey, byte[] data) {
		return rsa(null, publicKey).decrypt(data, KeyType.PublicKey);
	}

	/**
	 * 通过公钥解密 转成String
	 * @author yakir
	 * @date 2021/11/19 10:23
	 * @param publicKey
	 * @param charset
	 * @param data
	 * @return java.lang.String
	 */
	public static String decryptToStrByPublicKey(PublicKey publicKey, String charset, byte[] data) {
		return StrUtil.str(decryptByPublicKey(publicKey, data), charset);
	}

	/**
	 * 通过公钥解密 转成String 采用默认字符集
	 * @author yakir
	 * @date 2021/11/19 10:24
	 * @param publicKey
	 * @param data
	 * @return java.lang.String
	 */
	public static String decryptToStrByPublicKey(PublicKey publicKey, byte[] data) {
		return decryptToStrByPublicKey(publicKey, null, data);
	}

	/**
	 * 根据公钥解密
	 * @author yakir
	 * @date 2021/11/19 10:59
	 * @param publicKey
	 * @param data 16进制内容或base64
	 * @return java.lang.String
	 */
	public static String decryptStrToStrByPublicKey(PublicKey publicKey, String charset, String data) {
		return decryptToStrByPublicKey(publicKey, charset, SecureUtil.decode(data));
	}

	/**
	 * 根据公钥解密
	 * @author yakir
	 * @date 2021/11/19 11:00
	 * @param publicKey
	 * @param data 16进制内容或base64
	 * @return java.lang.String
	 */
	public static String decryptStrToStrByPublicKey(PublicKey publicKey, String data) {
		return decryptStrToStrByPublicKey(publicKey, null, data);
	}

	/******************************** 公钥加密 私钥解密 ************************************/
	/**
	 * 编码根据公钥
	 * @author yakir
	 * @date 2021/11/19 10:27
	 * @param publicKey
	 * @param data
	 * @return byte[]
	 */
	public static byte[] encryptByPublicKey(PublicKey publicKey, byte[] data) {
		return rsa(null, publicKey).encrypt(data, KeyType.PublicKey);
	}

	/**
	 * 通过私钥编码返回base64
	 * @author yakir
	 * @date 2021/11/19 10:17
	 * @param publicKey
	 * @param data
	 * @return java.lang.String base64 str
	 */
	public static String encryptBase64ByPublicKey(PublicKey publicKey, byte[] data) {
		return Base64.encode(encryptByPublicKey(publicKey, data));
	}

	/**
	 * 通过私钥编码返回HexStr
	 * @author yakir
	 * @date 2021/11/19 10:17
	 * @param publicKey
	 * @param data
	 * @return java.lang.String HexStr
	 */
	public static String encryptHexStrByPublicKey(PublicKey publicKey, byte[] data) {
		return HexUtil.encodeHexStr(encryptByPublicKey(publicKey, data));
	}

	/**
	 * 根据私钥解密
	 * @author yakir
	 * @date 2021/11/19 10:32
	 * @param privateKey
	 * @param data
	 * @return byte[]
	 */
	public static byte[] decryptByPrivateKey(PrivateKey privateKey, byte[] data) {
		return rsa(privateKey, null).decrypt(data, KeyType.PrivateKey);
	}

	/**
	 * 通过私钥解密 转成String
	 * @author yakir
	 * @date 2021/11/19 10:23
	 * @param privateKey
	 * @param charset
	 * @param data
	 * @return java.lang.String
	 */
	public static String decryptToStrByPrivateKey(PrivateKey privateKey, String charset, byte[] data) {
		return StrUtil.str(decryptByPrivateKey(privateKey, data), charset);
	}

	/**
	 * 通过私钥解密 转成String 采用默认字符集
	 * @author yakir
	 * @date 2021/11/19 10:24
	 * @param privateKey
	 * @param data
	 * @return java.lang.String
	 */
	public static String decryptToStrByPrivateKey(PrivateKey privateKey, byte[] data) {
		return decryptToStrByPrivateKey(privateKey, null, data);
	}

	/**
	 * 根据私钥解密
	 * @author yakir
	 * @date 2021/11/19 10:59
	 * @param privateKey
	 * @param data 16进制内容或base64
	 * @return java.lang.String
	 */
	public static String decryptStrToStrByPrivateKey(PrivateKey privateKey, String charset, String data) {
		return decryptToStrByPrivateKey(privateKey, charset, SecureUtil.decode(data));
	}

	/**
	 * 根据私钥解密
	 * @author yakir
	 * @date 2021/11/19 11:00
	 * @param privateKey
	 * @param data 16进制内容或base64
	 * @return java.lang.String
	 */
	public static String decryptStrToStrByPrivateKey(PrivateKey privateKey, String data) {
		return decryptStrToStrByPrivateKey(privateKey, null, data);
	}

	/******************************* 签名 验签 ************************************/

	/**
	 * 获取签名 构造 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做签名或验证
	 * @author yakir
	 * @date 2021/11/19 11:17
	 * @param privateKey
	 * @param publicKey
	 * @return cn.hutool.crypto.asymmetric.Sign
	 */
	public static Sign sign(PrivateKey privateKey, PublicKey publicKey) {
		return new Sign(SignAlgorithm.SHA1withRSA, privateKey, publicKey);
	}

	/**
	 * 私钥签名
	 * @author yakir
	 * @date 2021/11/19 11:20
	 * @param privateKey
	 * @param data
	 * @return byte[]
	 */
	public static byte[] signToByte(PrivateKey privateKey, byte[] data) {
		return sign(privateKey, null).sign(data);
	}

	/**
	 * 私钥签名 Base 64 Str
	 * @author yakir
	 * @date 2021/11/19 11:20
	 * @param privateKey
	 * @param data
	 * @return java.lang.String
	 */
	public static String signToBase64Str(PrivateKey privateKey, byte[] data) {
		return Base64.encode(signToByte(privateKey, data));
	}

	/**
	 * 私钥签名 Hex Str
	 * @author yakir
	 * @date 2021/11/19 11:20
	 * @param privateKey
	 * @param data
	 * @return java.lang.String
	 */
	public static String signToHexStr(PrivateKey privateKey, byte[] data) {
		return HexUtil.encodeHexStr(signToByte(privateKey, data));
	}

	/**
	 * 效验签名
	 * @author yakir
	 * @date 2021/11/19 11:21
	 * @param publicKey
	 * @param data
	 * @param sign
	 * @return boolean
	 */
	public static boolean verify(PublicKey publicKey, byte[] data, byte[] sign) {
		return sign(null, publicKey).verify(data, sign);
	}

	/**
	 * 效验签名
	 * @author yakir
	 * @date 2021/11/19 11:23
	 * @param publicKey
	 * @param data
	 * @param sign 可以Hex Str 或 base64 Str
	 * @return boolean
	 */
	public static boolean verify(PublicKey publicKey, byte[] data, String sign) {
		return verify(publicKey, data, SecureUtil.decode(sign));
	}

}
