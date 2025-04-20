package com.relaxed.common.job.trace;

/**
 * @author Yakir
 * @Topic TraceXxlJobSpringExecutor
 * @Description
 * @date 2024/12/19 11:29
 * @Version 1.0
 */

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.executor.XxlJobExecutor;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import com.xxl.job.core.glue.GlueFactory;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.handler.impl.MethodJobHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 可追踪的 XXL-Job Spring 执行器。 继承自 XxlJobSpringExecutor，为任务执行添加追踪功能。 主要功能包括： 1.
 * 自动扫描并注册带有 @XxlJob 注解的方法 2. 使用 TraceMethodJobHandler 包装任务处理器 3. 支持任务执行链路追踪
 *
 * @author Yakir
 * @since 1.0
 * @see TraceMethodJobHandler
 */
public class TraceXxlJobSpringExecutor extends XxlJobSpringExecutor {

	/**
	 * 日志记录器
	 */
	private static final Logger logger = LoggerFactory.getLogger(TraceXxlJobSpringExecutor.class);

	/**
	 * 在单例初始化完成后执行。 主要完成以下工作： 1. 初始化任务处理器方法仓库 2. 刷新 GlueFactory 3. 启动执行器
	 */
	@Override
	public void afterSingletonsInstantiated() {
		// init JobHandler Repository
		/* initJobHandlerRepository(applicationContext); */

		// init JobHandler Repository (for method)
		initJobHandlerMethodRepository(getApplicationContext());

		// refresh GlueFactory
		GlueFactory.refreshInstance(1);

		// super start
		try {
			super.start();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 初始化任务处理器方法仓库。 扫描 Spring 容器中所有带有 @XxlJob 注解的方法， 并将其注册为任务处理器。
	 * @param applicationContext Spring 应用上下文
	 */
	private void initJobHandlerMethodRepository(ApplicationContext applicationContext) {
		if (applicationContext == null) {
			return;
		}
		// init job handler from method
		String[] beanDefinitionNames = applicationContext.getBeanNamesForType(Object.class, false, true);
		for (String beanDefinitionName : beanDefinitionNames) {
			Object bean = applicationContext.getBean(beanDefinitionName);

			Map<Method, XxlJob> annotatedMethods = null; // referred to
															// ：org.springframework.context.event.EventListenerMethodProcessor.processBean
			try {
				annotatedMethods = MethodIntrospector.selectMethods(bean.getClass(),
						new MethodIntrospector.MetadataLookup<XxlJob>() {
							@Override
							public XxlJob inspect(Method method) {
								return AnnotatedElementUtils.findMergedAnnotation(method, XxlJob.class);
							}
						});
			}
			catch (Throwable ex) {
				logger.error("xxl-job method-jobhandler resolve error for bean[" + beanDefinitionName + "].", ex);
			}
			if (annotatedMethods == null || annotatedMethods.isEmpty()) {
				continue;
			}

			for (Map.Entry<Method, XxlJob> methodXxlJobEntry : annotatedMethods.entrySet()) {
				Method method = methodXxlJobEntry.getKey();
				XxlJob xxlJob = methodXxlJobEntry.getValue();
				if (xxlJob == null) {
					continue;
				}

				String name = xxlJob.value();
				if (name.trim().length() == 0) {
					throw new RuntimeException("xxl-job method-jobhandler name invalid, for[" + bean.getClass() + "#"
							+ method.getName() + "] .");
				}
				if (loadJobHandler(name) != null) {
					throw new RuntimeException("xxl-job jobhandler[" + name + "] naming conflicts.");
				}

				// execute method
				if (!(method.getParameterTypes().length == 1
						&& method.getParameterTypes()[0].isAssignableFrom(String.class))) {
					throw new RuntimeException("xxl-job method-jobhandler param-classtype invalid, for["
							+ bean.getClass() + "#" + method.getName() + "] , "
							+ "The correct method format like \" public ReturnT<String> execute(String param) \" .");
				}
				if (!method.getReturnType().isAssignableFrom(ReturnT.class)) {
					throw new RuntimeException("xxl-job method-jobhandler return-classtype invalid, for["
							+ bean.getClass() + "#" + method.getName() + "] , "
							+ "The correct method format like \" public ReturnT<String> execute(String param) \" .");
				}
				method.setAccessible(true);

				// init and destory
				Method initMethod = null;
				Method destroyMethod = null;

				if (xxlJob.init().trim().length() > 0) {
					try {
						initMethod = bean.getClass().getDeclaredMethod(xxlJob.init());
						initMethod.setAccessible(true);
					}
					catch (NoSuchMethodException e) {
						throw new RuntimeException("xxl-job method-jobhandler initMethod invalid, for["
								+ bean.getClass() + "#" + method.getName() + "] .");
					}
				}
				if (xxlJob.destroy().trim().length() > 0) {
					try {
						destroyMethod = bean.getClass().getDeclaredMethod(xxlJob.destroy());
						destroyMethod.setAccessible(true);
					}
					catch (NoSuchMethodException e) {
						throw new RuntimeException("xxl-job method-jobhandler destroyMethod invalid, for["
								+ bean.getClass() + "#" + method.getName() + "] .");
					}
				}

				// registry jobhandler
				registJobHandler(name,
						new TraceMethodJobHandler(new MethodJobHandler(bean, method, initMethod, destroyMethod)));
			}
		}

	}

}
