package com.relaxed.common.log.test.operator;

import com.relaxed.common.log.operation.annotation.BizLog;
import com.relaxed.common.log.operation.aspect.LogOperatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Yakir
 * @Topic UserAService
 * @Description
 * @date 2023/12/14 15:12
 * @Version 1.0
 */
@Slf4j
@Service
public class UserAService {
    @Autowired
    private UserBService userBService;

    /**
     * 模板替换方式
     * @param user
     * @return
     */
    @BizLog(success = "'商品信息更新成功'",bizNo = "#user.bizNo",type = "'创建用户'", fail = "'商品购买失败'",
            operator = "{{#user.username}}",
            detail = "'将{ifunc_test{#user.status,#user.username}}测试数据{ifunc_test{#user.status}}")
    public String sendGoods(User user){
        log.info("UserA-service修改了用户状态");
        LogOperatorContext.push("userA","finish");
        user.setStatus(2);
        userBService.updateUserStatus(user);
        if (true){
            throw new RuntimeException("错误产生");
        }

        return "";
    }
    /**
     * 正则方式
     */
//    @BizLog(success = "'商品信息更新成功'",bizNo = "#user.bizNo",type = "'创建用户'", fail = "'商品购买失败'",
//    operator = "#user.username",
//    detail = "'将'+#user.username+'的商品状态,修改为了 '+#user.status+ifunc_test(#user.status)+'二部分'+ifunc_test(#user.status)")
//    public String sendGoods(User user){
//        log.info("UserA-service修改了用户状态");
//        LogOperatorContext.push("userA","finish");
//        user.setStatus(2);
//        userBService.updateUserStatus(user);
//        if (true){
//            throw new RuntimeException("错误产生");
//        }
//
//        return "";
//    }
}
