package com.relaxed.extend.sms.executor.config;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.relaxed.common.core.util.SpringUtils;
import com.relaxed.extend.cache.CacheManage;
import com.relaxed.extend.sms.executor.channel.SmsChannel;
import com.relaxed.extend.sms.executor.model.convert.ConfigConverter;
import com.relaxed.extend.sms.executor.model.dto.ConfigDTO;
import com.relaxed.extend.sms.executor.model.entity.Config;
import com.relaxed.extend.sms.executor.service.ConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Yakir
 * @Topic SmsChannelLoader
 * @Description 短信渠道加载器
 * @date 2021/8/27 10:19
 * @Version 1.0
 */
@Slf4j
@RequiredArgsConstructor
public class SmsChannelLoader implements InitializingBean {
    private static final List<SmsChannel> CHANNEL_LIST = new ArrayList<>();
    /**
     * 维护所有通道
     * @author yakir
     * @date 2021/8/27 16:05
     * @param null
     * @return null
     */
    private static  Map<String,SmsChannel> CHANNEL_MAP = new HashMap<>();
    private final ConfigService configService;
    private final CacheManage cacheManage;

    @Override
    public void afterPropertiesSet() throws Exception {
        initChannelList();
        channelLoader();
    }

    private void initChannelList() {
        Map<String, SmsChannel> beansOfType = SpringUtils.getBeansOfType(SmsChannel.class);
        for (SmsChannel smsChannel : beansOfType.values()) {
            CHANNEL_MAP.put(smsChannel.channelAlias(),smsChannel);
        }
    }


    private void channelLoader() {
        //1.查询渠道列表
        List<Config> configs = configService.listChannel();
        log.info("查询到可用通道{}",configs);
        //2.遍历通道列表，将所有已启用通道注册进入
        List<SmsChannel> tempChanelList=new ArrayList<>();
        for (Config config : configs) {
            ConfigDTO configDTO = ConfigConverter.INSTANCE.poToDto(config);
            if (StrUtil.isNotEmpty(config.getOther())){
                configDTO.setExtend(JSONUtil.toBean(config.getOther(),Map.class));
            }
            //平台别名
            String platform = configDTO.getPlatform().trim();
            SmsChannel smsChannel = CHANNEL_MAP.get(platform);
            if (smsChannel!=null){
                smsChannel.setChannelConfig(configDTO);
                tempChanelList.add(smsChannel);
            }
        }
        //3.将每个通道的Bean对象保存到CONNECT_LIST集合中
        if (!CHANNEL_LIST.isEmpty()){
            CHANNEL_LIST.clear();
        }
        CHANNEL_LIST.addAll(tempChanelList);
        log.info("将初始化的通道加载到集合中：{}",CHANNEL_LIST);

    }

    public <T> T getChannelByLevel(Integer level) {
        return (T) CHANNEL_LIST.get(level - 1);
    }

    public boolean checkConnectLevel(Integer level) {
        return CHANNEL_LIST.size() <= level;
    }


}
