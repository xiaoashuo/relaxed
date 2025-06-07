package com.relaxed.test.download;

import com.relaxed.autoconfigure.log.AccessLogAutoConfiguration;
import com.relaxed.autoconfigure.log.properties.LogProperties;
import com.relaxed.common.jsch.sftp.SftpAutoConfiguration;
import com.relaxed.common.oss.s3.OssClientAutoConfiguration;
import com.relaxed.starter.download.ResponseDownloadAutoConfiguration;
import com.relaxed.starter.download.ResponseDownloadHandler;
import com.relaxed.test.download.controller.DownloadController;
import com.relaxed.test.log.access.TestAccessLogHandler;
import com.relaxed.test.log.controller.TestAccessController;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.HttpEncodingAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;

/**
 * TestDownloadApplication
 *
 * @author Yakir
 */
@SpringBootTest(
		classes = { ServletWebServerFactoryAutoConfiguration.class, DispatcherServletAutoConfiguration.class,
				ErrorMvcAutoConfiguration.class, MultipartAutoConfiguration.class, JacksonAutoConfiguration.class,
				WebMvcAutoConfiguration.class, HttpEncodingAutoConfiguration.class,
				ResponseDownloadAutoConfiguration.class, ResponseDownloadHandler.class, DownloadController.class,
				SftpAutoConfiguration.class, OssClientAutoConfiguration.class, },
		properties = { "spring.config.location=classpath:/sftp/application-sftp.yml",
				"spring.config.location=classpath:/oss/application-oss.yml" },
		webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestDownloadApplication {

	@Test
	public void testDownloadController() {
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
