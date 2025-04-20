package com.relaxed.autoconfigure.mq.core.annotation;

import com.relaxed.autoconfigure.mq.core.creator.NullMQDeclareCreator;
import com.relaxed.autoconfigure.mq.core.enums.MQTypeEnum;
import com.relaxed.autoconfigure.mq.rabbitmq.RabbitMQDeclareCreator;
import com.relaxed.autoconfigure.mq.core.creator.MQDeclareCreator;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.DefaultBeanNameGenerator;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用MQ功能注解
 * <p>
 * 用于启用消息队列功能，支持配置扫描包、MQ类型、创建器和Bean名称生成器。 通过@Import导入MQScannerRegister实现自动扫描和注册MQ组件。
 * </p>
 *
 * @author Yakir
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(MQScannerRegister.class)
public @interface EnableMQ {

	/**
	 * 扫描包路径
	 * <p>
	 * 指定需要扫描的包路径，用于自动发现和注册MQ组件。 如果不指定，则默认扫描当前包及其子包。
	 * </p>
	 * @return 扫描包路径数组
	 */
	String[] basePackages() default {};

	/**
	 * MQ类型
	 * <p>
	 * 指定使用的MQ类型，必须指定。 如果使用非内置类型，则必须指定MQTypeEnum#other并同时指定mqCreator。
	 * </p>
	 * @return MQ类型枚举
	 */
	MQTypeEnum mqType();

	/**
	 * MQ创建器
	 * <p>
	 * 指定MQ声明创建器，用于创建和初始化MQ组件。 默认为NullMQDeclareCreator。
	 * </p>
	 * @return MQ声明创建器类型
	 */
	Class<? extends MQDeclareCreator> creator() default NullMQDeclareCreator.class;

	/**
	 * Bean名称生成器
	 * <p>
	 * 指定Bean名称生成器，用于生成注册的Bean的名称。 默认为DefaultBeanNameGenerator。
	 * </p>
	 * @return Bean名称生成器类型
	 */
	Class<? extends BeanNameGenerator> nameGenerator() default DefaultBeanNameGenerator.class;

}
