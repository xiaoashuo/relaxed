package com.relaxed.common.log.test.operator;

import com.relaxed.common.log.operation.context.LogOperatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Yakir
 * @Topic UserBService
 * @Description
 * @date 2023/12/14 15:12
 * @Version 1.0
 */
@Slf4j
@Service
public class UserBService {

	// @BizLog(success = "商品信息更新成功",bizNo = "#user.bizNo",category = "user",
	// detail = "'将'+#user.username+'的商品状态,修改为了 '+#user.status+ifunc_test(#user.status)")
	public void updateUserStatus(User user) {
		LogOperatorContext.push("userB", "finish");
		log.info("UserB-service修改了用户状态");

	}

}
