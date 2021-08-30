package com.relaxed.extend.sms.executor.config;

/**
 * @author Yakir
 * @Topic SmsConstant
 * @Description
 * @date 2021/8/27 10:22
 * @Version 1.0
 */
public interface SmsConstant {
    /**
     * 短信服务实列 hash key
     */
     String SERVER_HASH_KEY="sms:server:id:hash";
    /**
     * 短信发送渠道列表 缓存key
     */
    String CHANNEL_LIST="sms:channel:list";
}
