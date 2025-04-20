package com.relaxed.autoconfigure.mq.core.creator;

import cn.hutool.core.util.StrUtil;
import com.relaxed.autoconfigure.mq.core.domain.AbstractMQ;
import com.relaxed.autoconfigure.mq.core.domain.MQMeta;
import org.springframework.beans.factory.support.*;
import org.springframework.core.env.Environment;

/**
 * MQ声明创建器抽象类
 * <p>
 * 实现MQDeclareCreator接口，提供创建队列、交换器和绑定关系的基础实现。 子类需要实现具体的队列、交换器创建和绑定逻辑。
 * </p>
 *
 * @author Yakir
 * @since 1.0
 */
public abstract class AbstractMQDeclareCreator implements MQDeclareCreator {

	/**
	 * 声明队列和交换器以及它们之间的绑定关系
	 * <p>
	 * 根据消息模型创建队列、交换器，并建立它们之间的绑定关系。 使用Bean定义注册表注册创建的组件。
	 * </p>
	 * @param amq 消息模型
	 * @param beanDefinitionRegistry Bean定义注册表
	 * @param environment 环境配置
	 * @param nameGenerator Bean名称生成器
	 */
	@Override
	public void declareQueueExchange(AbstractMQ amq, BeanDefinitionRegistry beanDefinitionRegistry,
			Environment environment, BeanNameGenerator nameGenerator) {
		MQMeta mqMeta = amq.getMQMeta();
		// 注册队列

		MQMeta.QueueMeta queueMeta = mqMeta.getQueueMeta();
		String queueName = queueMeta.getQueueName();
		boolean queueFlag = StrUtil.isNotEmpty(queueName);
		if (queueFlag) {
			BeanDefinitionBuilder queueBeanDefinitionBuilder = generateQueueBeanDefBuilder(queueMeta);
			AbstractBeanDefinition queueBeanDefinition = queueBeanDefinitionBuilder.getBeanDefinition();
			beanDefinitionRegistry.registerBeanDefinition(
					nameGenerator.generateBeanName(queueBeanDefinition, beanDefinitionRegistry), queueBeanDefinition);
		}
		MQMeta.ExchangeMeta exchangeMeta = mqMeta.getExchangeMeta();
		String exchangeName = exchangeMeta.getExchangeName();
		boolean exchangeFlag = StrUtil.isNotEmpty(exchangeName);
		if (exchangeFlag) {
			BeanDefinitionBuilder exchangeBeanDefBuilder = generateExchangeBeanDefBuilder(exchangeMeta, exchangeName);
			AbstractBeanDefinition exchangeBeanDefinition = exchangeBeanDefBuilder.getBeanDefinition();
			beanDefinitionRegistry.registerBeanDefinition(
					nameGenerator.generateBeanName(exchangeBeanDefinition, beanDefinitionRegistry),
					exchangeBeanDefinition);
		}
		boolean openBinding = isOpenBinding(mqMeta, queueFlag, exchangeFlag);
		if (openBinding) {
			registeredBinding(beanDefinitionRegistry, nameGenerator, queueName, exchangeName, mqMeta.getRoutingKey());
		}

	}

	/**
	 * 判断是否开启队列和交换器的绑定
	 * <p>
	 * 当队列和交换器都存在，且mqMeta中binding为true时开启绑定。
	 * </p>
	 * @param mqMeta MQ元数据
	 * @param queueFlag 队列是否存在
	 * @param exchangeFlag 交换器是否存在
	 * @return 是否开启绑定
	 */
	protected boolean isOpenBinding(MQMeta mqMeta, boolean queueFlag, boolean exchangeFlag) {
		return queueFlag && exchangeFlag && mqMeta.isBinding();
	}

	/**
	 * 生成队列Bean定义构建器
	 * <p>
	 * 根据队列元数据创建队列Bean定义构建器。
	 * </p>
	 * @param queueMeta 队列元数据
	 * @return 队列Bean定义构建器
	 */
	protected abstract BeanDefinitionBuilder generateQueueBeanDefBuilder(MQMeta.QueueMeta queueMeta);

	/**
	 * 生成交换器Bean定义构建器
	 * <p>
	 * 根据交换器元数据和名称创建交换器Bean定义构建器。
	 * </p>
	 * @param exchangeMeta 交换器元数据
	 * @param exchangeName 交换器名称
	 * @return 交换器Bean定义构建器
	 */
	protected abstract BeanDefinitionBuilder generateExchangeBeanDefBuilder(MQMeta.ExchangeMeta exchangeMeta,
			String exchangeName);

	/**
	 * 注册队列和交换器的绑定关系
	 * <p>
	 * 创建并注册Binding Bean，将队列绑定到交换器。
	 * </p>
	 * @param beanDefinitionRegistry Bean定义注册表
	 * @param nameGenerator Bean名称生成器
	 * @param queueName 队列名称
	 * @param exchangeName 交换器名称
	 * @param routingKey 路由键
	 */
	protected abstract void registeredBinding(BeanDefinitionRegistry beanDefinitionRegistry,
			BeanNameGenerator nameGenerator, String queueName, String exchangeName, String routingKey);

}
