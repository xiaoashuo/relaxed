package com.relaxed.fastexcel.annotation;

import cn.idev.excel.read.builder.AbstractExcelReaderParameterBuilder;
import com.relaxed.fastexcel.handler.DefaultAnalysisEventListener;
import com.relaxed.fastexcel.handler.ListAnalysisEventListener;

import java.lang.annotation.*;

/**
 * 导入excel
 *
 * @author lengleng
 * @author L.cm
 * @date 2021/4/16
 */
@Documented
@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestExcel {

	/**
	 * 前端上传字段名称 file
	 */
	String fileName() default "";

	/**
	 * 读取的监听器类
	 * @return readListener
	 */
	Class<? extends ListAnalysisEventListener<?>> readListener() default DefaultAnalysisEventListener.class;

	/**
	 * 是否跳过空行
	 * @return 默认跳过
	 */
	boolean ignoreEmptyRow() default false;

	/**
	 * 工作表名称
	 * @return sheetName
	 */
	String sheetName() default "";

	/**
	 * Count the number of added heads when read sheet. 0 - This Sheet has no head ,since
	 * the first row are the data 1 - This Sheet has one row head , this is the default 2
	 * - This Sheet has two row head ,since the third row is the data
	 * @see AbstractExcelReaderParameterBuilder#headRowNumber
	 * @return headRowNumber
	 */
	int headRowNumber() default 1;

	/**
	 * The number of rows to read, the default is all, start with 0. include table header
	 * row
	 */
	int numRows() default -1;

}
