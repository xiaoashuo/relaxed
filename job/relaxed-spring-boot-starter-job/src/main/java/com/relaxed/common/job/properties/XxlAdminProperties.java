package com.relaxed.common.job.properties;

import lombok.Data;

/**
 * XXL-Job 管理端配置属性类。 用于配置 XXL-Job 调度中心的地址，支持集群部署。 执行器使用该地址进行心跳注册和任务结果回调。
 *
 * @author Yakir
 * @since 1.0
 */
@Data
public class XxlAdminProperties {

	/**
	 * 调度中心部署地址。 支持集群部署，多个地址用逗号分隔。 为空则关闭自动注册功能。 默认值：http://127.0.0.1:8080/xxl-job-admin
	 */
	private String addresses = "http://127.0.0.1:8080/xxl-job-admin";

}
