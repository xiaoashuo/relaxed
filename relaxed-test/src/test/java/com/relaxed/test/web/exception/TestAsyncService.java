package com.relaxed.test.web.exception;

import com.relaxed.common.exception.annotation.ExceptionNotice;
import org.springframework.stereotype.Component;

/**
 * @author Yakir
 * @Topic TestAsyncService
 * @Description
 * @date 2021/12/22 10:21
 * @Version 1.0
 */
@Component
public class TestAsyncService {

	@ExceptionNotice
	public void asyncError() {
		if (true) {
			throw new RuntimeException("异步任务处理出现异常");
		}
	}

}
