package com.relaxed.common.log.operation.annotation;

import com.relaxed.common.log.operation.OperationLogAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author Yakir
 * @Topic EnableOperationLog
 * @Description
 * @date 2021/6/27 13:42
 * @Version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({ OperationLogAutoConfiguration.class })
public @interface EnableOperationLog {

}
