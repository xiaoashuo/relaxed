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
 * @author lengleng
 * @date 2019-09-18
 * <p>
 * xxl 初始化
 */
@Slf4j
@EnableConfigurationProperties(XxlJobProperties.class)
public class XxlJobAutoConfiguration {

	@ConditionalOnProperty(prefix = "xxl.job.log", name = "enabled", havingValue = "true", matchIfMissing = true)
	@ConditionalOnMissingBean
	@Bean
	public LogbackAppenderProvider logbackAppenderProvider() {
		return new DefaultLogBackAppenderProvider();
	}

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
