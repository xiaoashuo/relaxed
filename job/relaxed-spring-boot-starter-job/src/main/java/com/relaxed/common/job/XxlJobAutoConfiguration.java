package com.relaxed.common.job;

import com.relaxed.common.job.properties.XxlExecutorProperties;
import com.relaxed.common.job.properties.XxlJobProperties;
import com.relaxed.common.job.trace.TraceXxlJobSpringExecutor;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lengleng
 * @date 2019-09-18
 * <p>
 * xxl 初始化
 */
@Slf4j
@EnableConfigurationProperties(XxlJobProperties.class)
public class XxlJobAutoConfiguration {

	@Bean
	public TraceXxlJobSpringExecutor xxlJobSpringExecutor(XxlJobProperties xxlJobProperties) {
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
		return xxlJobSpringExecutor;
	}

}
