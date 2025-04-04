package com.relaxed.test.thread;

import lombok.Data;

/**
 * @author Yakir
 * @Topic ThreadPoolMonitorProperties
 * @Description
 * @date 2025/4/3 17:41
 * @Version 1.0
 */
@Data
public class ThreadPoolMonitorProperties {
    /**
     * 监控是否启用
     */
    private boolean monitorEnabled=true;
    /**
     * 告警阈值(百分比)
     */
    private Integer alertThreshold=80;
    /**
     * 提醒渠道
     */
    private String alertChannels;

    /**
     * 监控间隔 默认10s
     */
    private long monitorIntervalMills=10000;
    /**
     * 自动调整池数是否启用 默认为true
     * 若最大线程数不够用 自动扩充
     * 若当前闲置率过高 且超过一定时间 则将最大线程数 恢复到起始最大线程数
     */
    private boolean adjustPoolNumEnabled=true;
    /**
     * 空闲比率最大阈值 若所有空闲率小于最大比例，且超出空闲率最大时间 将自动恢复成原线程最大值
     * 百分比
     */
    private long idleRatioMaxThreshold=20;
    /**
     * 空闲率间隔时间 当超过定义的毫秒时间 则将自动扩充的最大线程池恢复原值
     * 单位 毫秒
     */
    private long idleRatioIntervalMills=60000;

    /**
     * 自动调整最大阈值 调整数超过当前值不在调整
     */
    private int adjustPoolMaxinumThreshold=30;
}
