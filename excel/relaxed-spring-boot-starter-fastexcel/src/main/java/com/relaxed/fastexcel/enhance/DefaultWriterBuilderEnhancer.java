package com.relaxed.fastexcel.enhance;

import cn.idev.excel.write.builder.ExcelWriterBuilder;
import cn.idev.excel.write.builder.ExcelWriterSheetBuilder;
import com.relaxed.fastexcel.annotation.ResponseExcel;
import com.relaxed.fastexcel.head.HeadGenerator;

import javax.servlet.http.HttpServletResponse;

/**
 * 默认Excel写入构建器增强实现 提供基础的Excel写入构建器增强功能 主要特点: 1. 提供默认的空实现 2. 可作为自定义增强器的基类 3. 保持原有构建器功能不变
 * 4. 支持按需覆盖增强方法
 *
 * @author Hccake
 * @since 1.0.0
 */
public class DefaultWriterBuilderEnhancer implements WriterBuilderEnhancer {

	/**
	 * 增强Excel写入构建器 默认实现不做任何增强,直接返回原构建器
	 * @param writerBuilder Excel写入构建器
	 * @param response HTTP响应对象
	 * @param responseExcel Excel响应注解
	 * @param templatePath 模板文件路径
	 * @return 原Excel写入构建器
	 */
	@Override
	public ExcelWriterBuilder enhanceExcel(ExcelWriterBuilder writerBuilder, HttpServletResponse response,
			ResponseExcel responseExcel, String templatePath) {
		// doNothing
		return writerBuilder;
	}

	/**
	 * 增强Sheet写入构建器 默认实现不做任何增强,直接返回原构建器
	 * @param writerSheetBuilder Sheet写入构建器
	 * @param sheetNo Sheet序号(从0开始)
	 * @param sheetName Sheet名称(使用模板时可为空)
	 * @param dataClass 数据类型Class
	 * @param template 模板文件路径
	 * @param headEnhancerClass 自定义表头生成器Class
	 * @return 原Sheet写入构建器
	 */
	@Override
	public ExcelWriterSheetBuilder enhanceSheet(ExcelWriterSheetBuilder writerSheetBuilder, Integer sheetNo,
			String sheetName, Class<?> dataClass, String template, Class<? extends HeadGenerator> headEnhancerClass) {
		// doNothing
		return writerSheetBuilder;
	}

}
