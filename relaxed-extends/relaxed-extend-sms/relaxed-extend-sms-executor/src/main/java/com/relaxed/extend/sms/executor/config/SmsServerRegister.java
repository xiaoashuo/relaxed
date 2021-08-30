package com.relaxed.extend.sms.executor.config;

import cn.hutool.core.lang.UUID;
import com.relaxed.extend.cache.CacheManage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.config.CacheManagementConfigUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.relaxed.extend.sms.executor.config.SmsConstant.SERVER_HASH_KEY;

/**
 * @author Yakir
 * @Topic SmsServerRegister
 * @Description 短信服务注册
 * @date 2021/8/27 9:48
 * @Version 1.0
 */
@Slf4j
@RequiredArgsConstructor
public class SmsServerRegister implements InitializingBean {

    /**
     * 当前服务实列唯一标识
     */
    private static final String SERVER_ID = UUID.randomUUID().toString();
    private final CacheManage cacheManage;
    private final SmsExecutorProperties smsExecutorProperties;



    @Override
    public void afterPropertiesSet() throws Exception {
        serviceRegister();
    }

    /**
     * 初始化bean时注册当前服务实列
     * @author yakir
     * @date 2021/8/27 10:09 
     */
    private void serviceRegister() {
        log.info("当前服务实例id:" + SERVER_ID);
        StringRedisTemplate redisTemplate = getRedisTemplate();
        redisTemplate.opsForHash().put(SERVER_HASH_KEY,SERVER_ID,System.currentTimeMillis());
    }

    /**
     * 定时服务报告
     * 报告服务信息证明服务存在 每三分钟报告一次，并传入当前时间戳
     */
    @Scheduled(cron = "1 0/3 * * * ?")
    public void serverReport() {
        long currentTime = System.currentTimeMillis();
        log.info("定时上报，服务id：{}，时间戳：{}",SERVER_ID, currentTime);
        StringRedisTemplate redisTemplate = getRedisTemplate();
        redisTemplate.opsForHash().put(SERVER_HASH_KEY,SERVER_ID, currentTime);
    }

    /**
     * 定时服务检查
     * 每十分钟检查一次服务列表，清空超过五分钟没有报告的服务
     * 服务注册器，定时服务检查，每十分钟检查一次服务列表，清空超过五分钟没有报告的服务
     */
    @Scheduled(cron = "30 0/10 * * * ?")
    public void checkServer() {
        //获取当前系统时间戳
        long current = System.currentTimeMillis();
        log.info("时间{}进行服务实例的检查，执行当前任务的服务为：{}",current,SERVER_ID);
        StringRedisTemplate redisTemplate = getRedisTemplate();
        //获得Redis中注册的所有服务实例id
        Map map = redisTemplate.opsForHash().entries(SERVER_HASH_KEY);
        List removeKeys = new ArrayList();
        map.forEach((key,value) -> {
            //key为服务实例id，value为上报的系统时间戳
            long storeTimeStamp = Long.parseLong(value.toString());
            if(current - storeTimeStamp > smsExecutorProperties.getServerTimeout()){
                //当前服务实例超过5分钟没有上报
                removeKeys.add(key);
            }
        });

        //清理服务实例
        removeKeys.forEach(key ->{
            log.info("清理服务实例：{}",key);
            redisTemplate.opsForHash().delete(SERVER_HASH_KEY,key);
        });
    }
    private StringRedisTemplate getRedisTemplate() {
        return cacheManage.getOperator();
    }

}
