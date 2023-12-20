package com.relaxed.common.log.test.biz.service;

import cn.hutool.core.util.IdUtil;
import com.relaxed.common.log.biz.annotation.BizLog;
import com.relaxed.common.log.biz.context.LogOperatorContext;
import com.relaxed.common.log.test.biz.domain.LogUser;
import org.springframework.stereotype.Service;

/**
 * @author Yakir
 * @Topic BizLogService
 * @Description
 * @date 2023/12/20 15:25
 * @Version 1.0
 */
@Service
public class BizLogService {

    /**
     * 简单注解
     * @param logUser
     * @return
     */
    @BizLog(success = "'simpleMethod执行成功'",bizNo = "{{#logUser.bizNo}}")
    public String simpleMethod(LogUser logUser){
        return "method [simpleMethod] exec success!!!";
    }

    /**
     * 上下文变量方法
     * @param logUser
     * @return
     */
    @BizLog(success = "'simpleMethod 执行成功'",bizNo = "{{#logUser.bizNo}}",detail = "物流投递到{{#deliveryAddress}}")
    public String simpleMethodContext(LogUser logUser){
        String deliveryAddress  = "上海市普陀区长寿路1888号";
        LogOperatorContext.push("deliveryAddress",deliveryAddress);
        return "method [simpleMethodContext] exec success!!!";
    }
}
