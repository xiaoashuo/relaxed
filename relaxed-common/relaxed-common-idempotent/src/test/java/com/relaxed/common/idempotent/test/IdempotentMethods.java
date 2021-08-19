package com.relaxed.common.idempotent.test;

import com.relaxed.common.idempotent.annotation.Idempotent;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hccake
 */
@Slf4j
public class IdempotentMethods {

	@Idempotent(uniqueExpression = "#key", duration = 1 * 10)
	public String method1(String key) {
		log.info("===执行方法1成功===");
		return key;
	}

}
