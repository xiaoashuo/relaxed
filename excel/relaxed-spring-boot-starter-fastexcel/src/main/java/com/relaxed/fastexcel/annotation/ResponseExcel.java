package com.relaxed.fastexcel.annotation;

import cn.idev.excel.converters.Converter;
import cn.idev.excel.support.ExcelTypeEnum;
import cn.idev.excel.write.handler.WriteHandler;
import com.relaxed.fastexcel.head.HeadGenerator;

import java.lang.annotation.*;

/**
 * Excel导出响应注解 用于标记Controller方法,表示该方法返回Excel文件 支持配置文件名、文件类型、密码、工作表、模板等属性
 *
 * @author lengleng
 * @since 1.0.0
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ResponseExcel {

	/**
	 * Excel文件名称 支持SPEL表达式,默认为空字符串
	 * @return 文件名称
	 */
	String name() default "";

	/**
	 * Excel文件类型 支持xlsx和xls两种格式,默认为xlsx
	 * @return 文件类型
	 */
	ExcelTypeEnum suffix() default ExcelTypeEnum.XLSX;

	/**
	 * Excel文件密码 用于加密Excel文件,默认为空字符串
	 * @return 文件密码
	 */
	String password() default "";

	/**
	 * 工作表配置 支持配置多个工作表,默认为一个名为"sheet1"的工作表
	 * @return 工作表配置数组
	 */
	Sheet[] sheets() default @Sheet(sheetName = "sheet1");

	/**
	 * 是否在内存中操作 true - 在内存中操作,适合小数据量 false - 使用临时文件,适合大数据量(默认)
	 * @return 是否在内存中操作
	 */
	boolean inMemory() default false;

	/**
	 * Excel模板文件路径 用于基于模板导出Excel,默认为空字符串
	 * @return 模板文件路径
	 */
	String template() default "";

	/**
	 * 包含的字段列表 指定需要导出的字段,如果为空则导出所有字段
	 * @return 包含的字段列表
	 */
	String[] include() default {};

	/**
	 * 排除的字段列表 指定不需要导出的字段
	 * @return 排除的字段列表
	 */
	String[] exclude() default {};

	/**
	 * Excel写入处理器 用于自定义Excel的写入逻辑,如样式设置等
	 * @return 写入处理器类数组
	 */
	Class<? extends WriteHandler>[] writeHandler() default {};

	/**
	 * 类型转换器 用于自定义字段值的转换逻辑
	 * @return 转换器类数组
	 */
	Class<? extends Converter>[] converter() default {};

	/**
	 * 表头生成器 用于自定义表头的生成逻辑
	 * @return 表头生成器类
	 */
	Class<? extends HeadGenerator> headGenerator() default HeadGenerator.class;

}
