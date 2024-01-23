package com.relaxed.autoconfigure.mq.core.creator;

import com.relaxed.autoconfigure.mq.core.domain.MQMeta;
import com.relaxed.autoconfigure.mq.core.enums.MQSendTypeEnum;
import com.relaxed.autoconfigure.mq.core.exception.MQException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;

/**
 * 空未定义创建器
 *
 * @author yakir
 */
public class NullMQDeclareCreator extends AbstractMQDeclareCreator {

	@Override
	protected BeanDefinitionBuilder generateQueueBeanDefBuilder(MQMeta.QueueMeta queueMeta) {
		throw new MQException("mq declare creator not found.");
	}

	@Override
	protected BeanDefinitionBuilder generateExchangeBeanDefBuilder(MQMeta.ExchangeMeta exchangeMeta,
			String exchangeName) {
		throw new MQException("mq declare creator not found.");
	}

	@Override
	protected void registeredBinding(BeanDefinitionRegistry beanDefinitionRegistry, BeanNameGenerator nameGenerator,
			String queueName, String exchangeName, String routingKey) {
		throw new MQException("mq declare creator not found.");
	}

}
