package com.relaxed.poi.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author Yakir
 * @Topic LoopRowTableContentData
 * @Description
 * @date 2024/3/26 10:48
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class LoopRowTableContentData<T> extends LabelData {

	/**
	 * 数据列表
	 */
	private List<T> dataList;

	/**
	 * 前缀
	 */
	private String prefix = "[";

	/**
	 * 后缀
	 */
	private String suffix = "]";

	/**
	 * 数据起始行
	 */
	private Integer dataStartRowNum;

}
