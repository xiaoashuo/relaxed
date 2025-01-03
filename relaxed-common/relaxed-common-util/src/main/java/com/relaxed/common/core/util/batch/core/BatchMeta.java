package com.relaxed.common.core.util.batch.core;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Yakir
 * @Topic BaseMeta
 * @Description
 * @date 2023/9/22 17:18
 * @Version 1.0
 */

@Getter
@Setter
public class BatchMeta {

	/**
	 * 当前分组编号默认从1开始
	 */
	private Integer groupNo;

	/**
	 * 坐标定位器计算出的起始坐标
	 */
	private Integer startIndex;

	/**
	 * 每批大小
	 */
	private Integer size;

}
