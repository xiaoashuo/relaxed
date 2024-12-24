package com.relaxed.test.log;

import cn.hutool.core.thread.ThreadUtil;
import com.relaxed.autoconfigure.log.AccessLogAutoConfiguration;
import com.relaxed.autoconfigure.log.properties.LogProperties;
import com.relaxed.common.mail.MailAutoConfiguration;
import com.relaxed.test.factory.YamlPropertySourceFactory;
import com.relaxed.test.log.access.TestAccessLogHandler;
import com.relaxed.test.log.controller.TestAccessController;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.TestPropertySource;

import java.util.concurrent.CountDownLatch;

/**
 * @author Yakir
 * @Topic TestLogApplication
 * @Description
 * @date 2024/12/24 10:00
 * @Version 1.0
 */

@SpringBootTest(
		classes = { ServletWebServerFactoryAutoConfiguration.class, DispatcherServletAutoConfiguration.class,
				ErrorMvcAutoConfiguration.class, MultipartAutoConfiguration.class, JacksonAutoConfiguration.class,
				WebMvcAutoConfiguration.class, AccessLogAutoConfiguration.class, LogProperties.class,
				TestAccessController.class, TestAccessLogHandler.class },
		properties = "spring.config.location=classpath:/log/application-access.yml",
		webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestLogApplication {

	@Test
	public void testController() {
		try {
			CountDownLatch countDownLatch = new CountDownLatch(1);
			countDownLatch.await();
			;
		}
		catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

}
