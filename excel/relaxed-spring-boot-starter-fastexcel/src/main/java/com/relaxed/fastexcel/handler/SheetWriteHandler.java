package com.relaxed.fastexcel.handler;

import com.relaxed.fastexcel.annotation.ResponseExcel;

import javax.servlet.http.HttpServletResponse;

/**
 * Excel工作表写入处理器接口 用于处理Excel工作表的写入操作 主要功能: 1. 支持不同类型数据的写入判断 2. 提供数据校验机制 3. 支持自定义导出逻辑 4.
 * 支持自定义写入逻辑
 *
 * @author lengleng
 * @since 1.0.0
 */
public interface SheetWriteHandler {

	/**
	 * 判断是否支持处理指定对象 用于确定当前处理器是否能处理该数据类型
	 * @param obj 待处理的对象
	 * @return true表示支持,false表示不支持
	 */
	boolean support(Object obj);

	/**
	 * 校验导出配置 在导出之前对配置进行校验,确保导出参数的正确性
	 * @param responseExcel Excel响应注解,包含导出配置信息
	 * @throws IllegalArgumentException 当校验失败时抛出
	 */
	void check(ResponseExcel responseExcel);

	/**
	 * 执行Excel导出操作 包括准备数据、写入Excel、输出响应等完整流程
	 * @param o 待导出的数据对象
	 * @param response HTTP响应对象
	 * @param responseExcel Excel响应注解
	 */
	void export(Object o, HttpServletResponse response, ResponseExcel responseExcel);

	/**
	 * 执行Excel写入操作 具体的Excel数据写入实现
	 * @param o 待写入的数据对象
	 * @param response HTTP响应对象
	 * @param responseExcel Excel响应注解
	 */
	void write(Object o, HttpServletResponse response, ResponseExcel responseExcel);

}
