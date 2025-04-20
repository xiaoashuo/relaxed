package com.relaxed.oauth2.auth.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 密码工具类 提供密码加密、解密和验证的功能 前端交互使用AES加密（CBC模式，PKCS5填充） 服务端OAuth2使用BCrypt加密
 *
 * @author Hccake
 * @since 1.0
 */
public final class PasswordUtils {

	/**
	 * 私有构造函数，防止实例化
	 */
	private PasswordUtils() {
	}

	/**
	 * 创建密码加密代理 支持多种密码加密算法，默认使用BCrypt 包含以下加密算法： - bcrypt: BCrypt加密 - ldap: LDAP加密 - MD4:
	 * MD4加密 - MD5: MD5加密 - noop: 不加密 - pbkdf2: PBKDF2加密 - scrypt: SCrypt加密 - SHA-1:
	 * SHA-1加密 - SHA-256: SHA-256加密 - sha256: 标准密码加密
	 * @return 密码加密代理
	 */
	@SuppressWarnings("deprecation")
	private static PasswordEncoder createDelegatingPasswordEncoder() {
		String encodingId = "bcrypt";
		Map<String, PasswordEncoder> encoders = new HashMap<>(10);
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		encoders.put(encodingId, bCryptPasswordEncoder);
		encoders.put("ldap", new org.springframework.security.crypto.password.LdapShaPasswordEncoder());
		encoders.put("MD4", new org.springframework.security.crypto.password.Md4PasswordEncoder());
		encoders.put("MD5", new org.springframework.security.crypto.password.MessageDigestPasswordEncoder("MD5"));
		encoders.put("noop", org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance());
		encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
		encoders.put("scrypt", new SCryptPasswordEncoder());
		encoders.put("SHA-1", new org.springframework.security.crypto.password.MessageDigestPasswordEncoder("SHA-1"));
		encoders.put("SHA-256",
				new org.springframework.security.crypto.password.MessageDigestPasswordEncoder("SHA-256"));
		encoders.put("sha256", new org.springframework.security.crypto.password.StandardPasswordEncoder());
		DelegatingPasswordEncoder delegatingPasswordEncoder = new DelegatingPasswordEncoder(encodingId, encoders);

		// 设置默认的密码解析器，以便兼容历史版本的密码
		delegatingPasswordEncoder.setDefaultPasswordEncoderForMatches(bCryptPasswordEncoder);
		return delegatingPasswordEncoder;
	}

	/**
	 * 默认密码加密器 使用BCrypt作为默认加密算法
	 */
	public static final PasswordEncoder ENCODER = PasswordUtils.createDelegatingPasswordEncoder();

	/**
	 * 解密AES加密的密码 使用CBC模式和PKCS5填充
	 * @param aesPass AES加密后的密文
	 * @param secretKey 密钥，同时作为偏移量
	 * @return 解密后的明文密码
	 */
	public static String decodeAES(String aesPass, String secretKey) {
		byte[] secretKeyBytes = secretKey.getBytes();
		AES aes = new AES(Mode.CBC, Padding.PKCS5Padding, secretKeyBytes, secretKeyBytes);
		byte[] result = aes.decrypt(Base64.decode(aesPass.getBytes(StandardCharsets.UTF_8)));
		return new String(result, StandardCharsets.UTF_8);
	}

	/**
	 * 使用AES加密密码 使用CBC模式和PKCS5填充
	 * @param password 明文密码
	 * @param secretKey 密钥，同时作为偏移量
	 * @return Base64编码的AES加密密文
	 */
	public static String encodeAESBase64(String password, String secretKey) {
		byte[] secretKeyBytes = secretKey.getBytes();
		AES aes = new AES(Mode.CBC, Padding.PKCS5Padding, secretKeyBytes, secretKeyBytes);
		return aes.encryptBase64(password, StandardCharsets.UTF_8);
	}

	/**
	 * 使用默认加密器加密密码
	 * @param rawPassword 明文密码
	 * @return 加密后的密文密码
	 */
	public static String encode(CharSequence rawPassword) {
		return ENCODER.encode(rawPassword);
	}

	/**
	 * 验证明文密码和密文密码是否匹配
	 * @param rawPassword 明文密码
	 * @param encodedPassword 密文密码
	 * @return 如果匹配返回true，否则返回false
	 */
	public static boolean matches(CharSequence rawPassword, String encodedPassword) {
		return ENCODER.matches(rawPassword, encodedPassword);
	}

	/**
	 * 检查密码是否需要升级加密算法 用于密码加密算法的平滑升级
	 * @param encodedPassword 密文密码
	 * @return 如果需要升级返回true，否则返回false
	 */
	public static boolean upgradeEncoding(String encodedPassword) {
		return ENCODER.upgradeEncoding(encodedPassword);
	}

}
