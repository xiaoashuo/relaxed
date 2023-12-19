package com.relaxed.common.log.test.operator;

import cn.hutool.core.util.IdUtil;
import com.relaxed.common.log.operation.discover.func.FuncMeta;
import com.relaxed.common.log.operation.discover.func.LogRecordFuncDiscover;
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
import java.util.Map;

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
    public void testModify(){
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

    public static String qu(String username){
        return "123";
    }
    @SneakyThrows
    public static void main(String[] args) {
        // spel 表达式 https://itmyhome.com/spring/expressions.html#expressions-language-ref
        Method method = LogOperatorTest.class.getMethod("qu",String.class);
        User user = new User();
        user.setUsername("张三");
        user.setStatus(1);

        ExpressionParser expressionParser = new SpelExpressionParser();

        // 创建上下文
        StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
        evaluationContext.registerFunction("queryState",method);
        // 设置root变量
        evaluationContext.setRootObject(user);
        // 添加自定义变量（非root变量）
        evaluationContext.setVariable("username", "zhangsan");
        evaluationContext.setVariable("age", 24);
        String spel = "'将'+{queryState{username}}";
        TemplateParserContext context = new TemplateParserContext("{","}");

        Expression expression = expressionParser.parseExpression(spel, context);
        String value = expression.getValue(evaluationContext, String.class);
        System.out.println(value);
    }


}
