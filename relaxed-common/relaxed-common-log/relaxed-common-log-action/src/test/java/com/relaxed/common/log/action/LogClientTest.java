package com.relaxed.common.log.action;

import cn.hutool.core.util.IdUtil;
import com.relaxed.common.log.action.annotation.LogTag;
import com.relaxed.common.log.action.converter.SimpleTypeDiffExtractor;
import com.relaxed.common.log.action.converter.json.JsonTypeExtractor;
import com.relaxed.common.log.action.converter.richtext.RichTextTypeExtractor;
import com.relaxed.common.log.action.handler.DataHandler;
import com.relaxed.common.log.action.handler.FieldHandler;
import com.relaxed.common.log.action.handler.RecordHandler;
import com.relaxed.common.log.action.handler.impl.DefaultDataHandler;
import com.relaxed.common.log.action.handler.impl.DefaultFieldHandler;
import com.relaxed.common.log.action.handler.impl.DefaultRecordHandler;
import com.relaxed.common.log.action.properties.LogClientProperties;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

class LogClientTest {

	Logger log = LoggerFactory.getLogger(LogClientTest.class);

	@Data
	public static class TestData {

		@LogTag(alias = "主键")
		private Integer id;

		@LogTag(alias = "用户名")
		private String username;

		private String password;

		@LogTag(alias = "用户名", extractor = SimpleTypeDiffExtractor.class)
		private InnerData innerData;

		@LogTag(alias = "json参数", extractor = JsonTypeExtractor.class)
		private String jsonParam;

		/**
		 * 富文本
		 */
		@LogTag(alias = "富文本", extractor = RichTextTypeExtractor.class)
		private String richText;

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
		String expected = "{\"username\":\"张三\",\"version\":\"1.0.0\",\"content\":[{\"lineNumber\":1,\"gender\":\"女\"}]}";
		String actual = "{\"jack\":\"张三\",\"version\":\"1.0.0\",\"content\":[{\"lineNumber\":2,\"age\":\"18\"}]}";
		TestData oldValue = buildTestData1("张三", "男", expected);
		TestData newValue = buildTestData1("李四", "女", actual);

		logClient.logObject(IdUtil.simpleUUID(), objectId, "张三", "created", "创建", "", "测试", oldValue, newValue);
		log.info("上报结束");
	}

	private String getHtmlText() {
		String objectId = IdUtil.objectId();
		String oldStr = "<html>\n" + "\n" + "<head>\n" + "<title>我的第一个 HTML 页面</title>\n" + "</head>\n" + "\n"
				+ "<body>\n" + "<p>body 元素的内容会显示在浏览器中。</p>\n" + "<p>body 随机值" + objectId + "。</p>\n"
				+ "<p>title 元素的内容会显示在浏览器的标题栏中。</p>\n" + "</body>\n" + "\n" + "</html>\n";
		return oldStr;
	}

	private TestData buildTestData1(String username, String gender, String jsonParam) {

		TestData testData = new TestData();
		testData.setId(1);
		testData.setUsername(username);
		testData.setPassword("123456");
		InnerData innerData = new InnerData();
		innerData.setGender(gender);
		testData.setInnerData(innerData);
		testData.setJsonParam(jsonParam);
		testData.setRichText(getHtmlText());
		return testData;
	}

}