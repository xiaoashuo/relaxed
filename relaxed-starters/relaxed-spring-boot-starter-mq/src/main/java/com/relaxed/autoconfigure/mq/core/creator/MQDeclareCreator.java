package com.relaxed.autoconfigure.mq.core.creator;

import com.relaxed.autoconfigure.mq.core.domain.AbstractMQ;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.core.env.Environment;

/**
 * MQ声明创建器接口
 * <p>
 * 定义创建和初始化消息队列相关组件的方法。 实现类负责创建队列、交换器以及它们之间的绑定关系。
 * </p>
 *
 * @author Yakir
 * @since 1.0
 */
public interface MQDeclareCreator {

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
	void declareQueueExchange(AbstractMQ amq, BeanDefinitionRegistry beanDefinitionRegistry, Environment environment,
			BeanNameGenerator nameGenerator);

}
