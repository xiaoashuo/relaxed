package com.relaxed.common.log.operation.spel;

import com.relaxed.common.log.operation.constants.LogRecordConstants;
import com.relaxed.common.log.operation.discover.LogRecordFuncDiscover;
import com.relaxed.common.log.operation.exception.LogRecordException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.expression.EvaluationContext;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Yakir
 * @Topic LogFunctionParser
 * @Description
 * @date 2023/12/15 11:34
 * @Version 1.0
 */
@RequiredArgsConstructor
public class LogSpelParser implements BeanFactoryAware {
    /**
     * 实现BeanFactoryAware以获取容器中的 beanFactory对象,
     * 拿到beanFactory后便可以获取容器中的bean,用于SpEl表达式的解析
     */
    private BeanFactory beanFactory;

    /**
     * 这个正则表达式的含义为：
     * 匹配一个包含在花括号中的字符串，其中花括号中可以包含任意数量的空白字符（包括空格、制表符、换行符等），
     * 并且花括号中至少包含一个单词字符（字母、数字或下划线）。
     * =================================================
     * 具体来说，该正则表达式由两部分组成：
     * {s*(\w*)\s*}：表示匹配一个左花括号，后面跟随零个或多个空白字符，然后是一个单词字符（字母、数字或下划线）零个或多个空白字符，最后是一个右花括号。这部分用括号括起来，以便提取匹配到的内容。
     * (.*?)：表示匹配任意数量的任意字符，但尽可能少地匹配。这部分用括号括起来，以便提取匹配到的内容。
     * =================================================
     * 因此，整个正则表达式的意思是：
     * 匹配一个包含在花括号中的字符串，
     * 其中花括号中可以包含任意数量的空白字符（包括空格、制表符、换行符等），
     * 并且花括号中至少包含一个单词字符（字母、数字或下划线），并提取出花括号中的内容。
     * =================================================
     */
    private static final Pattern PATTERN = Pattern.compile("\\{\\s*(\\w*)\\s*\\{(.*?)}}");



    /**
     * 缓存表达式求值器
     */
    private final LogSpelExpressionEvaluator cachedExpressionEvaluator = new LogSpelExpressionEvaluator();

    /**
     * 处理前置函数
     *
     * @param templates 模版
     * @param method 方法
     * @param args 参数
     * @param targetClass 目标类
     * @return {@link Map}<{@link String}, {@link String}> 返回结果
     */
    public Map<String, Object> processBeforeExec(List<String> templates, Method method, Object[] args, Class<?> targetClass) {
        HashMap<String, Object> map = new HashMap<>();
        AnnotatedElementKey elementKey = new AnnotatedElementKey(method, targetClass);
        EvaluationContext evaluationContext = cachedExpressionEvaluator.createEvaluationContext(method, args, beanFactory, null, null);
        for (String template : templates) {
            if (!template.contains("{")) {
                continue;
            }
            Matcher matcher = PATTERN.matcher(template);
            while (matcher.find()) {
                String paramName = matcher.group(2);
                if (paramName.contains(LogRecordConstants.POUND_KEY + LogRecordConstants.ERR_MSG) || paramName.contains(LogRecordConstants.POUND_KEY + LogRecordConstants.RESULT)) {
                    continue;
                }
                String funcName = matcher.group(1);
                if (LogRecordFuncDiscover.isBeforeExec(funcName)) {
                    Object valueExpress = cachedExpressionEvaluator.parseExpression(paramName, elementKey, evaluationContext);

                    Object value= LogRecordFuncDiscover.invokeFunc(funcName,valueExpress == null ? null : valueExpress.toString());
                    map.put(getFunctionMapKey(funcName, paramName), value);
                }
            }
        }
        return map;
    }

    /**
     * 处理后置函数
     *
     * @param expressTemplate      待解析的模板
     * @param funcValBeforeExecMap 自定义前置函数
     * @param method               方法
     * @param args                 参数
     * @param targetClass          目标类
     * @param errMsg               错误信息
     * @param result               结果
     * @return {@link Map}<{@link String}, {@link String}> 返回结果
     */
    public Map<String, String> processAfterExec(List<String> expressTemplate, Map<String, String> funcValBeforeExecMap, Method method, Object[] args, Class<?> targetClass, String errMsg, Object result) {
        HashMap<String, String> map = new HashMap<>();
        AnnotatedElementKey elementKey = new AnnotatedElementKey(method, targetClass);
        EvaluationContext evaluationContext = cachedExpressionEvaluator.createEvaluationContext(method, args, beanFactory, errMsg, result);
        for (String template : expressTemplate) {
            if (template.contains("{")) {
                Matcher matcher = PATTERN.matcher(template);
                StringBuffer parsedStr = new StringBuffer();
                while (matcher.find()) {
                    String paramName = matcher.group(2);
                    Object value = cachedExpressionEvaluator.parseExpression(paramName, elementKey, evaluationContext);
                    String funcName = matcher.group(1);
                    String param = value == null ? "" : value.toString();
                    String functionVal = ObjectUtils.isEmpty(funcName) ? param : getFuncVal(funcValBeforeExecMap, funcName, paramName, param);
                    matcher.appendReplacement(parsedStr, functionVal);
                }
                matcher.appendTail(parsedStr);
                map.put(template, parsedStr.toString());
            } else {
                Object value;
                try {
                    value = cachedExpressionEvaluator.parseExpression(template, elementKey, evaluationContext);
                } catch (Exception e) {
                    throw new LogRecordException(method.getDeclaringClass().getName() + "." + method.getName() + "下 BizLog 解析失败: [" + template + "], 请检查是否符合SpEl表达式规范！");
                }
                map.put(template, value == null ? "" : value.toString());
            }
        }
        return map;
    }

    /**
     * 获取前置函数映射的 key
     *
     * @param funcName 方法名
     * @param param    参数
     * @return {@link String} 返回结果
     */
    private String getFunctionMapKey(String funcName, String param) {
        return funcName + param;
    }


    /**
     * 获取自定义函数值
     *
     * @param funcValBeforeExecutionMap 执行之前的函数值
     * @param funcName                  函数名
     * @param paramName                 函数参数名称
     * @param param                     函数参数
     * @return {@link String} 返回结果
     */
    public String getFuncVal(Map<String, String> funcValBeforeExecutionMap, String funcName, String paramName, String param) {
        String val = null;
        if (!CollectionUtils.isEmpty(funcValBeforeExecutionMap)) {
            val = funcValBeforeExecutionMap.get(getFunctionMapKey(funcName, paramName));
        }
        if (ObjectUtils.isEmpty(val)) {
         //   val = customFunctionService.apply(funcName, param);
            val ="";
        }

        return val;
    }
    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public static void main(String[] args) {

    }
}
