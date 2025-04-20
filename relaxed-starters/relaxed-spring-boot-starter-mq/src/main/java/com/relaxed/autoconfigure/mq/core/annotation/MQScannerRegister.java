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
 * MQ扫描器注册器
 * <p>
 * 实现ImportBeanDefinitionRegistrar和EnvironmentAware接口，用于注册MQ扫描配置器。
 * 根据EnableMQ注解的配置，选择合适的MQ声明创建器，并注册MQScanConfigurer。
 * </p>
 *
 * @author Yakir
 * @since 1.0
 */
public class MQScannerRegister implements EnvironmentAware, ImportBeanDefinitionRegistrar {

	private Environment environment;

	/**
	 * 注册Bean定义
	 * <p>
	 * 从导入类的注解元数据中获取EnableMQ注解属性，并注册MQ扫描配置器。
	 * </p>
	 * @param importingClassMetadata 导入类的注解元数据
	 * @param registry Bean定义注册表
	 */
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

	/**
	 * 注册Bean定义
	 * <p>
	 * 根据注解属性和元数据创建并注册MQScanConfigurer。 设置MQ声明创建器、Bean名称生成器、环境配置和扫描包路径。
	 * </p>
	 * @param annoMeta 注解元数据
	 * @param annoAttrs 注解属性
	 * @param registry Bean定义注册表
	 * @param beanName Bean名称
	 */
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

	/**
	 * 选择MQ声明创建器
	 * <p>
	 * 根据MQ类型和指定的创建器类选择合适的MQ声明创建器。 对于内置类型（RABBIT_MQ、ROCKET_MQ），如果未指定创建器则使用默认创建器。
	 * 对于OTHER类型，必须指定创建器。
	 * </p>
	 * @param mqType MQ类型
	 * @param mqCreatorClass 指定的创建器类
	 * @return 选择的MQ声明创建器类
	 * @throws MQException 如果OTHER类型未指定创建器
	 */
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

	/**
	 * 获取默认基础包路径
	 * <p>
	 * 从导入类的类名中获取包名作为默认基础包路径。
	 * </p>
	 * @param importingClassMetadata 导入类的注解元数据
	 * @return 默认基础包路径
	 */
	private static String getDefaultBasePackage(AnnotationMetadata importingClassMetadata) {
		return ClassUtils.getPackageName(importingClassMetadata.getClassName());
	}

	/**
	 * 生成基础Bean名称
	 * <p>
	 * 根据导入类的类名和索引生成MQScanConfigurer的Bean名称。
	 * </p>
	 * @param importingClassMetadata 导入类的注解元数据
	 * @param index 索引
	 * @return 生成的Bean名称
	 */
	private static String generateBaseBeanName(AnnotationMetadata importingClassMetadata, int index) {
		return importingClassMetadata.getClassName() + "#" + MQScanConfigurer.class.getSimpleName() + "#" + index;
	}

	/**
	 * 设置环境配置
	 * <p>
	 * 实现EnvironmentAware接口，设置Spring环境配置。
	 * </p>
	 * @param environment Spring环境配置
	 */
	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

}