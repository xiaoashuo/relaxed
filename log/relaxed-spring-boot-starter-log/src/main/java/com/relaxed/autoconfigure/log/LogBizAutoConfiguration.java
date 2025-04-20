package com.relaxed.autoconfigure.log;

import com.relaxed.autoconfigure.log.properties.LogProperties;
import com.relaxed.common.log.biz.annotation.BizLog;
import com.relaxed.common.log.biz.aspect.LogOperatorAdvice;
import com.relaxed.common.log.biz.aspect.LogOperatorAdvisor;
import com.relaxed.common.log.biz.service.IDataHandler;
import com.relaxed.common.log.biz.service.IFieldHandler;
import com.relaxed.common.log.biz.service.ILogBizEnhance;
import com.relaxed.common.log.biz.service.ILogParse;
import com.relaxed.common.log.biz.service.ILogRecordService;
import com.relaxed.common.log.biz.service.IOperatorGetService;
import com.relaxed.common.log.biz.service.impl.DefaultDataHandler;
import com.relaxed.common.log.biz.service.impl.DefaultFieldHandler;
import com.relaxed.common.log.biz.service.impl.DefaultLogBizEnhance;
import com.relaxed.common.log.biz.service.impl.DefaultLogRecordService;
import com.relaxed.common.log.biz.service.impl.DefaultOperatorGetServiceImpl;
import com.relaxed.common.log.biz.service.impl.LogRegxSpelParse;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 业务日志自动配置类。 用于配置业务日志相关的组件和服务。 主要功能包括： 1. 配置字段处理器 2. 配置数据处理器 3. 配置日志记录服务 4. 配置操作者提取服务 5.
 * 配置业务日志增强器 6. 配置日志解析器 7. 配置日志切面
 *
 * @author Yakir
 * @since 1.0
 */
@Configuration
@ConditionalOnProperty(prefix = LogProperties.Biz.PREFIX, name = "enabled", havingValue = "true")
public class LogBizAutoConfiguration {

	/**
	 * 注册默认的字段处理器。 用于处理日志记录中的字段转换和格式化。
	 * @return 字段处理器实例
	 */
	@Bean
	@ConditionalOnMissingBean
	public IFieldHandler fieldHandler() {
		return new DefaultFieldHandler();
	}

	/**
	 * 注册默认的数据处理器。 用于处理日志记录中的数据转换和格式化。
	 * @param fieldHandler 字段处理器
	 * @return 数据处理器实例
	 */
	@Bean
	@ConditionalOnMissingBean
	public IDataHandler dataHandler(IFieldHandler fieldHandler) {
		return new DefaultDataHandler(fieldHandler);
	}

	/**
	 * 注册默认的日志记录服务。 用于处理日志的存储和上报。
	 * @return 日志记录服务实例
	 */
	@Bean
	@ConditionalOnMissingBean
	public ILogRecordService logRecordService() {
		return new DefaultLogRecordService();
	}

	/**
	 * 注册默认的操作者提取服务。 用于从当前上下文中提取操作者信息。
	 * @return 操作者提取服务实例
	 */
	@Bean
	@ConditionalOnMissingBean
	public IOperatorGetService operatorGetService() {
		return new DefaultOperatorGetServiceImpl();
	}

	/**
	 * 注册默认的业务日志增强器。 用于增强业务日志的功能。
	 * @return 业务日志增强器实例
	 */
	@Bean
	@ConditionalOnMissingBean
	public ILogBizEnhance logBizEnhance() {
		return new DefaultLogBizEnhance();
	}

	/**
	 * 注册默认的日志解析器。 使用正则表达式和SpEL表达式解析日志内容。
	 * @param iOperatorGetService 操作者提取服务
	 * @param logBizEnhance 业务日志增强器
	 * @param dataHandler 数据处理器
	 * @return 日志解析器实例
	 */
	@Bean
	@ConditionalOnMissingBean
	public ILogParse regxLogParse(IOperatorGetService iOperatorGetService, ILogBizEnhance logBizEnhance,
			IDataHandler dataHandler) {
		return new LogRegxSpelParse(iOperatorGetService, logBizEnhance, dataHandler);
	}

	/**
	 * 注册日志操作通知。 用于处理日志记录的具体逻辑。
	 * @param logParse 日志解析器
	 * @param logRecordService 日志记录服务
	 * @return 日志操作通知实例
	 */
	@Bean
	public LogOperatorAdvice logOperatorAdvice(ILogParse logParse, ILogRecordService logRecordService) {
		return new LogOperatorAdvice(logParse, logRecordService);
	}

	/**
	 * 注册日志操作顾问。 用于配置日志切面的切入点。
	 * @param logOperatorAdvice 日志操作通知
	 * @return 日志操作顾问实例
	 */
	@Bean
	public LogOperatorAdvisor logOperatorAdvisor(LogOperatorAdvice logOperatorAdvice) {
		LogOperatorAdvisor advisor = new LogOperatorAdvisor(logOperatorAdvice, BizLog.class);
		return advisor;
	}

}
