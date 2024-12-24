package com.relaxed.test.log.controller;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Yakir
 * @Topic TestAccessController
 * @Description
 * @date 2024/12/24 9:57
 * @Version 1.0
 */
@RequestMapping("/test/log/access")
@RestController
public class TestAccessController {

	@GetMapping
	public String testMethod() {
		return "Hello, World!";
	}

	@PostMapping("/form")
	public String testForm(LogForm logForm) {
		return JSONUtil.toJsonStr(logForm);
	}

	@PostMapping("/json")
	public String testJson(@RequestBody LogForm logForm) {
		ThreadUtil.sleep(3000);
		return JSONUtil.toJsonStr(logForm);
	}

}
