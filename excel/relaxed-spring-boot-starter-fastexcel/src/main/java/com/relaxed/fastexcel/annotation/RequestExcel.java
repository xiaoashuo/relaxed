package com.relaxed.fastexcel.annotation;

import cn.idev.excel.read.builder.AbstractExcelReaderParameterBuilder;
import com.relaxed.fastexcel.handler.DefaultAnalysisEventListener;
import com.relaxed.fastexcel.handler.ListAnalysisEventListener;

import java.lang.annotation.*;

/**
 * Excel导入注解 用于标记Controller方法参数,表示该参数接收Excel文件上传 支持配置文件名、读取监听器、是否跳过空行等属性
 *
 * @author lengleng
 * @author L.cm
 * @since 1.0.0
 */
@Documented
@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestExcel {

	/**
	 * 前端上传Excel文件的字段名称 默认为空字符串,表示使用默认字段名
	 * @return 字段名称
	 */
	String fileName() default "";

	/**
	 * Excel读取监听器类 用于自定义Excel数据的读取和处理逻辑
	 * @return 监听器类
	 */
	Class<? extends ListAnalysisEventListener<?>> readListener() default DefaultAnalysisEventListener.class;

	/**
	 * 是否跳过空行 设置为true时,将跳过Excel中的空行
	 * @return 是否跳过空行,默认为false
	 */
	boolean ignoreEmptyRow() default false;

	/**
	 * 要读取的工作表名称 如果为空字符串,则读取第一个工作表
	 * @return 工作表名称
	 */
	String sheetName() default "";

	/**
	 * 表头行数 0 - 表示没有表头,第一行即为数据 1 - 表示有一行表头(默认值) 2 - 表示有两行表头,第三行开始为数据
	 *
	 * @see AbstractExcelReaderParameterBuilder#headRowNumber
	 * @return 表头行数
	 */
	int headRowNumber() default 1;

	/**
	 * 要读取的数据行数 -1 - 表示读取所有行(默认值) 0或正整数 - 表示读取指定行数 注意:行数包含表头行
	 * @return 要读取的行数
	 */
	int numRows() default -1;

}
