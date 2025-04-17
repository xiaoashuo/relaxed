package com.relaxed.test.web.exception;

import com.relaxed.common.exception.annotation.ExceptionNotice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Yakir
 * @Topic TestService
 * @Description
 * @date 2021/12/21 10:41
 * @Version 1.0
 */
// @ExceptionNotice
@Component
public class TestService {

	@Autowired
	private TestAsyncService testAsyncService;

	@ExceptionNotice
	public void hello() {
		System.out.println("hello");
	}

	@ExceptionNotice
	public void helloThrowable() {
		System.out.println("hello exception");
		throw new RuntimeException("测试失败");
	}

	public String helloReturn() {
		System.out.println("hello");
		return "hello";
	}

	public String helloNoAnnotationReturn() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				testAsyncService.asyncError();
			}
		}).start();
		if (true) {
			throw new RuntimeException("接口调用出现异常");
		}
		return "hello";
	}

}
