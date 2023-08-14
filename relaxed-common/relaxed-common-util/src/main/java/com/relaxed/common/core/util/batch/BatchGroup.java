package com.relaxed.common.core.util.batch;

import lombok.Getter;
import org.springframework.util.Assert;

/**
 * @author Yakir
 * @Topic BatchGroup
 * @Description
 * @date 2023/4/3 14:56
 * @Version 1.0
 */
@Getter
public class BatchGroup {

	/**
	 * 分组数
	 */
	private long groupNum;

	/**
	 * 批次大小
	 */
	private int size;

	/**
	 * 总数
	 */
	private long total;

	public BatchGroup(long total, int size) {
		Assert.isTrue(total > 0, "total must not be less 0");
		Assert.isTrue(size > 0, "size must not be less 0");
		this.total = total;
		this.size = size;
		// 切分数目
		long groupNum = total / size;
		this.groupNum = total % size != 0 ? groupNum + 1 : groupNum;
	}

}
