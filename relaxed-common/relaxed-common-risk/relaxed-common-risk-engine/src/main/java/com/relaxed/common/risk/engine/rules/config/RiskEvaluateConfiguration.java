package com.relaxed.common.risk.engine.rules.config;

import com.relaxed.common.risk.engine.config.EngineProperties;
import com.relaxed.common.risk.engine.rules.RiskEvaluateChain;
import com.relaxed.common.risk.engine.rules.extractor.FieldExtractor;
import com.relaxed.common.risk.engine.rules.extractor.SimpleFieldExtractor;
import com.relaxed.common.risk.engine.rules.machine.Estimator;
import com.relaxed.common.risk.engine.rules.machine.EstimatorHolder;
import com.relaxed.common.risk.engine.rules.machine.impl.TensorDnnEstimator;
import com.relaxed.common.risk.engine.rules.script.RuleScriptHandler;
import com.relaxed.common.risk.engine.rules.script.groovy.GroovyScriptHandler;
import com.relaxed.common.risk.engine.rules.statistics.AggregateExecutor;
import com.relaxed.common.risk.engine.rules.statistics.AggregateInvoker;
import com.relaxed.common.risk.engine.rules.statistics.executor.AggregateExecutorDiscover;
import com.relaxed.common.risk.engine.rules.statistics.invoker.SimpleAggregateInvoker;
import com.relaxed.common.risk.engine.rules.statistics.provider.AggregateFunctionProvider;
import com.relaxed.common.risk.engine.rules.statistics.provider.SimpleAggregateFunctionProvider;
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
		RiskEvaluateChain.Builder riskEvaluateChainBuilder = RiskEvaluateChain.builder();
		for (RiskEvaluateBuilderCustomizer riskEvaluateBuilderCustomizer : customizerList) {
			riskEvaluateBuilderCustomizer.customize(riskEvaluateChainBuilder);
		}
		return riskEvaluateChainBuilder.build();
	}

	/**
	 * 规则脚本处理器
	 * @author yakir
	 * @date 2021/8/30 12:42
	 * @return com.relaxed.common.risk.engine.rules.script.RuleScriptHandler
	 */
	@Bean
	@ConditionalOnMissingBean
	public RuleScriptHandler ruleScriptHandler() {
		return new GroovyScriptHandler();
	}

	/**
	 * 字段提取器
	 * @author yakir
	 * @date 2021/8/30 13:37
	 * @return com.relaxed.common.risk.engine.rules.extractor.FieldExtractor
	 */
	@Bean
	@ConditionalOnMissingBean
	public FieldExtractor fieldExtractor() {
		return new SimpleFieldExtractor();
	}

	/**
	 * 聚合执行器发现者
	 * @author yakir
	 * @date 2021/8/30 15:11
	 * @param aggregateExecutors
	 * @return com.relaxed.common.risk.engine.rules.statistics.executor.AggregateExecutorDiscover
	 */
	@Bean
	@ConditionalOnMissingBean
	public AggregateExecutorDiscover aggregateExecutorDiscover(List<AggregateExecutor> aggregateExecutors) {
		AggregateExecutorDiscover aggregateExecutorDiscover = new AggregateExecutorDiscover();
		for (AggregateExecutor aggregateExecutor : aggregateExecutors) {
			aggregateExecutorDiscover.putAggregateExecutor(aggregateExecutor);
		}
		return aggregateExecutorDiscover;
	}

	/**
	 * 聚合调用者
	 * @author yakir
	 * @date 2021/8/30 15:45
	 * @param aggregateExecutorDiscover
	 * @return com.relaxed.common.risk.engine.rules.statistics.AggregateInvoker
	 */
	@Bean
	@ConditionalOnMissingBean
	public AggregateInvoker aggregateInvoker(AggregateExecutorDiscover aggregateExecutorDiscover) {
		return new SimpleAggregateInvoker(aggregateExecutorDiscover);
	}

	/**
	 * 聚合函数提供者
	 * @author yakir
	 * @date 2021/8/30 15:57
	 * @return com.relaxed.common.risk.engine.rules.statistics.provider.AggregateFunctionProvider
	 */
	@Bean
	@ConditionalOnMissingBean
	public AggregateFunctionProvider aggregateFunctionProvider() {
		return new SimpleAggregateFunctionProvider();
	}

	/**
	 * 机器学习类型持有者
	 * @author yakir
	 * @date 2021/8/31 9:48
	 * @return com.relaxed.common.risk.engine.rules.machine.EstimatorHolder
	 */
	@Bean
	@ConditionalOnMissingBean
	public EstimatorHolder estimatorHolder(List<Estimator> estimators) {
		EstimatorHolder estimatorHolder = new EstimatorHolder();
		for (Estimator estimator : estimators) {
			estimatorHolder.put(estimator);
		}
		return estimatorHolder;
	}

	/**
	 * TensorFlow 机器学习
	 * @author yakir
	 * @date 2021/8/31 10:34
	 * @return com.relaxed.common.risk.engine.rules.machine.Estimator
	 */
	@ConditionalOnMissingBean
	@Bean
	public Estimator tensorDnnEstimator() {
		return new TensorDnnEstimator(EngineProperties.getMachineWorkDir());
	}

}
