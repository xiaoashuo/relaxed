package com.relaxed.common.job.properties;

import lombok.Data;

/**
 * XXL-Job 执行器配置属性类。 用于配置 XXL-Job 执行器的各项参数，包括： 1. 执行器标识和地址 2. 网络配置 3. 安全认证 4. 日志配置
 *
 * @author Yakir
 * @since 1.0
 */
@Data
public class XxlExecutorProperties {

	/**
	 * 执行器应用名称。 用于执行器心跳注册分组，为空则关闭自动注册。 默认值：xxl-job-executor
	 */
	private String appname = "xxl-job-executor";

	/**
	 * 执行器注册地址。 优先使用该配置作为注册地址，为空时使用内嵌服务 "IP:PORT" 作为注册地址。 支持容器类型执行器动态IP和动态映射端口。
	 */
	private String address;

	/**
	 * 执行器IP地址。 默认为空表示自动获取IP，多网卡时可手动设置指定IP。 该IP不会绑定Host，仅用于通讯。
	 * 地址信息用于"执行器注册"和"调度中心请求并触发任务"。
	 */
	private String ip;

	/**
	 * 执行器端口号。 小于等于0则自动获取。 默认端口为9999，单机部署多个执行器时，注意要配置不同执行器端口。
	 */
	private Integer port = 0;

	/**
	 * 执行器通讯TOKEN。 非空时启用安全认证。
	 */
	private String accessToken;

	/**
	 * 执行器运行日志文件存储路径。 需要对该路径拥有读写权限。 为空则使用默认路径。 默认值：logs/applogs/xxl-job/jobhandler
	 */
	private String logPath = "logs/applogs/xxl-job/jobhandler";

	/**
	 * 执行器日志保存天数。 值大于3时生效，启用执行器Log文件定期清理功能。 默认值：30
	 */
	private Integer logRetentionDays = 30;

}
