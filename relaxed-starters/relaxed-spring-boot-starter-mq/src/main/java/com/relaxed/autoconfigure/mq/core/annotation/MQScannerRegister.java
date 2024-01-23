package com.relaxed.autoconfigure.mq.core.annotation;

import com.relaxed.autoconfigure.mq.core.creator.MQDeclareCreator;
import com.relaxed.autoconfigure.mq.core.creator.NullMQDeclareCreator;
import com.relaxed.autoconfigure.mq.core.enums.MQTypeEnum;
import com.relaxed.autoconfigure.mq.core.exception.MQException;
import com.relaxed.autoconfigure.mq.rabbitmq.RabbitMQDeclareCreator;
import com.relaxed.autoconfigure.mq.rocketmq.RocketMQDeclareCreator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Yakir
 * @Topic AutoConfiguredRabbitMQScannerRegister
 * @Description
 * @date 2021/12/23 17:19
 * @Version 1.0
 */
public class MQScannerRegister implements EnvironmentAware, ImportBeanDefinitionRegistrar {

	private Environment environment;

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		// 后续参考MapperScan注解扫描注册包
		AnnotationAttributes mapperScanAttrs = AnnotationAttributes
				.fromMap(importingClassMetadata.getAnnotationAttributes(EnableMQ.class.getName()));
		if (mapperScanAttrs != null) {
			this.registerBeanDefinitions(importingClassMetadata, mapperScanAttrs, registry,
					generateBaseBeanName(importingClassMetadata, 0));
		}

	}

	void registerBeanDefinitions(AnnotationMetadata annoMeta, AnnotationAttributes annoAttrs,
			BeanDefinitionRegistry registry, String beanName) {
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(MQScanConfigurer.class);

		MQTypeEnum mqType = annoAttrs.getEnum("mqType");
		Class<? extends MQDeclareCreator> mqCreatorClass = chooseCreator(mqType, annoAttrs.getClass("creator"));
		MQDeclareCreator mqDeclareCreator = BeanUtils.instantiateClass(mqCreatorClass);
		builder.addPropertyValue("creator", mqDeclareCreator);
		Class<? extends BeanNameGenerator> nameGenerator = annoAttrs.getClass("nameGenerator");
		BeanNameGenerator beanNameGenerator = BeanUtils.instantiateClass(nameGenerator);
		builder.addPropertyValue("nameGenerator", beanNameGenerator);
		builder.addPropertyValue("environment", environment);

		List<String> basePackages = new ArrayList();
		basePackages.addAll(Arrays.stream(annoAttrs.getStringArray("basePackages")).filter(StringUtils::hasText)
				.collect(Collectors.toList()));
		if (basePackages.isEmpty()) {
			basePackages.add(getDefaultBasePackage(annoMeta));
		}
		builder.addPropertyValue("basePackage", StringUtils.collectionToCommaDelimitedString(basePackages));
		registry.registerBeanDefinition(beanName, builder.getBeanDefinition());

	}

	private Class<? extends MQDeclareCreator> chooseCreator(MQTypeEnum mqType,
			Class<? extends MQDeclareCreator> mqCreatorClass) {
		switch (mqType) {
		case RABBIT_MQ:
			mqCreatorClass = NullMQDeclareCreator.class.equals(mqCreatorClass) ? RabbitMQDeclareCreator.class
					: mqCreatorClass;
			break;
		case ROCKET_MQ:
			mqCreatorClass = NullMQDeclareCreator.class.equals(mqCreatorClass) ? RocketMQDeclareCreator.class
					: mqCreatorClass;
			break;
		case OTHER:
			if (NullMQDeclareCreator.class.equals(mqCreatorClass)) {
				throw new MQException("mq declare creator can not null mq declare creator");
			}
			break;
		}
		return mqCreatorClass;
	}

	private static String getDefaultBasePackage(AnnotationMetadata importingClassMetadata) {
		return ClassUtils.getPackageName(importingClassMetadata.getClassName());
	}

	private static String generateBaseBeanName(AnnotationMetadata importingClassMetadata, int index) {
		return importingClassMetadata.getClassName() + "#" + MQScanConfigurer.class.getSimpleName() + "#" + index;
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

}
