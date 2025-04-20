package com.relaxed.pool.monitor;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.relaxed.pool.monitor.annotation.ThreadPoolMonitor;
import com.relaxed.pool.monitor.monitor.MonitoredThreadPool;
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
import org.springframework.core.task.TaskDecorator;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.StandardMethodMetadata;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池监控增强器，用于自动增强被 @ThreadPoolMonitor 注解标记的线程池。 实现了 BeanPostProcessor 接口，在 Spring Bean
 * 初始化后对线程池进行代理增强， 添加监控和动态调整能力。
 *
 * @author Yakir
 * @since 1.0.0
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

	/**
	 * Bean 初始化后的处理方法，用于增强线程池。 对于 ThreadPoolExecutor 和 ThreadPoolTaskExecutor 类型的 Bean，
	 * 如果它们被 @ThreadPoolMonitor 注解标记，则进行监控增强处理。
	 * @param bean 待处理的 Bean 实例
	 * @param beanName Bean 的名称
	 * @return 处理后的 Bean 实例，可能是原实例或代理后的实例
	 * @throws BeansException 处理过程中可能抛出的异常
	 */
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
							if (bean instanceof MonitoredThreadPool) {
								MonitoredThreadPool monitoredThreadPool = (MonitoredThreadPool) bean;

								// 若本身注册的就是监控线程池 则直接注册
								String poolKey = StrUtil.isBlank(annotation.name()) ? monitoredThreadPool.getName()
										: annotation.name();
								getMonitor().register(poolKey, monitoredThreadPool);
							}
							else {
								threadPoolExecutor = (ThreadPoolExecutor) bean;
								bean = createProxiedExecutor(beanName, threadPoolExecutor, annotation);
							}

						}
						else if (bean instanceof ThreadPoolTaskExecutor) {
							ThreadPoolTaskExecutor threadPoolTaskExecutor = (ThreadPoolTaskExecutor) bean;

							threadPoolExecutor = createProxiedExecutor(beanName, threadPoolTaskExecutor, annotation);

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

	/**
	 * 从方法元数据中提取 ThreadPoolMonitor 注解。 支持标准的方法元数据和自定义的注解属性提取。
	 * @param methodMetadata 方法元数据
	 * @return ThreadPoolMonitor 注解实例
	 */
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

	/**
	 * 获取 Bean 定义中的工厂方法元数据。
	 * @param beanDefinition 带注解的 Bean 定义
	 * @return 工厂方法元数据
	 */
	private MethodMetadata getFactoryMethodMetaData(AnnotatedBeanDefinition beanDefinition) {
		return beanDefinition.getFactoryMethodMetadata();
	}

	/**
	 * 创建被监控的线程池执行器。 将原始的线程池执行器包装为可监控的实例。
	 * @param beanName Bean 的名称
	 * @param original 原始线程池执行器
	 * @param annotation ThreadPoolMonitor 注解实例
	 * @return 增强后的线程池执行器
	 */
	private <T extends ThreadPoolExecutor> ThreadPoolExecutor createProxiedExecutor(String beanName, T original,
			ThreadPoolMonitor annotation) {
		// 实现代理逻辑，例如监控线程池状态
		String poolKey = StrUtil.isBlank(annotation.name()) ? beanName : annotation.name();
		return getMonitor().register(poolKey, original);
	}

	/**
	 * 创建被监控的 ThreadPoolTaskExecutor。 将原始的 ThreadPoolTaskExecutor 包装为可监控的实例， 保留原有的任务装饰器功能。
	 * @param beanName Bean 的名称
	 * @param threadPoolTaskExecutor 原始线程池任务执行器
	 * @param annotation ThreadPoolMonitor 注解实例
	 * @return 增强后的线程池执行器
	 */
	private ThreadPoolExecutor createProxiedExecutor(String beanName, ThreadPoolTaskExecutor threadPoolTaskExecutor,
			ThreadPoolMonitor annotation) {
		String poolKey = StrUtil.isBlank(annotation.name()) ? beanName : annotation.name();
		ThreadPoolExecutor threadPoolExecutor = threadPoolTaskExecutor.getThreadPoolExecutor();
		// 装饰器
		TaskDecorator taskDecorator = (TaskDecorator) ReflectUtil.getFieldValue(threadPoolTaskExecutor,
				"taskDecorator");
		ConcurrentReferenceHashMap decoratedTaskMap = (ConcurrentReferenceHashMap) ReflectUtil
				.getFieldValue(threadPoolTaskExecutor, "decoratedTaskMap");
		MonitoredThreadPool monitoredThreadPool = new MonitoredThreadPool(poolKey, threadPoolExecutor) {
			@Override
			public void execute(Runnable command) {
				Runnable decorated;
				if (taskDecorator != null) {
					decorated = taskDecorator.decorate(command);
					if (decorated != command) {
						decoratedTaskMap.put(decorated, command);
					}
				}
				else {
					decorated = command;
				}
				super.execute(decorated);
			}
		};
		// 实现代理逻辑，例如监控线程池状态

		return getMonitor().register(poolKey, monitoredThreadPool);
	}

}
