package com.relaxed.autoconfigure.web.exception;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Yakir
 * @Topic GlobalExceptionHandlerResolverTest
 * @Description
 * @date 2021/12/21 14:01
 * @Version 1.0
 */
@SpringBootApplication(scanBasePackages = "com.relaxed")
@SpringBootTest
@ActiveProfiles("web")
@Slf4j
class GlobalExceptionHandlerResolverTest {

	// @Autowired
	// private MockMvc mockMvc;
	@Autowired
	private TestService testService;

	@Test
	public void helloAop() {
		try {
			testService.hello();
			String helloReturn = testService.helloReturn();
			log.info("返回值" + helloReturn);
			testService.helloThrowable();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}