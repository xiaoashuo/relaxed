package com.relaxed.common.log.operation.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.relaxed.common.log.operation.annotation.BizLog;
import com.relaxed.common.log.operation.aspect.LogOperatorContext;
import com.relaxed.common.log.operation.discover.FuncMeta;
import com.relaxed.common.log.operation.discover.LogRecordFuncDiscover;
import com.relaxed.common.log.operation.model.LogBizInfo;
import com.relaxed.common.log.operation.service.ILogParse;
import com.relaxed.common.log.operation.service.IOperatorGetService;
import com.relaxed.common.log.operation.spel.LogMethodResolver;
import com.relaxed.common.log.operation.spel.LogSpelEvaluationContext;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author Yakir
 * @Topic DefaultSpelParse
 * @Description
 * @date 2023/12/18 16:09
 * @Version 1.0
 */

public class DefaultLogSpelParse  implements ILogParse , BeanFactoryAware {

    private final IOperatorGetService operatorGetService;
    public DefaultLogSpelParse() {
        this(null);
    }

    public DefaultLogSpelParse(IOperatorGetService operatorGetService) {
        this.operatorGetService = operatorGetService;
    }

    /**
     * 方法参数获取
     */
    private static final ParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new LocalVariableTableParameterNameDiscoverer();
    /**
     * 实现BeanFactoryAware以获取容器中的 beanFactory对象,
     * 拿到beanFactory后便可以获取容器中的bean,用于SpEl表达式的解析
     */
    private BeanFactory beanFactory;
    private final SpelExpressionParser parser = new SpelExpressionParser();
    @Override
    public LogBizInfo beforeResolve(Object target, Method method, Object[] args, BizLog bizLog) {
        LogSpelEvaluationContext spElContext = buildSpelContext(target, method, args);
        //执行解析
        //1.判断是否需要记录日志
        String conditionSpel = bizLog.condition();
        if (StrUtil.isNotBlank(conditionSpel)){
            boolean conditionPassed = parseParamToBoolean(conditionSpel, spElContext);
            if (!conditionPassed){
                return null;
            }
        }

        String operatorSpel = bizLog.operator();
        String bizNoSpel = bizLog.bizNo();
        String typeSpel = bizLog.type();
        //2.创建业务日志
        LogBizInfo logBizInfo = new LogBizInfo();
        //解析业务日志文本
        // operatorId 处理：优先级 注解传入 > 自定义接口实现
        String operatorId=getOperatorId(operatorSpel,spElContext);
        logBizInfo.setOperator(operatorId);
        // bizId 处理：SpEL解析 必须符合表达式
        String bizId=null;
        if (StringUtils.isNotBlank(bizNoSpel)) {
            bizId = parseParamToString(bizNoSpel, spElContext);
        }
        logBizInfo.setBizNo(bizId);

        String type=null;
        if (StringUtils.isNotBlank(typeSpel)) {
            type = parseParamToString(typeSpel, spElContext);
        }
        logBizInfo.setType(type);
        return logBizInfo;
    }

    /**
     * 获取操作者id
     * 优先级  注解>自定义接口实现
     * @param operatorSpel 操作者表达式
     * @param context 上下文
     * @return 操作者id
     */
    private String getOperatorId(String operatorSpel,StandardEvaluationContext  context) {
        if (StringUtils.isNotBlank(operatorSpel)) {
            return parseParamToString(operatorSpel, context);
        }
        String operatorId;
        if (operatorGetService != null) {
            operatorId = operatorGetService.getOperatorId();
        }else{
            operatorId="unknown";
        }
        return operatorId;

    }


    @Override
    public LogBizInfo afterResolve(LogBizInfo logBizOp, Object target, Method method, Object[] args, BizLog bizLog) {
        LogSpelEvaluationContext spElContext = buildSpelContext(target, method, args);
        //格式化详情文件
        String detailExpression = bizLog.detail();
        String detailText = parseParamToStringOrJson(detailExpression, spElContext);
        logBizOp.setDetails(detailText);
        //格式化成功模板  失败模板
        String successSpel = bizLog.success();

        String successText="";
        if (StrUtil.isNotBlank(successSpel)){
            successText = parseParamToString(successSpel, spElContext);
        }
        logBizOp.setSuccessText(successText);
        String failSpel = bizLog.fail();
        String failText="";
        if (StrUtil.isNotBlank(failSpel)){
            failText = parseParamToString(failSpel, spElContext);
        }
        logBizOp.setFailText(failText);
        return logBizOp;
    }
    private LogSpelEvaluationContext buildSpelContext(Object target, Method method, Object[] args) {
        LogSpelEvaluationContext spElContext=new LogSpelEvaluationContext(target, method, args,PARAMETER_NAME_DISCOVERER);
        //注册当前方法全局变量参数
        registerGlobalParam(spElContext);
        //注册全局函数
        registerGlobalFuncs(spElContext);
        return spElContext;
    }
    /**
     * 注册全局的spel方法
     * @param spElContext
     */
    private void registerGlobalFuncs(StandardEvaluationContext spElContext) {
        //采用方法解析器形式
        spElContext.addMethodResolver(new LogMethodResolver());
// 采用注册函数形式
//        Map<String, FuncMeta> functionMap = LogRecordFuncDiscover.getFunctionMap();
//        functionMap.forEach((methodName,funcMeta)-> spElContext.registerFunction(methodName,funcMeta.getMethod()));
    }
    private boolean parseParamToBoolean(String spel, StandardEvaluationContext context) {
        Expression conditionExpression = parser.parseExpression(spel);
        return Boolean.TRUE.equals(conditionExpression.getValue(context, Boolean.class));
    }

    private String parseParamToString(String spel, StandardEvaluationContext context) {
        Expression bizIdExpression = parser.parseExpression(spel);
        return bizIdExpression.getValue(context, String.class);
    }
    private String parseParamToStringOrJson(String spel, StandardEvaluationContext context) {
        Expression msgExpression = parser.parseExpression(spel);
        Object obj = msgExpression.getValue(context, Object.class);
        if (obj != null) {
            return obj instanceof String ? (String) obj : JSONUtil.toJsonStr(obj);
        }
        return null;
    }
    /**
     * 注册全局变量参数
     * @param logRecordContext
     */
    private void registerGlobalParam(StandardEvaluationContext logRecordContext) {
        // 把 LogRecordContext 中的变量都放到 RootObject 中
        Map<String, Object> variables = LogOperatorContext.peek();
        if (variables != null && variables.size() > 0) {
            for (Map.Entry<String, Object> entry : variables.entrySet()) {
                logRecordContext.setVariable(entry.getKey(), entry.getValue());
            }
        }
        if (beanFactory != null) {
            // setBeanResolver 主要用于支持SpEL模板中调用指定类的方法，如：@XXService.x(#root)
            //如果获得工厂方法自己，bean名字须要添加&前缀而不是@
            logRecordContext.setBeanResolver(new BeanFactoryResolver(beanFactory));
        }
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory=beanFactory;
    }
}
