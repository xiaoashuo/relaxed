package com.relaxed.test.web.exception;

import com.relaxed.common.model.result.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Yakir
 * @Topic TestController
 * @Description
 * @date 2021/12/21 14:21
 * @Version 1.0
 */
@RestController
@RequestMapping("/test")
public class TestController {

	@Autowired
	private TestService testService;

	@GetMapping("/hello")
	public R ts() {
		testService.helloNoAnnotationReturn();
		return R.ok();
	}

}
