package com.relaxed.autoconfigure.mq.core.creator;

import com.relaxed.autoconfigure.mq.core.domain.AbstractMQ;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.core.env.Environment;

public interface MQDeclareCreator {

	/**
	 * 定义队列交换机 以及绑定关系
	 * @param amq
	 * @param beanDefinitionRegistry
	 * @param environment
	 * @param nameGenerator
	 */
	void declareQueueExchange(AbstractMQ amq, BeanDefinitionRegistry beanDefinitionRegistry, Environment environment,
			BeanNameGenerator nameGenerator);

}
