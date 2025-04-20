package com.relaxed.common.job.annotation;

import com.relaxed.common.job.XxlJobAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用 XXL-Job 支持的注解。 用于在 Spring Boot 应用中启用 XXL-Job 的自动配置。 主要功能包括： 1. 自动配置 XXL-Job 执行器 2.
 * 启用日志同步功能 3. 支持任务执行链路追踪
 *
 * @author Yakir
 * @since 1.0
 * @see XxlJobAutoConfiguration
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({ XxlJobAutoConfiguration.class })
public @interface EnableXxlJob {

}
