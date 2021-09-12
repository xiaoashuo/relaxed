package com.relaxed.common.ip;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Yakir
 * @Topic Ip2RegionProperties
 * @Description
 * @date 2021/9/1 11:05
 * @Version 1.0
 */
@Data
@ConfigurationProperties(prefix = Ip2RegionProperties.PREFIX)
public class Ip2RegionProperties {

	public static final String PREFIX = "relaxed.ip";

	/**
	 * 是否启用
	 */
	private boolean enabled = false;

	/**
	 * db 数据文件未知
	 */
	private String dbFile = "ip2region.db";

}
