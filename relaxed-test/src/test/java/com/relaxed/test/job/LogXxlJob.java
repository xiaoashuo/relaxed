package com.relaxed.test.job;

import com.relaxed.common.job.properties.XxlJobProperties;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author Yakir
 * @Topic LogXxlJob
 * @Description
 * @date 2024/12/18 16:11
 * @Version 1.0
 */
@Slf4j
@Component
public class LogXxlJob {

	@XxlJob("testLogPrint")
	public ReturnT<String> testLogPrint(String params) {
		log.info("当前执行任务名称:testLogPrint");
		log.info("XXL-这是一条双appender日志");
		// 异常日志示例
		try {
			throw new RuntimeException("测试异常");
		}
		catch (Exception e) {
			log.error("XXL-发生错误", e);
		}
		return new ReturnT<>("执行成功");
	}

}
