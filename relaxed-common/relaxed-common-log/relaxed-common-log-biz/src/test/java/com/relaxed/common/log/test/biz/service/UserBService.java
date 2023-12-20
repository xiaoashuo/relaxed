package com.relaxed.common.log.test.biz.service;

import com.relaxed.common.log.biz.annotation.BizLog;
import com.relaxed.common.log.biz.context.LogOperatorContext;
import com.relaxed.common.log.test.biz.domain.LogUser;
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

	@BizLog(systemName = "'业务B'", success = "'success exec b '", bizNo = "#user.bizNo",
			detail = "'将'+#user.username+'的商品状态,修改为了 '+#user.status+ifunc_test(#user.status)")
	public void updateUserStatus(LogUser user) {
		LogOperatorContext.push("userB", "finish");
		log.info("UserB-service修改了用户状态");

	}

}
