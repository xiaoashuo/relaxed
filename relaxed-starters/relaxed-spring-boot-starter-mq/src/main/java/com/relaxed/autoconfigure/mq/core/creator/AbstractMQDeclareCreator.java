package com.relaxed.autoconfigure.mq.core.creator;

import cn.hutool.core.util.StrUtil;
import com.relaxed.autoconfigure.mq.core.domain.AbstractMQ;
import com.relaxed.autoconfigure.mq.core.domain.MQMeta;
import org.springframework.beans.factory.support.*;
import org.springframework.core.env.Environment;

public abstract class AbstractMQDeclareCreator implements MQDeclareCreator {

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
	 * 是否开启绑定
	 * @author yakir
	 * @date 2021/12/27 13:44
	 * @param mqMeta
	 * @param queueFlag
	 * @param exchangeFlag
	 * @return boolean
	 */
	protected boolean isOpenBinding(MQMeta mqMeta, boolean queueFlag, boolean exchangeFlag) {
		return queueFlag && exchangeFlag && mqMeta.isBinding();
	}

	/**
	 * 生成队列bean 定义
	 * @author yakir
	 * @date 2021/12/27 11:08
	 * @param queueMeta
	 * @return org.springframework.beans.factory.support.BeanDefinitionBuilder
	 */
	protected abstract BeanDefinitionBuilder generateQueueBeanDefBuilder(MQMeta.QueueMeta queueMeta);

	/**
	 * 生成交换机bean 定义
	 *
	 * @author yakir
	 * @date 2021/12/27 11:08
	 * @param exchangeMeta
	 * @param exchangeName
	 * @return org.springframework.beans.factory.support.BeanDefinitionBuilder
	 */
	protected abstract BeanDefinitionBuilder generateExchangeBeanDefBuilder(MQMeta.ExchangeMeta exchangeMeta,
			String exchangeName);

	protected abstract void registeredBinding(BeanDefinitionRegistry beanDefinitionRegistry,
			BeanNameGenerator nameGenerator, String queueName, String exchangeName, String routingKey);

}
