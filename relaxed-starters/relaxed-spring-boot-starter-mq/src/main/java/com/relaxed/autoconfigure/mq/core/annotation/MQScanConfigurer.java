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

/**
 * MQ扫描配置器
 * <p>
 * 实现BeanDefinitionRegistryPostProcessor接口，用于扫描和注册MQ组件。
 * 负责扫描指定包下的AbstractMQ子类，并使用MQDeclareCreator创建和注册MQ组件。
 * </p>
 *
 * @author Yakir
 * @since 1.0
 */
@Setter
public class MQScanConfigurer implements BeanDefinitionRegistryPostProcessor {

	/**
	 * 扫描的基础包路径
	 */
	private String basePackage;

	/**
	 * MQ声明创建器
	 */
	private MQDeclareCreator creator;

	/**
	 * Bean名称生成器
	 */
	private BeanNameGenerator nameGenerator;

	/**
	 * Spring环境配置
	 */
	private Environment environment;

	/**
	 * 处理Bean定义注册表
	 * <p>
	 * 扫描指定包下的AbstractMQ子类，实例化并注册MQ组件。 设置全局环境配置。
	 * </p>
	 * @param beanDefinitionRegistry Bean定义注册表
	 * @throws BeansException 如果处理过程中发生异常
	 */
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

	/**
	 * 处理Bean工厂
	 * <p>
	 * 当前实现为空，预留用于后续扩展。
	 * </p>
	 * @param beanFactory 可配置的Bean工厂
	 * @throws BeansException 如果处理过程中发生异常
	 */
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		// 预留实现
	}

}
