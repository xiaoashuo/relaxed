package com.relaxed.poi.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 循环行表格内容数据类，用于存储 Word 文档中需要循环渲染的表格数据。 支持自定义前缀、后缀和数据起始行，支持链式调用。
 *
 * @param <T> 表格行数据的类型
 * @author Yakir
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class LoopRowTableContentData<T> extends LabelData {

	/**
	 * 表格行数据列表，用于循环渲染
	 */
	private List<T> dataList;

	/**
	 * 模板标签前缀，默认为"["
	 */
	private String prefix = "[";

	/**
	 * 模板标签后缀，默认为"]"
	 */
	private String suffix = "]";

	/**
	 * 数据起始行号，用于指定从哪一行开始渲染数据
	 */
	private Integer dataStartRowNum;

}
