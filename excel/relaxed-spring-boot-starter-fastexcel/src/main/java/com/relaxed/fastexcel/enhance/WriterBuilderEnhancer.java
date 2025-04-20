package com.relaxed.fastexcel.enhance;

import cn.idev.excel.write.builder.ExcelWriterBuilder;
import cn.idev.excel.write.builder.ExcelWriterSheetBuilder;
import com.relaxed.fastexcel.annotation.ResponseExcel;
import com.relaxed.fastexcel.head.HeadGenerator;

import javax.servlet.http.HttpServletResponse;

/**
 * Excel写入构建器增强接口 用于增强Excel写入过程中的构建器功能 主要功能: 1. 支持Excel写入构建器的自定义增强 2. 支持Sheet写入构建器的自定义增强
 * 3. 支持模板文件的处理 4. 支持自定义表头生成
 *
 * @author Hccake
 * @since 1.0.0
 */
public interface WriterBuilderEnhancer {

	/**
	 * 增强Excel写入构建器 在构建Excel写入器时提供自定义增强功能
	 * @param writerBuilder Excel写入构建器
	 * @param response HTTP响应对象
	 * @param responseExcel Excel响应注解
	 * @param templatePath 模板文件路径
	 * @return 增强后的Excel写入构建器
	 */
	ExcelWriterBuilder enhanceExcel(ExcelWriterBuilder writerBuilder, HttpServletResponse response,
			ResponseExcel responseExcel, String templatePath);

	/**
	 * 增强Sheet写入构建器 在构建Sheet写入器时提供自定义增强功能
	 * @param writerSheetBuilder Sheet写入构建器
	 * @param sheetNo Sheet序号(从0开始)
	 * @param sheetName Sheet名称(使用模板时可为空)
	 * @param dataClass 数据类型Class
	 * @param template 模板文件路径
	 * @param headEnhancerClass 自定义表头生成器Class
	 * @return 增强后的Sheet写入构建器
	 */
	ExcelWriterSheetBuilder enhanceSheet(ExcelWriterSheetBuilder writerSheetBuilder, Integer sheetNo, String sheetName,
			Class<?> dataClass, String template, Class<? extends HeadGenerator> headEnhancerClass);

}
