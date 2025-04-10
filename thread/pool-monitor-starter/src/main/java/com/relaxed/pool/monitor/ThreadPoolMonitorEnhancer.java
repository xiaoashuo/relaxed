package com.relaxed.pool.monitor;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.relaxed.pool.monitor.annotation.ThreadPoolMonitor;
import com.relaxed.pool.monitor.monitor.ThreadPoolTaskMonitor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.StandardMethodMetadata;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
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
		if (!(bean instanceof ThreadPoolExecutor || bean instanceof ThreadPoolTaskExecutor)) {
			return bean;
		}

		// 2. 获取Bean定义
		try {
			BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
			// 获取 @Bean 方法元数据
			if (beanDefinition instanceof AnnotatedBeanDefinition) {
				MethodMetadata methodMetadata = getFactoryMethodMetaData((AnnotatedBeanDefinition) beanDefinition);
				String annotationName = ThreadPoolMonitor.class.getName();
				// 4. 查找@ThreadPoolMonitor注解
				if (methodMetadata.isAnnotated(annotationName)) {
					ThreadPoolMonitor annotation = extractAnnotation(methodMetadata);
					if (annotation != null) {
						ThreadPoolExecutor threadPoolExecutor = null;
						if (bean instanceof ThreadPoolExecutor) {
							threadPoolExecutor = (ThreadPoolExecutor) bean;
							bean = createProxiedExecutor(beanName, threadPoolExecutor, annotation);
						}
						else if (bean instanceof ThreadPoolTaskExecutor) {
							ThreadPoolTaskExecutor threadPoolTaskExecutor = (ThreadPoolTaskExecutor) bean;

							threadPoolExecutor = threadPoolTaskExecutor.getThreadPoolExecutor();
							threadPoolExecutor = createProxiedExecutor(beanName, threadPoolExecutor, annotation);

							ReflectUtil.setFieldValue(threadPoolTaskExecutor, "threadPoolExecutor", threadPoolExecutor);
							bean = threadPoolTaskExecutor;
						}

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

	private static ThreadPoolMonitor extractAnnotation(MethodMetadata methodMetadata) {
		ThreadPoolMonitor annotation;
		if (methodMetadata instanceof StandardMethodMetadata) {
			Method method = ((StandardMethodMetadata) methodMetadata).getIntrospectedMethod();
			annotation = method.getAnnotation(ThreadPoolMonitor.class);
		}
		else {
			// 方案1：直接读取属性（推荐）
			Map<String, Object> attrs = methodMetadata.getAnnotationAttributes(ThreadPoolMonitor.class.getName());
			annotation = new ThreadPoolMonitor() {

				@Override
				public Class<? extends Annotation> annotationType() {
					return ThreadPoolMonitor.class;
				}

				@Override
				public String name() {
					return (String) attrs.get("name");
				}
			};
		}
		return annotation;
	}

	private MethodMetadata getFactoryMethodMetaData(AnnotatedBeanDefinition beanDefinition) {

		MethodMetadata factoryMethodMetadata = beanDefinition.getFactoryMethodMetadata();
		return factoryMethodMetadata;
	}

	// private Method getFactoryMethod(BeanDefinition beanDefinition) {
	// StandardMethodMetadata standardMethodMetadata = (StandardMethodMetadata)
	// ReflectUtil
	// .getFieldValue(beanDefinition, "factoryMethodMetadata");
	// Method factoryMethod = standardMethodMetadata.getIntrospectedMethod();
	// return factoryMethod;
	// }

	private <T extends ThreadPoolExecutor> ThreadPoolExecutor createProxiedExecutor(String beanName, T original,
			ThreadPoolMonitor annotation) {
		// 实现代理逻辑，例如监控线程池状态
		String poolKey = StrUtil.isBlank(annotation.name()) ? beanName : annotation.name();
		return getMonitor().register(poolKey, original);
	}

}
