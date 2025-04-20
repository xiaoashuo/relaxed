package com.relaxed.common.ip;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for IP2Region functionality. This class binds to configuration
 * properties with the prefix "relaxed.ip" and provides settings for IP address lookup
 * functionality using the IP2Region database.
 *
 * @author Yakir
 * @since 1.0
 */
@Data
@ConfigurationProperties(prefix = Ip2RegionProperties.PREFIX)
public class Ip2RegionProperties {

	/**
	 * Configuration prefix for IP2Region properties.
	 */
	public static final String PREFIX = "relaxed.ip";

	/**
	 * Flag to enable/disable IP address lookup functionality. When set to true, the
	 * IP2Region functionality will be activated. Defaults to false.
	 */
	private boolean enabled = false;

	/**
	 * Path to the IP2Region database file. This file contains the IP address mapping data
	 * used for location lookups. Defaults to "ip2region.db".
	 */
	private String dbFile = "ip2region.db";

}
