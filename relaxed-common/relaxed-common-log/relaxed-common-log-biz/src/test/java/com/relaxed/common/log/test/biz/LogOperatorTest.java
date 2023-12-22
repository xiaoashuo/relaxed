package com.relaxed.common.log.test.biz;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.IdUtil;

import cn.hutool.json.JSONUtil;
import com.relaxed.common.log.biz.annotation.LogDiffTag;
import com.relaxed.common.log.biz.constant.LogRecordConstants;
import com.relaxed.common.log.biz.model.AttributeModel;
import com.relaxed.common.log.biz.model.DiffMeta;
import com.relaxed.common.log.biz.service.IDataHandler;
import com.relaxed.common.log.biz.service.IFieldHandler;
import com.relaxed.common.log.biz.service.impl.DefaultDataHandler;
import com.relaxed.common.log.biz.service.impl.DefaultFieldHandler;
import com.relaxed.common.log.test.biz.domain.LogUser;
import com.relaxed.common.log.test.biz.domain.TestData;
import com.relaxed.common.log.test.biz.service.BizLogService;
import com.relaxed.common.log.test.biz.service.UserAService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.javers.common.collections.Lists;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author Yakir
 * @Topic LogOperatorTest
 * @Description
 * @date 2023/12/14 15:09
 * @Version 1.0
 */
@Slf4j
@SpringBootApplication(scanBasePackages = "com.relaxed")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class LogOperatorTest {

	@Autowired
	private UserAService userAService;

	@Autowired
	private BizLogService bizLogService;

	@Autowired
	private IDataHandler dataHandler;

	@Test
	public void testLogRecord() {
		MDC.put(LogRecordConstants.TRACE_ID, IdUtil.objectId());
		try {
			LogUser user = getLogUser();
			// 简单方法
			// bizLogService.simpleMethod(user);
			// 上下文变量方法
			// bizLogService.simpleMethodContext(user);
			// 自定义函数方法
			// bizLogService.simpleMethodCustomFunc(user);
			// 执行前置函数
			// bizLogService.simpleMethodCustomBeforFunc(user);
			// 测试方法执行失败
			// bizLogService.simpleMethodFail(user);
			// 测试方法嵌套日志
			// bizLogService.simpleMethodNested(user);
			// 差异方法
			bizLogService.simpleMethodDiff(user);

		}
		finally {
			MDC.remove(LogRecordConstants.TRACE_ID);
		}

	}

	@Test
	public void diffCompare() {
		IFieldHandler fieldHandler = new IFieldHandler() {
			@Override
			public AttributeModel extractAttributeModel(Field field, LogDiffTag logTag, Object oldFieldValue,
					Object newFieldValue) {
				return new DefaultFieldHandler().extractAttributeModel(field, logTag, oldFieldValue, newFieldValue);
			}

			@Override
			public boolean ignoreField(Class clazzType, Field field, Object oldFieldValue, Object newFieldValue) {
				// 若为字符串或基本类型 内容相同 则不进行记录
				Class<?> fieldType = field.getType();
				if ((String.class.isAssignableFrom(fieldType) || ClassUtil.isBasicType(fieldType))
						&& oldFieldValue.equals(newFieldValue)) {
					return true;
				}
				return false;
			}
		};
		IDataHandler dataHandler = new DefaultDataHandler(fieldHandler);

		String objectId = IdUtil.objectId();
		String expected = "{\"username\":\"张三\",\"version\":\"1.0.0\",\"content\":[{\"lineNumber\":1,\"gender\":\"女\"}]}";
		String actual = "{\"jack\":\"张三\",\"version\":\"1.0.0\",\"content\":[{\"lineNumber\":2,\"age\":\"18\"}]}";
		TestData oldValue = buildTestData1("张三", "男", expected, false);
		TestData newValue = buildTestData1("李四", "女", actual, true);
		List<AttributeModel> attributeModelList = dataHandler.diffObject(new DiffMeta("diffUser", oldValue, newValue));
		log.info("差异化信息{}", JSONUtil.toJsonStr(attributeModelList));
	}

	private TestData buildTestData1(String username, String gender, String jsonParam, boolean randomInnerData) {

		TestData testData = new TestData();
		testData.setId(1);
		testData.setUsername(username);
		testData.setPassword("123456");
		TestData.InnerData innerData = new TestData.InnerData();
		innerData.setGender(gender);
		TestData.InnerDataText innerDataText = new TestData.InnerDataText();
		innerDataText.setData(randomInnerData ? IdUtil.simpleUUID() : null);
		innerDataText.setTeachers(Lists.asList(new TestData.Teacher(IdUtil.simpleUUID())));
		innerData.setInnerDataText(innerDataText);
		testData.setInnerData(innerData);
		testData.setJsonParam(jsonParam);
		testData.setRichText(getHtmlText());
		return testData;
	}

	private String getHtmlText() {
		String objectId = IdUtil.objectId();
		String oldStr = "<html>\n" + "\n" + "<head>\n" + "<title>我的第一个 HTML 页面</title>\n" + "</head>\n" + "\n"
				+ "<body>\n" + "<p>body 元素的内容会显示在浏览器中。</p>\n" + "<p>body 随机值" + objectId + "。</p>\n"
				+ "<p>title 元素的内容会显示在浏览器的标题栏中。</p>\n" + "</body>\n" + "\n" + "</html>\n";
		return oldStr;
	}

	@Test
	public void testModify() {
		LogUser user = getLogUser();
		userAService.sendGoods(user);

	}

	private static LogUser getLogUser() {
		LogUser user = new LogUser();
		user.setUsername("张三");
		user.setStatus(1);
		user.setBizNo(IdUtil.getSnowflakeNextIdStr());
		return user;
	}

	public static String qu(String username) {
		return "123";
	}

	@SneakyThrows
	public static void main(String[] args) {

		// CusIParseFunc cusIParseFunc = new CusIParseFunc();
		// Method orgMethod = cusIParseFunc.getClass().getMethod("apply",Object[].class);
		// Method[] methods = cusIParseFunc.getClass().getMethods();
		//
		// Class<?>[] parameterTypes = orgMethod.getParameterTypes();
		// Method method = cusIParseFunc.getClass().getMethod("apply", parameterTypes);
		//
		// Object[] arguments = {1, 2, 3};;
		// Object result = LogClassUtil.invokeRaw(cusIParseFunc,method, arguments);
		// System.out.println(result);
		//
		// Object orgResult =
		// LogClassUtil.invokeRaw(cusIParseFunc,orgMethod,(Object)arguments);
		// System.out.println(orgResult);
		//
		// Method orgListMethod =
		// cusIParseFunc.getClass().getMethod("applyList",String.class, String[].class);
		// Object listresult = LogClassUtil.invokeRaw(cusIParseFunc,orgListMethod,"a",
		// arguments);
		// System.out.println(listresult);

	}

}
