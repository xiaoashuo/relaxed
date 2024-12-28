package com.relaxed.test.excel;

import com.relaxed.autoconfigure.log.AccessLogAutoConfiguration;
import com.relaxed.autoconfigure.log.properties.LogProperties;
import com.relaxed.fastexcel.ExcelHandlerConfiguration;
import com.relaxed.fastexcel.ResponseExcelAutoConfiguration;
import com.relaxed.test.excel.controller.TestExcelController;
import com.relaxed.test.log.access.TestAccessLogHandler;
import com.relaxed.test.log.controller.TestAccessController;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;

/**
 * @author Yakir
 * @Topic TestExcelStarter
 * @Description
 * @date 2024/12/28 15:08
 * @Version 1.0
 */
@SpringBootTest(
		classes = { ServletWebServerFactoryAutoConfiguration.class, DispatcherServletAutoConfiguration.class,
				ErrorMvcAutoConfiguration.class, MultipartAutoConfiguration.class, JacksonAutoConfiguration.class,
				WebMvcAutoConfiguration.class, ExcelHandlerConfiguration.class, ResponseExcelAutoConfiguration.class,
				TestExcelController.class },
		properties = "spring.config.location=classpath:/excel/application-excel.yml",
		webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestExcelStarter {

	@SneakyThrows
	@Test
	public void testStartApp() {
		CountDownLatch countDownLatch = new CountDownLatch(1);
		countDownLatch.await();
	}

}
