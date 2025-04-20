package com.relaxed.fastexcel.processor;

import java.lang.reflect.Method;

/**
 * Excel文件名处理器接口 用于处理Excel导出时的文件名生成 主要功能: 1. 支持动态文件名生成 2. 支持表达式解析 3. 支持方法参数注入 4. 支持自定义解析逻辑
 *
 * @author lengleng
 * @since 1.0.0
 */
public interface NameProcessor {

	/**
	 * 解析文件名 根据表达式和方法参数生成文件名 处理流程: 1. 解析表达式 2. 注入方法参数 3. 执行表达式 4. 返回结果
	 * @param args 方法参数数组
	 * @param method 当前执行的方法
	 * @param key 文件名表达式
	 * @return 解析后的文件名
	 */
	String doDetermineName(Object[] args, Method method, String key);

}
