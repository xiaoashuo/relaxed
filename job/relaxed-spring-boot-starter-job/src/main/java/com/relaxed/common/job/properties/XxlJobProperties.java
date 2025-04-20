package com.relaxed.common.job.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * XXL-Job 配置属性类。 用于配置 XXL-Job 的日志、管理端和执行器相关属性。 通过 @ConfigurationProperties 注解绑定配置文件中的
 * xxl.job 前缀配置。
 *
 * @author Yakir
 * @since 1.0
 */
@Data
@ConfigurationProperties(prefix = "xxl.job")
public class XxlJobProperties {

	/**
	 * 日志配置属性
	 */
	@NestedConfigurationProperty
	private XxlLogProperties log = new XxlLogProperties();

	/**
	 * 管理端配置属性
	 */
	@NestedConfigurationProperty
	private XxlAdminProperties admin = new XxlAdminProperties();

	/**
	 * 执行器配置属性
	 */
	@NestedConfigurationProperty
	private XxlExecutorProperties executor = new XxlExecutorProperties();

}
