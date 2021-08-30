package com.relaxed.extend.sms.executor.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Yakir
 * @Topic SmsExecutorProperties
 * @Description
 * @date 2021/8/27 9:39
 * @Version 1.0
 */
@Data
@ConfigurationProperties(prefix = "relaxed.sms")
public class SmsExecutorProperties {
    /**
     * 消息最大失败次数
     */
    private int messageErrorNum;
    /**
     * 通道最大失败次数
     */
    private int configLevelFailNum;
    /**
     * 通道选举算法启动比例
     */
    private double configBuildScale;
    /**
     * 服务超时时间戳 毫秒  默认5分钟
     */
    private long serverTimeout=1000 * 60 * 5;
}
