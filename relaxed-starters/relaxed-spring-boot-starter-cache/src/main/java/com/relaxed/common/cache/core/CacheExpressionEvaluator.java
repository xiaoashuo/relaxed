package com.relaxed.common.cache.core;

import cn.hutool.core.util.StrUtil;
import com.relaxed.common.cache.config.CachePropertiesHolder;
import com.relaxed.common.cache.model.MetaAnnotationInfo;
import com.relaxed.common.core.util.SpELUtil;
import lombok.Data;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Yakir
 * @Topic ExpressionEvalutor
 * @Description
 * @date 2021/7/24 12:55
 * @Version 1.0
 */
@Data
public class CacheExpressionEvaluator {

	private Object target;

	private Method method;

	private Object[] args;

	/**
	 * SpEL 上下文
	 */
	EvaluationContext spElContext;

	public CacheExpressionEvaluator() {

	}

	public CacheExpressionEvaluator(Object target, Method method, Object[] arguments) {
		this.target = target;
		this.method = method;
		this.args = arguments;
		this.spElContext = SpELUtil.getSpElContext(target, method, arguments);
	}

	public boolean condition(String conditionExpression) {
		return (Boolean.TRUE
				.equals(SpELUtil.getExpression(conditionExpression).getValue(this.spElContext, Boolean.class)));
	}

	public String getKey(MetaAnnotationInfo metaAnnotationInfo) {
		String spELExpressions = metaAnnotationInfo.getKey();
		// 根据keyJoint 判断是否需要拼接
		if (spELExpressions == null || spELExpressions.length() == 0) {
			return spELExpressions;
		}
		// 获取所有需要拼接的元素, 组装进集合中
		String joint = SpELUtil.parseValueToString(spElContext, spELExpressions);
		String prefix = metaAnnotationInfo.getPrefix();
		String suffix = metaAnnotationInfo.getSuffix();
		if (!StringUtils.hasText(prefix) && !StringUtils.hasText(suffix)) {
			return joint;
		}
		return jointKey(prefix, joint, suffix);
	}

	/**
	 * 拼接key, 默认使用 ：作为分隔符
	 * @param list
	 * @return
	 */
	public String jointKey(List<String> list) {
		return String.join(CachePropertiesHolder.delimiter(), list);
	}

	/**
	 * 拼接key, 默认使用 ：作为分隔符
	 * @param items
	 * @return
	 */
	public String jointKey(String... items) {
		List<String> keyList = new ArrayList<>();
		for (String item : items) {
			if (StringUtils.hasText(item)) {
				keyList.add(item);
			}
		}
		return jointKey(keyList);
	}

}
