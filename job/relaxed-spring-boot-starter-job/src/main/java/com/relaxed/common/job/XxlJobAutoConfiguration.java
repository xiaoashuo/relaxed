package com.relaxed.common.job;

import com.relaxed.common.job.log.DefaultLogBackAppenderProvider;
import com.relaxed.common.job.log.LogbackAppenderProvider;
import com.relaxed.common.job.properties.XxlExecutorProperties;
import com.relaxed.common.job.properties.XxlJobProperties;
import com.relaxed.common.job.properties.XxlLogProperties;
import com.relaxed.common.job.trace.TraceXxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.Nullable;

/**
 * XXL-Job 自动配置类。 用于配置 XXL-Job 执行器和日志同步功能，主要功能包括： 1. 配置执行器参数 2. 配置日志同步 3. 支持自定义日志提供者
 *
 * @author Yakir
 * @since 1.0
 */
@Slf4j
@EnableConfigurationProperties(XxlJobProperties.class)
public class XxlJobAutoConfiguration {

	/**
	 * 创建默认的日志追加器提供者。 当配置文件中启用日志同步且未提供自定义提供者时使用。
	 * @return 日志追加器提供者
	 */
	@ConditionalOnProperty(prefix = "xxl.job.log", name = "enabled", havingValue = "true", matchIfMissing = true)
	@ConditionalOnMissingBean
	@Bean
	public LogbackAppenderProvider logbackAppenderProvider() {
		return new DefaultLogBackAppenderProvider();
	}

	/**
	 * 创建 XXL-Job 执行器。 配置执行器参数并初始化日志同步功能。
	 * @param xxlJobProperties XXL-Job 配置属性
	 * @param logbackAppenderProvider 日志追加器提供者
	 * @return XXL-Job 执行器
	 */
	@Bean
	public TraceXxlJobSpringExecutor xxlJobSpringExecutor(XxlJobProperties xxlJobProperties,
			@Nullable LogbackAppenderProvider logbackAppenderProvider) {
		log.info(">>>>>>>>>>> xxl-job config init.");
		TraceXxlJobSpringExecutor xxlJobSpringExecutor = new TraceXxlJobSpringExecutor();
		xxlJobSpringExecutor.setAdminAddresses(xxlJobProperties.getAdmin().getAddresses());
		XxlExecutorProperties executorProperties = xxlJobProperties.getExecutor();
		xxlJobSpringExecutor.setAppname(executorProperties.getAppname());
		xxlJobSpringExecutor.setIp(executorProperties.getIp());
		xxlJobSpringExecutor.setPort(executorProperties.getPort());
		xxlJobSpringExecutor.setAccessToken(executorProperties.getAccessToken());
		xxlJobSpringExecutor.setLogPath(executorProperties.getLogPath());
		xxlJobSpringExecutor.setLogRetentionDays(executorProperties.getLogRetentionDays());
		xxlJobSpringExecutor.setAddress(executorProperties.getAddress());
		XxlLogProperties logProperties = xxlJobProperties.getLog();
		if (logProperties.isEnabled()) {
			if (logbackAppenderProvider == null) {
				throw new IllegalArgumentException("XXL-LOG日志同步配置已启用,但Provider未提供,请检查");
			}
			logbackAppenderProvider.addAppender(logProperties);
			log.info("log同步xxl-console success");
		}
		return xxlJobSpringExecutor;
	}

}
