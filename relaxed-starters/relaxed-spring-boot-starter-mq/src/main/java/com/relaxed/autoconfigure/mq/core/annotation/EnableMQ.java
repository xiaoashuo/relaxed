package com.relaxed.autoconfigure.mq.core.annotation;

import com.relaxed.autoconfigure.mq.core.creator.NullMQDeclareCreator;
import com.relaxed.autoconfigure.mq.core.enums.MQTypeEnum;
import com.relaxed.autoconfigure.mq.rabbitmq.RabbitMQDeclareCreator;
import com.relaxed.autoconfigure.mq.core.creator.MQDeclareCreator;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.DefaultBeanNameGenerator;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(MQScannerRegister.class)
public @interface EnableMQ {

	/**
	 * 扫描包
	 * @return
	 */
	String[] basePackages() default {};

	/**
	 * MQ 类型 必须指定 若不为内置 则指定MQTypeEnum#other 则必须指定mqCreator
	 * @return
	 */
	MQTypeEnum mqType();

	/**
	 * mq 创建者
	 * @return
	 */
	Class<? extends MQDeclareCreator> creator() default NullMQDeclareCreator.class;

	/**
	 * bean 名称生成器
	 * @return
	 */
	Class<? extends BeanNameGenerator> nameGenerator() default DefaultBeanNameGenerator.class;

}
