package com.relaxed.common.log.operation.annotation;

import com.relaxed.common.log.operation.enums.OperationTypeEnum;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author Yakir
 * @Topic CreateLog
 * @Description
 * @date 2021/6/27 12:43
 * @Version 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Log(type = OperationTypeEnum.UPDATE)
public @interface UpdateLog {

	/**
	 * 消息分组
	 * @return
	 */
	@AliasFor(annotation = Log.class)
	String group() default "default";

	/**
	 * 日志消息
	 * @return 日志消息描述
	 */
	@AliasFor(annotation = Log.class)
	String msg();

}
