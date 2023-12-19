package com.relaxed.common.log.operation.spel;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.expression.CachedExpressionEvaluator;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Yakir
 * @Topic LogSpelExpressionEvaluator
 * @Description
 * @date 2023/12/18 13:56
 * @Version 1.0
 */
public class LogSpelExpressionEvaluator extends CachedExpressionEvaluator {
    /**
     * 缓存key
     */
    private final Map<ExpressionKey, Expression> keyCache = new ConcurrentHashMap<>(64);
    /**
     * 创建上下文
     *
     * @param method      方法
     * @param args        参数
     * @param beanFactory bean工厂
     * @param errMsg      错误信息
     * @param result      结果
     * @return {@link EvaluationContext} 返回结果
     */
    public EvaluationContext createEvaluationContext(Method method, Object[] args, BeanFactory beanFactory, String errMsg, Object result) {
        LogSpelEvaluationContext evaluationContext = new LogSpelEvaluationContext(method, args, this.getParameterNameDiscoverer());
        evaluationContext.putResult(errMsg, result);
        if (beanFactory != null) {
            // setBeanResolver 主要用于支持SpEL模板中调用指定类的方法，如：@XXService.x(#root)
            evaluationContext.setBeanResolver(new BeanFactoryResolver(beanFactory));
        }

        return evaluationContext;
    }

    public Object parseExpression(String expression, AnnotatedElementKey methodKey, EvaluationContext evalContext) {
        return this.getExpression(this.keyCache, methodKey, expression).getValue(evalContext);
    }

    void clear() {
        this.keyCache.clear();
    }

}
