package com.relaxed.autoconfigure.mq.rabbitmq;

import cn.hutool.core.util.RandomUtil;
import com.relaxed.autoconfigure.mq.core.creator.AbstractMQDeclareCreator;
import com.relaxed.autoconfigure.mq.core.domain.MQMeta;
import com.relaxed.autoconfigure.mq.core.enums.MQSendTypeEnum;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;

import java.util.Collections;
import java.util.Map;
import java.util.function.Supplier;

/**
 * RabbitMQ声明创建器。 继承AbstractMQDeclareCreator，负责创建和初始化RabbitMQ的队列、交换器和绑定关系。
 * 支持广播模式和点对点模式的消息路由。
 *
 * @author Yakir
 * @since 1.0
 */
public class RabbitMQDeclareCreator extends AbstractMQDeclareCreator {

	/**
	 * 生成队列Bean定义构建器。 根据队列元数据创建持久化队列。
	 * @param queueMeta 队列元数据
	 * @return 队列Bean定义构建器
	 */
	@Override
	protected BeanDefinitionBuilder generateQueueBeanDefBuilder(MQMeta.QueueMeta queueMeta) {
		Supplier<Queue> queueSupplier = () -> new Queue(queueMeta.getQueueName(), true, false, false,
				queueMeta.getArgs());
		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(Queue.class,
				queueSupplier);
		return beanDefinitionBuilder;
	}

	/**
	 * 注册队列和交换器的绑定关系。 创建并注册Binding Bean，将队列绑定到交换器。
	 * @param beanDefinitionRegistry Bean定义注册表
	 * @param nameGenerator Bean名称生成器
	 * @param queueName 队列名称
	 * @param exchangeName 交换器名称
	 * @param routingKey 路由键
	 */
	@Override
	protected void registeredBinding(BeanDefinitionRegistry beanDefinitionRegistry, BeanNameGenerator nameGenerator,
			String queueName, String exchangeName, String routingKey) {
		/// 注册绑定
		Supplier<Binding> bindingSupplier = () -> new Binding(queueName, Binding.DestinationType.QUEUE, exchangeName,
				routingKey, Collections.emptyMap());
		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(Binding.class,
				bindingSupplier);
		AbstractBeanDefinition bindBeanDefinition = beanDefinitionBuilder.getBeanDefinition();
		beanDefinitionRegistry.registerBeanDefinition(
				nameGenerator.generateBeanName(bindBeanDefinition, beanDefinitionRegistry), bindBeanDefinition);
	}

	/**
	 * 生成交换器Bean定义构建器。 根据交换器元数据和发送类型创建相应的交换器。
	 * @param exchangeMeta 交换器元数据
	 * @param exchangeName 交换器名称
	 * @return 交换器Bean定义构建器
	 */
	@Override
	protected BeanDefinitionBuilder generateExchangeBeanDefBuilder(MQMeta.ExchangeMeta exchangeMeta,
			String exchangeName) {
		MQSendTypeEnum exchangeType = exchangeMeta.getExchangeType();
		Map<String, Object> args = exchangeMeta.getArgs();
		BeanDefinitionBuilder beanDefinitionBuilder;
		// 广播模式
		if (exchangeType == MQSendTypeEnum.BROADCAST) {
			// 动态注册交换机， 交换机名称/bean名称 = FANOUT_EXCHANGE_NAME_PREFIX + amq.getMQName()
			Supplier<FanoutExchange> fanoutExchangeSupplier = () -> new FanoutExchange(exchangeName, true, false, args);
			beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(FanoutExchange.class,
					fanoutExchangeSupplier);
		}
		else {
			Supplier<CustomExchange> customExchangeSupplier = () -> new CustomExchange(exchangeName,
					ExchangeTypes.DIRECT, true, false, args);
			beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(CustomExchange.class,
					customExchangeSupplier);
		}
		return beanDefinitionBuilder;
	}

}
