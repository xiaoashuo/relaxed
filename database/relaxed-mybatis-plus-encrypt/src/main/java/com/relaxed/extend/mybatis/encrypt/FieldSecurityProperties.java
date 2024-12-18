package com.relaxed.extend.mybatis.encrypt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Yakir
 * @Topic FieldSecurityProperties
 * @Description
 * @date 2024/10/18 13:50
 * @Version 1.0
 */
@ConfigurationProperties(FieldSecurityProperties.PREFIX)
@Data
public class FieldSecurityProperties {

	public static final String PREFIX = "relaxed.mybatis.sec";

	/**
	 * 默认加密算法
	 */
	private String defSec = AES.SEC_FLAG;

	private AES aes = new AES();

	@Data
	public class AES {

		public static final String SEC_FLAG = "AES";

		private String key = "c6eb7f4a1df78536";

	}

}
