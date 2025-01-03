package com.relaxed.common.core.util.batch.core;

import com.relaxed.common.core.util.batch.BatchExecutor;
import org.springframework.util.Assert;

/**
 * @author Yakir
 * @Topic BatchGroup
 * @Description
 * @date 2025/1/2 14:18
 * @Version 1.0
 */

public class BatchGroup {

	private BatchExecutor _parent;

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
	private long totalCount;

	public BatchGroup(BatchExecutor parent) {
		this._parent = parent;
	}

	public BatchGroup totalCount(long totalCount) {
		this.totalCount = totalCount;
		return this;
	}

	public BatchGroup size(int size) {
		this.size = size;
		return this;
	}

	public long getGroupNum() {
		return groupNum;
	}

	public int getSize() {
		return size;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public BatchExecutor end() {
		Assert.isTrue(totalCount > 0, "total must not be less 0");
		Assert.isTrue(size > 0, "size must not be less 0");
		// 切分数目
		long groupNum = totalCount / size;
		this.groupNum = totalCount % size != 0 ? groupNum + 1 : groupNum;
		return _parent;
	}

}
