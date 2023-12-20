package com.relaxed.common.log.test.operator;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.relaxed.common.log.operation.discover.func.FuncMeta;
import com.relaxed.common.log.operation.discover.func.LogRecordFuncDiscover;
import com.relaxed.common.log.operation.util.LogClassUtil;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Yakir
 * @Topic LogOperatorTest
 * @Description
 * @date 2023/12/14 15:09
 * @Version 1.0
 */

@SpringBootApplication(scanBasePackages = "com.relaxed")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class LogOperatorTest {

	@Autowired
	private UserAService userAService;

	@Test
	public void testModify() {
		User user = new User();
		user.setUsername("张三");
		user.setStatus(1);
		user.setBizNo(IdUtil.getSnowflakeNextIdStr());
		userAService.sendGoods(user);

		Map<String, FuncMeta> functionMap = LogRecordFuncDiscover.getFunctionMap();
		FuncMeta testAnnotation = functionMap.get("testAnnotation");
		Method method = testAnnotation.getMethod();
		Object target = testAnnotation.getTarget();

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
