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
 * rabbit mq 定义
 *
 * @author yakir
 */
public class RabbitMQDeclareCreator extends AbstractMQDeclareCreator {

	@Override
	protected BeanDefinitionBuilder generateQueueBeanDefBuilder(MQMeta.QueueMeta queueMeta) {
		Supplier<Queue> queueSupplier = () -> new Queue(queueMeta.getQueueName(), true, false, false,
				queueMeta.getArgs());
		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(Queue.class,
				queueSupplier);
		return beanDefinitionBuilder;
	}

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
