package com.relaxed.common.log.action;

import com.relaxed.common.log.action.properties.LogClientProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author Yakir
 * @Topic LogClientAutoConfiguration
 * @Description
 * @date 2021/12/14 16:48
 * @Version 1.0
 */
@EnableConfigurationProperties(value = LogClientProperties.class)
public class LogClientAutoConfiguration {

}
