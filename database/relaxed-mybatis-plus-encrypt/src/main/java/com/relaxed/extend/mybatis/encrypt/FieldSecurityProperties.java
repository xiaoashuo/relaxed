package com.relaxed.extend.mybatis.encrypt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 字段安全配置属性类 用于配置字段加密相关的属性，支持通过配置文件进行自定义 包含默认加密算法和AES加密的具体配置
 *
 * @author Yakir
 */
@ConfigurationProperties(FieldSecurityProperties.PREFIX)
@Data
public class FieldSecurityProperties {

	public static final String PREFIX = "relaxed.mybatis.sec";

	private String defSec = AES.SEC_FLAG;

	private AES aes = new AES();

	@Data
	public static class AES {

		public static final String SEC_FLAG = "AES";

		private String key = "c6eb7f4a1df78536";

	}

}
