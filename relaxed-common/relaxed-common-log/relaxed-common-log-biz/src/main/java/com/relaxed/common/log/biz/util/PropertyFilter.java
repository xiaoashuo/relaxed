package com.relaxed.common.log.biz.util;

import org.javers.core.diff.Change;
import org.javers.core.diff.Diff;

import java.util.List;

/**
 * @author Yakir
 * @Topic PropertyFilter
 * @Description
 * @date 2024/2/29 16:06
 * @Version 1.0
 */
public interface PropertyFilter {

	/**
	 * 是否忽略当前属性差异提取
	 * @param change 变化
	 * @param source 原字段类
	 * @param target 目标字段类
	 * @return true 忽略属性 false 加入差异结果集
	 */
	boolean ignoreProperty(Change change, Class source, Class target);

}
