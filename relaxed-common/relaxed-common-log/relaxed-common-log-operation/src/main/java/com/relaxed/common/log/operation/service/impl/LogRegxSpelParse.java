package com.relaxed.common.log.operation.service.impl;

import cn.hutool.core.util.StrUtil;
import com.relaxed.common.log.operation.annotation.BizLog;
import com.relaxed.common.log.operation.constants.LogRecordConstants;
import com.relaxed.common.log.operation.discover.LogRecordFuncDiscover;
import com.relaxed.common.log.operation.exception.LogRecordException;
import com.relaxed.common.log.operation.model.LogBizInfo;
import com.relaxed.common.log.operation.service.ILogParse;
import com.relaxed.common.log.operation.spel.LogSeplUtil;
import com.relaxed.common.log.operation.spel.LogSpelEvaluationContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Yakir
 * @Topic LogRegxSpelParse
 * @Description
 * @date 2023/12/19 14:26
 * @Version 1.0
 */
public class LogRegxSpelParse implements ILogParse, BeanFactoryAware {
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
     * 最终匹配格式结构 {xx?{}} 此时可以固定 参数为2，函数名为1 完整字符串为0
     * 字段 {{param}}
     * 函数 {func{param}}
     */
    private static final Pattern PATTERN = Pattern.compile("\\{\\s*(\\w*)\\s*\\{(.*?)}}");
    /**
     * 实现BeanFactoryAware以获取容器中的 beanFactory对象,
     * 拿到beanFactory后便可以获取容器中的bean,用于SpEl表达式的解析
     */
    private BeanFactory beanFactory;
    @Override
    public LogBizInfo beforeResolve(Object target, Method method, Object[] args, BizLog bizLog) {
        //等待解析得模板
        List<String> waitExpressTemplate = getExpressTemplate(bizLog);
        //记录log上下文
        LogSpelEvaluationContext logRecordContext = buildSpelContext(target, method, args);
        HashMap<String, String> map = new HashMap<>();

        for (String template : waitExpressTemplate) {
            //若模板不包含{
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

                //如果为函数 且 是前置函数 则此处调用执行
                if (StrUtil.isNotBlank(funcName)&& LogRecordFuncDiscover.isBeforeExec(funcName)) {
                    Object value = LogSeplUtil.parseExpression(paramName, logRecordContext);
                 //   Object value = cachedExpressionEvaluator.parseExpression(paramName, elementKey, evaluationContext);

                    String funcVal = LogRecordFuncDiscover.invokeFuncToStr(funcName, value);
                    map.put(getFunctionMapKey(funcName, paramName), funcVal);
                }
            }
        }
        LogBizInfo logBizInfo = new LogBizInfo();
        logBizInfo.setFuncResMap(map);
        return logBizInfo;
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

    private LogSpelEvaluationContext buildSpelContext(Object target, Method method, Object[] args) {
        LogSpelEvaluationContext logRecordContext = LogSeplUtil.buildSpelContext(target, method, args);
        if (beanFactory != null) {
            // setBeanResolver 主要用于支持SpEL模板中调用指定类的方法，如：@XXService.x(#root)
            //如果获得工厂方法自己，bean名字须要添加&前缀而不是@
            logRecordContext.setBeanResolver(new BeanFactoryResolver(beanFactory));
        }
        return logRecordContext;
    }

    @Override
    public LogBizInfo afterResolve(LogBizInfo logBizOp, Object target, Method method, Object[] args, BizLog bizLog) {
        HashMap<String, String> map = new HashMap<>();
        //等待解析得模板
        List<String> waitExpressTemplate = getExpressTemplate(bizLog);
        LogSpelEvaluationContext logRecordContext = buildSpelContext(target, method, args);

        Map<String, String> funcResMap = logBizOp.getFuncResMap();

        for (String template : waitExpressTemplate) {
            //从缓存获取

            //解析后得表达式值
            String expressionValue = resolveExpression(template, logRecordContext, funcResMap);

            if (template.contains("{")) {
                Matcher matcher = PATTERN.matcher(template);
                StringBuffer parsedStr = new StringBuffer();
                while (matcher.find()) {
                    String paramName = matcher.group(2);
                    String funcName = matcher.group(1);

                    if (StrUtil.isBlank(funcName)){
                        //函数名为空 说明不是函数
                        Object value = LogSeplUtil.parseExpression(paramName,logRecordContext);
                        String param = value == null ? "" : value.toString();
                        String funcVal= param;
                        matcher.appendReplacement(parsedStr, funcVal);
                    }else{
                        //是函数
                        Object[] funcArgs=null;
                        if (StrUtil.isNotBlank(paramName)){
                            String[] paramNameExps = paramName.split(StrUtil.COMMA);
                            funcArgs=new Object[paramNameExps.length];
                            for (int i = 0; i < paramNameExps.length; i++) {
                                funcArgs[i]=LogSeplUtil.parseExpression(paramNameExps[i],logRecordContext);
                            }
                        }

                        String funcVal = getFuncVal(funcResMap, funcName, paramName, funcArgs);
                        matcher.appendReplacement(parsedStr, funcVal);
                        funcResMap.put(getFunctionMapKey(funcName,paramName),funcVal);
                    }
                }
                matcher.appendTail(parsedStr);
                map.put(template, parsedStr.toString());
            } else {
                Object value;
                try {
                    value =  LogSeplUtil.parseExpression(template,logRecordContext) ;
                } catch (Exception e) {
                    throw new LogRecordException(method.getDeclaringClass().getName() + "." + method.getName() + "下 BizLog 解析失败: [" + template + "], 请检查是否符合SpEl表达式规范！");
                }
                map.put(template, value == null ? "" : value.toString());
            }
        }
        //
        logBizOp.setFieldMap(map);
        logBizOp.setFuncResMap(funcResMap);
        return logBizOp;
    }


    public String resolveExpression(String template,LogSpelEvaluationContext logRecordContext,Map<String,String> funcCache ){

        String value;
        if (template.contains("{")) {
            //模板搜寻匹配 若模板包含 左花括号 及支持正则匹配提取
            Matcher matcher = PATTERN.matcher(template);
            StringBuffer parsedStr = new StringBuffer();
            while (matcher.find()) {
                String paramName = matcher.group(2);
                String funcName = matcher.group(1);
                //函数名为空 说明不是函数
                if (StrUtil.isBlank(funcName)){
                    String paramValue = LogSeplUtil.parseParamToString(paramName,logRecordContext);
                    matcher.appendReplacement(parsedStr, paramValue);
                }
                else{
                    //是函数 解析参数值
                    Object[] funcArgs=null;
                    if (StrUtil.isNotBlank(paramName)){
                        String[] paramNameExps = paramName.split(StrUtil.COMMA);
                        funcArgs=new Object[paramNameExps.length];
                        for (int i = 0; i < paramNameExps.length; i++) {
                            funcArgs[i]=LogSeplUtil.parseExpression(paramNameExps[i],logRecordContext);
                        }
                    }
                    String funcVal = getFuncVal(funcCache, funcName, paramName, funcArgs);
                    matcher.appendReplacement(parsedStr, funcVal);
                    funcCache.put(getFunctionMapKey(funcName,paramName),funcVal);
                }
            }
            matcher.appendTail(parsedStr);
            value = parsedStr.toString();
        }else{
            //走默认spel表达式提取
             value=LogSeplUtil.parseParamToString(template,logRecordContext);

        }
        return value;
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
    public String getFuncVal(Map<String, String> funcValBeforeExecutionMap, String funcName, String paramName, Object[] params) {
        String val = null;
        if (!CollectionUtils.isEmpty(funcValBeforeExecutionMap)) {
            val = funcValBeforeExecutionMap.get(getFunctionMapKey(funcName, paramName));
        }
        if (ObjectUtils.isEmpty(val)) {
            val = LogRecordFuncDiscover.invokeFuncToStr(funcName, params);
        }

        return val;
    }
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory=beanFactory;
    }
    /**
     * 获取不为空的待解析模板
     * 从这个List里面我们也可以知道，哪些参数需要符合SpEl表达式
     *
     * @param bizLog
     * @return
     */
    private List<String> getExpressTemplate(BizLog bizLog) {
        Set<String> set = new HashSet<>();
        set.addAll(Arrays.asList(bizLog.bizNo(), bizLog.detail(),
                bizLog.operator(), bizLog.success(), bizLog.fail(),
                bizLog.condition()));
        return set.stream().filter(s -> !ObjectUtils.isEmpty(s)).collect(Collectors.toList());
    }
}
