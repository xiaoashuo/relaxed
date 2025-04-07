package com.relaxed.pool.monitor;

import com.relaxed.pool.monitor.monitor.ThreadPoolTaskMonitor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * @author Yakir
 * @Topic ThreadPoolMonitorAutoConfiguration
 * @Description
 * @date 2025/4/4 11:22
 * @Version 1.0
 */
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(ThreadPoolMonitorProperties.class)
public class ThreadPoolMonitorAutoConfiguration {

	/**
	 * 提醒服务
	 */
	@ConditionalOnMissingBean
	@Bean
	public AlertService defaultAlertService() {
		return (finalMsg, channels) -> {
			// log.info("线程池告警,通知渠道:{},消息:{}",channels,finalMsg);
		};
	}

	/**
	 * 线程池监控bean
	 * @param alertService
	 * @param poolMonitorProperties
	 */
	@Bean
	@ConditionalOnProperty(prefix = "relaxed.thread-pool.monitor", name = "monitorEnabled", havingValue = "true")
	public ThreadPoolTaskMonitor threadPoolTaskMonitor(AlertService alertService,
			ThreadPoolMonitorProperties poolMonitorProperties) {
		return new ThreadPoolTaskMonitor(alertService, poolMonitorProperties);
	}

	/**
	 * 线程池监控后置处理器 增强代理
	 */
	@Bean
	@ConditionalOnProperty(prefix = "relaxed.thread-pool.monitor", name = "adjustPoolNumEnabled", havingValue = "true")
	public ThreadPoolMonitorEnhancer threadPoolMonitorEnhancer() {
		return new ThreadPoolMonitorEnhancer();
	}

}
