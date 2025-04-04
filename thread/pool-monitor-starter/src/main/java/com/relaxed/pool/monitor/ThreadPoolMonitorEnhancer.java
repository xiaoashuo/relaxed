package com.relaxed.pool.monitor;

import cn.hutool.core.util.StrUtil;
import com.relaxed.pool.monitor.annotation.ThreadPoolMonitor;
import com.relaxed.pool.monitor.monitor.ThreadPoolTaskMonitor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Yakir
 * @Topic ThreadPoolMonitorPostProcessor
 * @Description
 * @date 2025/4/4 10:12
 * @Version 1.0
 */
@Slf4j
public class ThreadPoolMonitorEnhancer implements BeanPostProcessor, ApplicationContextAware {

	private ThreadPoolTaskMonitor monitor;

	private ConfigurableListableBeanFactory beanFactory;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.beanFactory = (ConfigurableListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
		this.monitor = applicationContext.getBean(ThreadPoolTaskMonitor.class);
	}

	private ThreadPoolTaskMonitor getMonitor() {
		return monitor;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		// 1. 检查是否是ThreadPoolExecutor实例
		if (!(bean instanceof ThreadPoolExecutor)) {
			return bean;
		}

		// 2. 获取Bean定义
		try {
			BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);

			// 3. 检查是否是@Bean方法创建的
			if (beanDefinition.getFactoryMethodName() != null && beanDefinition.getFactoryBeanName() != null) {

				Object factoryBean = beanFactory.getBean(beanDefinition.getFactoryBeanName());
				Method factoryMethod = ReflectionUtils.findMethod(factoryBean.getClass(),
						beanDefinition.getFactoryMethodName());

				// 4. 查找@ThreadPoolMonitor注解
				if (factoryMethod != null) {
					ThreadPoolMonitor annotation = AnnotationUtils.findAnnotation(factoryMethod,
							ThreadPoolMonitor.class);
					if (annotation != null) {
						bean = createProxiedExecutor(beanName, (ThreadPoolExecutor) bean, annotation);
					}
				}
			}
		}
		catch (Exception e) {
			// 异常处理
			log.error("线程池增强出错,当前bean名称:{}", beanName, e);
		}

		return bean;
	}

	private ThreadPoolExecutor createProxiedExecutor(String beanName, ThreadPoolExecutor original,
			ThreadPoolMonitor annotation) {
		// 实现代理逻辑，例如监控线程池状态
		String poolKey = StrUtil.isBlank(annotation.name()) ? beanName : annotation.name();
		return getMonitor().register(poolKey, original);
	}

}