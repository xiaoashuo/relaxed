package com.relaxed.extend.sms.executor.channel;

import com.relaxed.extend.sms.executor.model.dto.ConfigDTO;

/**
 * @author Yakir
 * @Topic SmsChannel
 * @Description
 * @date 2021/8/27 15:58
 * @Version 1.0
 */
public interface SmsChannel {
    /**
     * 获取渠道别名
     * @author yakir
     * @date 2021/8/27 15:59
     * @return java.lang.String
     */
    String channelAlias();
    /**
     * 填充渠道配置信息
     * @author yakir
     * @date 2021/8/27 16:21
     * @param configDTO
     */
    void setChannelConfig(ConfigDTO configDTO);
    /**
     * 获取渠道配置
     * @author yakir
     * @date 2021/8/27 16:32
     * @return com.relaxed.extend.sms.executor.model.dto.ConfigDTO
     */
    ConfigDTO getChannelConfig();

}
