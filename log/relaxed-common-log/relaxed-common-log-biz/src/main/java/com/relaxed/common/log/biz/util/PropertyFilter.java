package com.relaxed.common.log.biz.util;

import com.relaxed.common.log.biz.model.AttributeChange;
import org.javers.core.diff.Change;
import org.javers.core.diff.Diff;

import java.util.List;

/**
 * 属性过滤器接口 该接口用于在对象比较时过滤属性变更 主要功能包括： 1. 根据属性变更信息决定是否保留该变更 2. 支持基于源对象和目标对象类型的过滤 3.
 * 提供自定义的过滤逻辑实现
 *
 * @author Yakir
 */
@FunctionalInterface
public interface PropertyFilter {

	/**
	 * 过滤属性变更 根据属性变更信息和对象类型决定是否保留该变更
	 * @param attributeChange 属性变更信息
	 * @param sourceClass 源对象类型
	 * @param targetClass 目标对象类型
	 * @return 如果保留该变更返回 true，否则返回 false
	 */
	boolean filterProperty(AttributeChange attributeChange, Class<?> sourceClass, Class<?> targetClass);

}
