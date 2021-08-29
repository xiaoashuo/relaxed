package com.relaxed.common.risk.engine.rules.config;

import com.relaxed.common.risk.engine.rules.RiskEvaluateChain;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * @author Yakir
 * @Topic RiskEvaluateConfiguration
 * @Description
 * @date 2021/8/29 18:05
 * @Version 1.0
 */
@Configuration(proxyBeanMethods = false)
public class RiskEvaluateConfiguration {

	/**
	 * 风控过滤器链
	 * @author yakir
	 * @date 2021/8/29 18:09
	 * @param customizerList
	 * @return com.relaxed.common.risk.engine.rules.RiskEvaluateChain
	 */
	@Bean
	@Scope("prototype")
	@ConditionalOnMissingBean
	public RiskEvaluateChain riskEvaluateChain(@Nullable List<RiskEvaluateBuilderCustomizer> customizerList) {
		RiskEvaluateChain riskEvaluateChain = new RiskEvaluateChain();
		for (RiskEvaluateBuilderCustomizer riskEvaluateBuilderCustomizer : customizerList) {
			riskEvaluateBuilderCustomizer.customize(riskEvaluateChain);
		}
		return riskEvaluateChain;
	}

}
