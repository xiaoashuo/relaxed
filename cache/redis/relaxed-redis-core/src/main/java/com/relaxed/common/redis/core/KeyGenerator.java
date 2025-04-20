package com.relaxed.common.redis.core;

import cn.hutool.core.lang.Assert;
import com.relaxed.common.core.util.SpELUtil;
import com.relaxed.common.redis.config.CachePropertiesHolder;
import org.springframework.expression.EvaluationContext;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Redis缓存键生成器。 用于根据SpEL表达式生成Redis缓存的键，支持前缀和动态拼接。
 *
 * @author Yakir
 * @since 1.0
 */
public class KeyGenerator {

	/**
	 * SpEL表达式上下文
	 */
	EvaluationContext spelContext;

	/**
	 * 构造函数
	 * @param target 目标对象
	 * @param method 目标方法
	 * @param arguments 方法参数
	 */
	public KeyGenerator(Object target, Method method, Object[] arguments) {
		this.spelContext = SpELUtil.getSpElContext(target, method, arguments);
	}

	/**
	 * 根据前缀和拼接元素获取完整的缓存键
	 * @param keyPrefix 键前缀
	 * @param keyJoint 键拼接元素，值为SpEL表达式，可为空
	 * @return 完整的缓存键
	 */
	public String getKey(String keyPrefix, String keyJoint) {
		// 根据 keyJoint 判断是否需要拼接
		if (keyJoint == null || keyJoint.length() == 0) {
			return keyPrefix;
		}
		// 获取所有需要拼接的元素, 组装进集合中
		String joint = SpELUtil.parseValueToString(spelContext, keyJoint);
		Assert.notNull(joint, "Key joint cannot be null!");

		if (!StringUtils.hasText(keyPrefix)) {
			return joint;
		}
		// 拼接后返回
		return jointKey(keyPrefix, joint);
	}

	/**
	 * 根据前缀和拼接元素获取多个缓存键
	 * @param keyPrefix 键前缀
	 * @param keyJoint 键拼接元素，值为SpEL表达式，必须解析为集合
	 * @return 缓存键列表
	 */
	public List<String> getKeys(String keyPrefix, String keyJoint) {
		// keyJoint 必须有值
		Assert.notEmpty(keyJoint, "[getKeys] keyJoint cannot be null");

		// 获取所有需要拼接的元素, 组装进集合中
		List<String> joints = SpELUtil.parseValueToStringList(spelContext, keyJoint);
		Assert.notEmpty(joints, "[getKeys] keyJoint must be resolved to a non-empty collection!");

		if (!StringUtils.hasText(keyPrefix)) {
			return joints;
		}
		// 拼接后返回
		return joints.stream().map(x -> jointKey(keyPrefix, x)).collect(Collectors.toList());
	}

	/**
	 * 使用默认分隔符拼接键
	 * @param keyItems 用于拼接键的元素列表
	 * @return 拼接完成的键
	 */
	public String jointKey(List<String> keyItems) {
		return String.join(CachePropertiesHolder.delimiter(), keyItems);
	}

	/**
	 * 使用默认分隔符拼接键
	 * @param keyItems 用于拼接键的元素数组
	 * @return 拼接完成的键
	 */
	public String jointKey(String... keyItems) {
		return jointKey(Arrays.asList(keyItems));
	}

	/**
	 * 判断SpEL条件表达式是否成立
	 * @param conditionExpression SpEL条件表达式
	 * @return 表达式是否成立
	 */
	public boolean condition(String conditionExpression) {
		return (Boolean.TRUE
				.equals(SpELUtil.getExpression(conditionExpression).getValue(this.spelContext, Boolean.class)));
	}

}
