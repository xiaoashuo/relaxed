package com.relaxed.common.log.action;

import cn.hutool.core.util.IdUtil;
import com.relaxed.common.log.action.annotation.LogTag;
import com.relaxed.common.log.action.converter.DefaultTypeDiffConverter;
import com.relaxed.common.log.action.handler.DataHandler;
import com.relaxed.common.log.action.handler.FieldHandler;
import com.relaxed.common.log.action.handler.RecordHandler;
import com.relaxed.common.log.action.handler.impl.DefaultDataHandler;
import com.relaxed.common.log.action.handler.impl.DefaultFieldHandler;
import com.relaxed.common.log.action.handler.impl.DefaultRecordHandler;
import com.relaxed.common.log.action.properties.LogClientProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Yakir
 * @Topic LogClientTest
 * @Description
 * @date 2021/12/14 16:50
 * @Version 1.0
 */
@SpringBootApplication(scanBasePackages = { "com.relaxed.common.log.action.LogClientAutoConfiguration" })
@SpringBootTest
@Slf4j
class LogClientTest {

	@Data
	public static class TestData {

		@LogTag(alias = "主键")
		private Integer id;

		@LogTag(alias = "用户名")
		private String username;

		private String password;

		@LogTag(alias = "用户名", converter = DefaultTypeDiffConverter.class)
		private InnerData innerData;

	}

	@Data
	public static class InnerData {

		private String gender;

	}

	@Test
	public void ts() {
		RecordHandler recordHandler = new DefaultRecordHandler();
		FieldHandler fieldHandler = new DefaultFieldHandler();
		DataHandler dataHandler = new DefaultDataHandler(recordHandler, fieldHandler);
		LogClientProperties logClientProperties = new LogClientProperties();
		logClientProperties.setAppName("test");
		LogClient logClient = new LogClient(dataHandler, logClientProperties);
		String objectId = IdUtil.objectId();
		TestData testData = new TestData();
		testData.setId(1);
		testData.setUsername("张三");
		testData.setPassword("123456");
		InnerData innerData = new InnerData();
		innerData.setGender("男");
		testData.setInnerData(innerData);
		TestData testData1 = new TestData();
		testData1.setId(1);
		testData1.setUsername("李四");
		testData1.setPassword("123456");
		InnerData innerData1 = new InnerData();
		innerData1.setGender("女");
		testData1.setInnerData(innerData1);
		logClient.logObject(objectId, "张三", "created", "创建", "", "测试", testData, testData1);
	}

}