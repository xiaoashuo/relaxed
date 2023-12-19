package com.relaxed.common.log.operation;

import com.relaxed.common.log.operation.annotation.BizLog;
import com.relaxed.common.log.operation.aspect.LogOperatorAdvice;
import com.relaxed.common.log.operation.aspect.LogOperatorAdvisor;
import com.relaxed.common.log.operation.service.ILogParse;
import com.relaxed.common.log.operation.service.ILogRecordService;
import com.relaxed.common.log.operation.service.impl.DefaultLogRecordService;
import com.relaxed.common.log.operation.service.impl.LogRegxSpelParse;
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



	@Bean
	public ILogRecordService logRecordService(){
		return new DefaultLogRecordService();
	}
//	@Bean
//	public ILogParse logParse(){
//		return new DefaultLogSpelParse();
//	}

	@Bean
	public ILogParse regxLogParse(){
		return new LogRegxSpelParse();
	}
	@Bean
	public LogOperatorAdvice logOperatorAdvice(ILogParse logParse,ILogRecordService logRecordService) {
		return new LogOperatorAdvice(logParse,logRecordService);
	}

	@Bean
	public LogOperatorAdvisor exceptionAnnotationAdvisor(LogOperatorAdvice logOperatorAdvice) {
		LogOperatorAdvisor advisor = new LogOperatorAdvisor(logOperatorAdvice, BizLog.class);
		return advisor;
	}

}
