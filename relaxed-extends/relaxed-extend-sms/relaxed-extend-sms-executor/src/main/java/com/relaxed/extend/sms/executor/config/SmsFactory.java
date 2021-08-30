package com.relaxed.extend.sms.executor.config;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.relaxed.extend.sms.executor.channel.SmsChannel;
import com.relaxed.extend.sms.executor.model.dto.SmsSendDTO;
import com.relaxed.extend.sms.executor.model.entity.ManualProcess;
import com.relaxed.extend.sms.executor.model.entity.SendLog;
import com.relaxed.extend.sms.executor.service.ManualProcessService;
import com.sun.deploy.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * @author Yakir
 * @Topic SmsFactory
 * @Description
 * @date 2021/8/27 16:28
 * @Version 1.0
 */
@RequiredArgsConstructor
@Slf4j
public class SmsFactory {

    private final SmsChannelLoader smsChannelLoader;

    private final ManualProcessService manualProcessService;

    /**
     * 根据级别获取通道
     * @author yakir
     * @date 2021/8/27 16:32
     * @param level
     * @return com.relaxed.extend.sms.executor.channel.SmsChannel
     */
    public SmsChannel getSmsChannelByLevel(Integer level){
        return smsChannelLoader.getChannelByLevel(level);
    }
    /**
     * 根据级别获取通道id
     * @author yakir
     * @date 2021/8/27 16:32
     * @param level
     * @return java.lang.String
     */
    public String getConfigIdByLevel(Integer level){
        return getSmsChannelByLevel(level).getChannelConfig().getId();
    }

    /**
     * 发送短信
     * @author yakir
     * @date 2021/8/27 17:54
     * @param param
     * @return boolean
     */
    public boolean send(String param){
        //短信发送选择级别
        Integer level=1;
        //短信发送错误数目
        Integer messageErrorNum=0;
        do{
            //执行时间
            LocalDateTime executeTime = LocalDateTime.now();
            log.info("发送短信 level:{} , json:{}", level, param);
            SendLog sendLog = new SendLog();
            sendLog.setCreateTime(executeTime);
            sendLog.setUpdateTime(executeTime);
            //开始执行发送短信时间
            long begin = System.currentTimeMillis();
            SmsSendDTO smsSendDTO = JSONUtil.toBean(param, SmsSendDTO.class);
            /**
             * 当所有通道全部尝试后，如果通道级别大于所有通道配置的级别，
             * 则说明所有通道都发送失败，该短信需要人工处理
             */
            if (smsChannelLoader.checkConnectLevel(level)){
                log.warn("短信发送失败，需要人工介入处理");
                ManualProcess manualProcess = new ManualProcess();
                manualProcess.setMobile(smsSendDTO.getMobile());
                manualProcess.setSignature(smsSendDTO.getSignature());
                manualProcess.setTemplate(smsSendDTO.getTemplate());
//                manualProcess.setConfigIds(StringUtils.join(smsSendDTO.getConfigIds()));
//                manualProcess.setRequest(JSON.toJSONString(smsSendDTO.getParams()));
                manualProcess.setRequest(smsSendDTO.getSendTime());
                manualProcess.setCreateTime(LocalDateTime.now());
                manualProcessService.save(manualProcess);
            }
        }while (true);

    }


}
