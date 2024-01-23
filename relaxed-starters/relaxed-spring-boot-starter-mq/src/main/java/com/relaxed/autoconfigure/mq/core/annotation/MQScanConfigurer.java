package com.relaxed.autoconfigure.mq.core.annotation;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import com.relaxed.autoconfigure.mq.core.creator.MQDeclareCreator;
import com.relaxed.autoconfigure.mq.core.domain.AbstractMQ;
import com.relaxed.autoconfigure.mq.utils.MQHelper;
import lombok.Setter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.core.env.Environment;

import java.util.Set;

@Setter
public class MQScanConfigurer implements BeanDefinitionRegistryPostProcessor {

	private String basePackage;

	private MQDeclareCreator creator;

	private BeanNameGenerator nameGenerator;

	private Environment environment;

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {

		// 注册全局方法
		MQHelper.setEnvironment(environment);
		// 获取到所有的MQ定义
		Set<Class<?>> set = ClassUtil.scanPackageBySuper(basePackage, AbstractMQ.class);

		for (Class<?> aClass : set) {
			// 实例化
			AbstractMQ amq = (AbstractMQ) ReflectUtil.newInstance(aClass);
			creator.declareQueueExchange(amq, beanDefinitionRegistry, environment, nameGenerator);

		}
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

	}

}
