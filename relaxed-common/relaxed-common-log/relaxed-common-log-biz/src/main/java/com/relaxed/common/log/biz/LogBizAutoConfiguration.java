package com.relaxed.common.log.biz;

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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Yakir
 * @Topic OperatiorConfig
 * @Description
 * @date 2023/12/14 14:58
 * @Version 1.0
 */
@Configuration(proxyBeanMethods = false)
public class LogBizAutoConfiguration {

	/**
	 * 字段处理器
	 * @author yakir
	 * @date 2021/12/15 15:53
	 * @return com.relaxed.common.log.action.handler.FieldHandler
	 */
	@Bean
	@ConditionalOnMissingBean
	public IFieldHandler fieldHandler() {
		return new DefaultFieldHandler();
	}

	/**
	 * 数据处理器
	 * @author yakir
	 * @date 2021/12/15 15:54
	 * @param fieldHandler
	 * @return com.relaxed.common.log.action.handler.DataHandler
	 */
	@Bean
	@ConditionalOnMissingBean
	public IDataHandler dataHandler(IFieldHandler fieldHandler) {
		return new DefaultDataHandler(fieldHandler);
	}

	/**
	 * 日志记录上报器方法
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean
	public ILogRecordService logRecordService() {
		return new DefaultLogRecordService();
	}

	/**
	 * 操作者id提取器
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean
	public IOperatorGetService operatorGetService() {
		return new DefaultOperatorGetServiceImpl();
	}

	/**
	 * 业务日志增强器
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean
	public ILogBizEnhance logBizEnhance() {
		return new DefaultLogBizEnhance();
	}

	/**
	 * 正则spel 日志解析器
	 * @param iOperatorGetService
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean
	public ILogParse regxLogParse(IOperatorGetService iOperatorGetService, ILogBizEnhance logBizEnhance,
			IDataHandler dataHandler) {
		return new LogRegxSpelParse(iOperatorGetService, logBizEnhance, dataHandler);
	}

	/**
	 * log 切面
	 * @param logParse
	 * @param logRecordService
	 * @return
	 */

	@Bean
	public LogOperatorAdvice logOperatorAdvice(ILogParse logParse, ILogRecordService logRecordService) {
		return new LogOperatorAdvice(logParse, logRecordService);
	}

	@Bean
	public LogOperatorAdvisor exceptionAnnotationAdvisor(LogOperatorAdvice logOperatorAdvice) {
		LogOperatorAdvisor advisor = new LogOperatorAdvisor(logOperatorAdvice, BizLog.class);
		return advisor;
	}

}
