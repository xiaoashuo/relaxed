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
 * 线程池监控自动配置类，负责配置线程池监控相关的组件。 包括告警服务、监控任务和监控增强器的自动配置。
 *
 * @author Yakir
 * @since 1.0.0
 */
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(ThreadPoolMonitorProperties.class)
public class ThreadPoolMonitorAutoConfiguration {

	/**
	 * 配置默认的告警服务实现。 当容器中没有 AlertService 的实现时，使用此默认实现。
	 * @return 默认的告警服务实例
	 */
	@ConditionalOnMissingBean
	@Bean
	public AlertService defaultAlertService() {
		return (finalMsg, channels) -> {
			// log.info("线程池告警,通知渠道:{},消息:{}",channels,finalMsg);
		};
	}

	/**
	 * 配置线程池监控任务。 只有当配置属性 relaxed.thread-pool.monitor.monitorEnabled 为 true 时才创建此 Bean。
	 * @param alertService 告警服务
	 * @param poolMonitorProperties 线程池监控配置属性
	 * @return 线程池监控任务实例
	 */
	@Bean
	@ConditionalOnProperty(prefix = "relaxed.thread-pool.monitor", name = "monitorEnabled", havingValue = "true")
	public ThreadPoolTaskMonitor threadPoolTaskMonitor(AlertService alertService,
			ThreadPoolMonitorProperties poolMonitorProperties) {
		return new ThreadPoolTaskMonitor(alertService, poolMonitorProperties);
	}

	/**
	 * 配置线程池监控增强器。 只有当配置属性 relaxed.thread-pool.monitor.adjustPoolNumEnabled 为 true 时才创建此
	 * Bean。 用于增强线程池的动态调整能力。
	 * @return 线程池监控增强器实例
	 */
	@Bean
	@ConditionalOnProperty(prefix = "relaxed.thread-pool.monitor", name = "adjustPoolNumEnabled", havingValue = "true")
	public ThreadPoolMonitorEnhancer threadPoolMonitorEnhancer() {
		return new ThreadPoolMonitorEnhancer();
	}

}
