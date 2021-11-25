package com.relaxed.common.core.util;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author Yakir
 * @Topic RSAUtilTest
 * @Description 使用建议 在自己项目中 对此工具类 进行二次装饰 避免了 私钥的传参
 * @date 2021/11/19 11:30
 * @Version 1.0
 */
class RSAUtilTest {

	// 私钥
	public static String privateKeyStr = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCqrpUHZx/NSRsK"
			+ "jQc9i/gCGzU9Q3qRf44pm0lML9FfJf+m9mo9qdAEsMl3N0Y/nVcdGWb8VBry+dyl"
			+ "mKNR4VPfFNGQKWW8OtvUlT9l3I9/MTd/ZsoVt2dQdBvOp7+9hrClS+rmw/HFz81l"
			+ "RCFcku8HIIIcFxPQylRgSxHI8PMVJEodBsQSRvOzGtzWTfhTfG0Y0sgZPt75hKjd"
			+ "J1rTo4264AG3YzlxHlmqrrQxxmYEKEnNplmNVga2bXvPICZ2VPbl9w/52mlSobg3"
			+ "6VPKMzdlhTUgFsADPMaG2Pf3GqOa2GTSupzgTyz/54LyF6gCbXcAknRFgH5eyAv5"
			+ "tPqV9CsrAgMBAAECggEAAXIHCxABgfCLjRRSql/EEuh+E+29XPwSjSGmhkGlaUPe"
			+ "HWDa13jXrSJ+IkdSjflcIn/zklF4BPS+vJxFTc01s57ug2UGWoi5EdzNs6Qhhvc4"
			+ "vBh3v6VU96Z0EdTz17wLROsWqyufoYg3+hKQocMQySOqVmiPn2YHPuWD2grIVDZ9"
			+ "68mC1FykGEcv9De0m6yVEsfZDXNUxm3cz1758iBqakvyOVxGsI+V+e7/iSxJiwIB"
			+ "6f3NSGQVtEsqwyhnl6dZRYDtnq5iUiwUOshl5Z2CYBfBcyTpMKC2RuHp3u9THHpc"
			+ "3TFE4I1Li5HkiFy+ai6QKl2M85ce3GCXmjyw7n2vmQKBgQDTHihNX6uu6YlVFcRa"
			+ "XhhGigyLrIP8LbLd4r/dKMXuGa8XqkLPlDtXelh2n565Lo5DPGlpANi3Jp495Hzn"
			+ "bL+YnHJs7boVOGtORB0XHUbiMaTlT85snpvVjwFngHvY/ZxtXclpsnX563AvzCyl"
			+ "amrKEhV17BFZbRQOTEL1UNp57QKBgQDO98AyfVgs1tCdtLOsFSFiWrAsJJimBS7b"
			+ "ec+W/UPGDAl3hFzQzJ2SUvF4EneatYVOEDdHLUzVnT7XXeA/eMZL56N8BTSffh8q"
			+ "XK0E21K8tW4hRbze6CIjbsJ1x7ZLZaoM4Ub2YAvkulF0PcasX0kWl+bv/DnkI09x"
			+ "/n3vgbO2dwKBgQCoAnj6UmeztEDJiKARdo6FHHmtciY7Ozb8Y+Zin38c5C22fJXc"
			+ "0k+DZ2cdSBwtrQIkOeB9YuIUp1QJV1ubZKz5S4+4ZlvPZW3oBEbOTUtK2U0r/J3/"
			+ "TR4hD0SD1Pk6j2G8m4Wdaxt+P8KxFyB0p8LCey++/5Yy/56VXlVvGuAzZQKBgQCU"
			+ "VfcXeMTIplGwpkGcFSzvLCZWDQim/NH/lYdWJUD84cWrNl+7ett4cyADueClLnJT"
			+ "Z8Xmqq4F8ASJIQxHEY21+1gt3CFCKoe1ueR7taHQBIzhJfVfIarOEGUpOzEJSt0d"
			+ "DBzrGh2MGomksV4CTuy4V7i5yeHIBBK9lfO2xBQEswKBgQCIzYO53kTFl6YGjmWO"
			+ "qUJsT+5WegR4GdxtqYpQGPC1RmU7ig1TZzen+X3xB+lIHqgA1HvTr6M+tPkmnMwU"
			+ "iARPOgjXY0zmsStXaHQYKruT3EjZRs2GnmVpVOAj1asqi+/2t0NgLgB5gPLYMXS+" + "BGf01OehvUt5Ge+OChDBXSW5Bw==";

	// 公钥
	public static String publicKeyStr = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqq6VB2cfzUkbCo0HPYv4"
			+ "Ahs1PUN6kX+OKZtJTC/RXyX/pvZqPanQBLDJdzdGP51XHRlm/FQa8vncpZijUeFT"
			+ "3xTRkCllvDrb1JU/ZdyPfzE3f2bKFbdnUHQbzqe/vYawpUvq5sPxxc/NZUQhXJLv"
			+ "ByCCHBcT0MpUYEsRyPDzFSRKHQbEEkbzsxrc1k34U3xtGNLIGT7e+YSo3Sda06ON"
			+ "uuABt2M5cR5Zqq60McZmBChJzaZZjVYGtm17zyAmdlT25fcP+dppUqG4N+lTyjM3"
			+ "ZYU1IBbAAzzGhtj39xqjmthk0rqc4E8s/+eC8heoAm13AJJ0RYB+XsgL+bT6lfQr" + "KwIDAQAB";

	public static void main(String[] args) {
		// 读取私钥
		PrivateKey privateKey = RSAUtil.getPrivateKey(privateKeyStr);
		// 读取公钥
		PublicKey publicKey = RSAUtil.getPublicKey(publicKeyStr);
		String param = "test";
		byte[] paramBytes = param.getBytes(StandardCharsets.UTF_8);
		// 私钥加密 公钥解密
		// 私钥加密 to base64 str
		String encryptBase64Str = RSAUtil.encryptBase64ByPrivateKey(privateKey, paramBytes);
		System.out.println("私钥编码Base64Str==>" + encryptBase64Str);
		String decryptStr = RSAUtil.decryptStrToStrByPublicKey(publicKey, encryptBase64Str);
		System.out.println("公钥解码Base64默认字符集Str==>" + decryptStr);
		String encryptHexStrByPrivateKey = RSAUtil.encryptHexStrByPrivateKey(privateKey, paramBytes);
		System.out.println("私钥编码HexStr==>" + encryptHexStrByPrivateKey);
		String decryptHexStr = RSAUtil.decryptStrToStrByPublicKey(publicKey, encryptHexStrByPrivateKey);
		System.out.println("公钥解码Hex默认字符集Str==>" + decryptHexStr);
		// 公钥加密 私钥解密
		String encryptBase64ByPublicKey = RSAUtil.encryptBase64ByPublicKey(publicKey, paramBytes);
		System.out.println("公钥编码Base64Str==>" + encryptBase64ByPublicKey);
		String decryptBase64StrToStrByPrivateKey = RSAUtil.decryptStrToStrByPrivateKey(privateKey,
				encryptBase64ByPublicKey);
		System.out.println("私钥解码Base64默认字符集Str==>" + decryptBase64StrToStrByPrivateKey);
		String encryptHexStrByPublicKey = RSAUtil.encryptHexStrByPublicKey(publicKey, paramBytes);
		System.out.println("公钥编码HexStr==>" + encryptHexStrByPublicKey);
		String decryptHexStrToStrByPrivateKey = RSAUtil.decryptStrToStrByPrivateKey(privateKey,
				encryptHexStrByPublicKey);
		System.out.println("私钥解码Hex默认字符集Str==>" + decryptHexStrToStrByPrivateKey);
		// 签名 验签
		String signBase64Str = RSAUtil.signToBase64Str(privateKey, paramBytes);
		System.out.println("签名数据Base64Str==>" + signBase64Str);
		boolean verifyBase64 = RSAUtil.verify(publicKey, paramBytes, signBase64Str);
		System.out.println("效验签名Base64Str==>" + verifyBase64);

		String signToHexStr = RSAUtil.signToHexStr(privateKey, paramBytes);
		System.out.println("签名数据HexStr==>" + signToHexStr);

		boolean verifyHex = RSAUtil.verify(publicKey, paramBytes, signToHexStr);
		System.out.println("效验签名HexStr==>" + verifyHex);
		// 错误示范 使用错误签名
		boolean verifyError = RSAUtil.verify(publicKey, "error".getBytes(StandardCharsets.UTF_8), signBase64Str);
		System.out.println("错误示范效验签名HexStr==>" + verifyError);
	}

}