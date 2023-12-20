package com.relaxed.common.log.test.biz.service;

import com.relaxed.common.log.biz.annotation.BizLog;
import com.relaxed.common.log.biz.context.LogOperatorContext;
import com.relaxed.common.log.test.biz.domain.LogUser;
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
	@BizLog(systemName = "'业务A'", success = "'商品信息更新成功'", bizNo = "#user.bizNo", type = "'创建用户'", fail = "'商品购买失败'",
			operator = "{{#user.username}}", condition = "true", recordReturnValue = false,
			detail = "'将{ifunc_test{#user.status,#user.username}}测试数据{ifunc_test{#user.status}}")
	public String sendGoods(LogUser user) {
		log.info("UserA-service修改了用户状态");
		LogOperatorContext.push("userA", "finish");
		user.setStatus(2);
		userBService.updateUserStatus(user);
		// if (true){
		// throw new RuntimeException("错误产生");
		// }

		return "执行A方法成功";
	}

}
