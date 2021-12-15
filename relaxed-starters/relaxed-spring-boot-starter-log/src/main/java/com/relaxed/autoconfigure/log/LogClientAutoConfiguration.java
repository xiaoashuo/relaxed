package com.relaxed.autoconfigure.log;

import com.relaxed.common.log.action.properties.LogClientProperties;
import com.relaxed.common.log.action.handler.DataHandler;
import com.relaxed.common.log.action.handler.FieldHandler;
import com.relaxed.common.log.action.handler.RecordHandler;
import com.relaxed.common.log.action.handler.impl.DefaultDataHandler;
import com.relaxed.common.log.action.handler.impl.DefaultFieldHandler;
import com.relaxed.common.log.action.handler.impl.DefaultRecordHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author Yakir
 * @Topic LogClientAutoConfiguration
 * @Description
 * @date 2021/12/14 16:48
 * @Version 1.0
 */
@EnableConfigurationProperties(value = LogClientProperties.class)
public class LogClientAutoConfiguration {

	/**
	 * 字段处理器
	 * @author yakir
	 * @date 2021/12/15 15:53
	 * @return com.relaxed.common.log.action.handler.FieldHandler
	 */
	@Bean
	@ConditionalOnMissingBean
	public FieldHandler fieldHandler() {
		return new DefaultFieldHandler();
	}

	/**
	 * 日志记录上报
	 * @author yakir
	 * @date 2021/12/15 15:53
	 * @return com.relaxed.common.log.action.handler.RecordHandler
	 */
	@Bean
	@ConditionalOnMissingBean
	public RecordHandler recordHandler() {
		return new DefaultRecordHandler();
	}

	/**
	 * 数据处理器
	 * @author yakir
	 * @date 2021/12/15 15:54
	 * @param recordHandler
	 * @param fieldHandler
	 * @return com.relaxed.common.log.action.handler.DataHandler
	 */
	@Bean
	@ConditionalOnMissingBean
	public DataHandler dataHandler(RecordHandler recordHandler, FieldHandler fieldHandler) {
		return new DefaultDataHandler(recordHandler, fieldHandler);
	}

}
