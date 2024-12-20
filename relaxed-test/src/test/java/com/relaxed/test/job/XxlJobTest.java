package com.relaxed.test.job;

import cn.hutool.core.thread.ThreadUtil;
import com.relaxed.common.job.XxlJobAutoConfiguration;
import com.relaxed.common.job.annotation.EnableXxlJob;
import com.relaxed.common.job.properties.XxlJobProperties;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.logging.LoggingApplicationListener;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.concurrent.CountDownLatch;

/**
 * @author Yakir
 * @Topic XxlJobTest
 * @Description
 * @date 2024/12/19 17:32
 * @Version 1.0
 */
@Slf4j
@SpringBootTest(classes = { XxlJobProperties.class, LogXxlJob.class, XxlJobAutoConfiguration.class })
@TestPropertySource(locations = { "classpath:/job/application-job.properties" })
// @ExtendWith(SpringExtension.class)
// @ContextConfiguration(classes = {XxlJobProperties.class,
// LogXxlJob.class,XxlJobAutoConfiguration.class
// })
public class XxlJobTest {

	@Autowired
	XxlJobProperties xxlJobProperties;

	@SneakyThrows
	@Test
	public void testLogTask() {
		log.info("xxl-job属性:{}", xxlJobProperties);
		CountDownLatch countDownLatch = new CountDownLatch(1);
		countDownLatch.await();
	}

}
