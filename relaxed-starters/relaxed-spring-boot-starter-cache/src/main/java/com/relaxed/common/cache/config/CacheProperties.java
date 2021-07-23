package com.relaxed.common.cache.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Yakir
 * @Topic CacheProperties
 * @Description
 * @date 2021/7/23 17:14
 * @Version 1.0
 */
@Data
@ConfigurationProperties(prefix = "relaxed.redis")
public class CacheProperties {
    /**
     * 通用的key前缀
     */
    private String keyPrefix = "";
    /**
     * redis锁 后缀
     */
    private String lockKeySuffix = "locked";

    /**
     * 默认分隔符
     */
    private String delimiter = ":";

    /**
     * 空值标识
     */
    private String nullValue = "N_V";

    /**
     * 默认超时时间(s)
     */
    private long expireTime = 86400L;

    /**
     * 锁的超时时间(ms)
     */
    private long lockedTimeOut = 1000L;



    /**
     * redis 特有数据
     */
    @Data
    public static class Redis{

    }





}
