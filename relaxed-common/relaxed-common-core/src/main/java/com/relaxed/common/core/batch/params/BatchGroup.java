package com.relaxed.common.core.batch.params;

import lombok.Data;
import org.springframework.util.Assert;

/**
 * @author Yakir
 * @Topic BatchPage
 * @Description
 * @date 2021/7/9 12:40
 * @Version 1.0
 */
@Data
public class BatchGroup {

	/**
	 * 分组数
	 */
	private int groupNum;

	/**
	 * 批次大小
	 */
	private int size;

	/**
	 * 总数
	 */
	private int total;

	public BatchGroup(int total, int size) {
		Assert.isTrue(total > 0, "total must not be less 0");
		Assert.isTrue(size > 0, "size must not be less 0");
		this.total = total;
		this.size = size;
		// 切分数目
		int groupNum = total / size;
		this.groupNum = total % size != 0 ? groupNum + 1 : groupNum;
	}

}
