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
 * RocketMQ声明创建器
 * <p>
 * 实现MQDeclareCreator接口，负责创建和初始化RocketMQ的队列和主题。 支持从环境配置中读取RocketMQ服务器配置。
 * </p>
 *
 * @author Yakir
 * @since 1.0
 */
@Slf4j
public class RocketMQDeclareCreator implements MQDeclareCreator {

	/**
	 * RocketMQ代理配置
	 * <p>
	 * 包含命名服务器地址、代理IP等配置信息
	 * </p>
	 */
	private BrokerConfig brokerConfig;

	/**
	 * 声明队列和交换器
	 * <p>
	 * 根据消息模型中的元数据创建RocketMQ主题。
	 * </p>
	 * @param amq 消息模型
	 * @param beanDefinitionRegistry Bean定义注册表
	 * @param environment 环境配置
	 * @param nameGenerator Bean名称生成器
	 */
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

	/**
	 * 初始化RocketMQ代理配置
	 * <p>
	 * 从环境配置中读取RocketMQ服务器地址等配置信息。
	 * </p>
	 * @param environment 环境配置
	 */
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
