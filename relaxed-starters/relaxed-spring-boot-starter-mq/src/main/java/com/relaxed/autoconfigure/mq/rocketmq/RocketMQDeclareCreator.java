package com.relaxed.autoconfigure.mq.rocketmq;

import com.relaxed.autoconfigure.mq.core.creator.MQDeclareCreator;
import com.relaxed.autoconfigure.mq.core.domain.AbstractMQ;
import com.relaxed.autoconfigure.mq.core.domain.MQMeta;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.BrokerConfig;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.core.env.Environment;

/**
 * @author Yakir
 * @Topic RocketMQDeclareCreator
 * @Description
 * @date 2021/12/28 9:15
 * @Version 1.0
 */
@Slf4j
public class RocketMQDeclareCreator implements MQDeclareCreator {

	/**
	 * brokerConfig
	 */
	private BrokerConfig brokerConfig;

	@Override
	public void declareQueueExchange(AbstractMQ amq, BeanDefinitionRegistry beanDefinitionRegistry,
			Environment environment, BeanNameGenerator nameGenerator) {
		// 初始化broker
		initBrokerConfig(environment);
		MQMeta mqMeta = amq.getMQMeta();
		MQMeta.ExchangeMeta exchangeMeta = mqMeta.getExchangeMeta();
		// 交换机名称类比为topic
		String exchangeName = exchangeMeta.getExchangeName();
		RocketMQTopicUtil.initTopic(exchangeName, brokerConfig.getNamesrvAddr(), brokerConfig.getBrokerName());
	}

	void initBrokerConfig(Environment environment) {
		if (brokerConfig != null) {
			return;
		}
		String nameServer = environment.getProperty("rocketmq.name-server", String.class);
		log.debug("rocketmq.nameServer = {}", nameServer);
		if (nameServer == null) {
			log.warn(
					"The necessary spring property 'rocketmq.name-server' is not defined, all rockertmq beans creation are skipped!");
		}
		BrokerConfig brokerConfig = new BrokerConfig();
		brokerConfig.setNamesrvAddr(nameServer);
		brokerConfig.setBrokerIP1("127.0.0.1");
		this.brokerConfig = brokerConfig;
	}

}
