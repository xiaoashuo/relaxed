package com.relaxed.common.core.util.batch.meta;

import lombok.Getter;
import org.springframework.util.Assert;

/**
 * @author Yakir
 * @Topic GroupMeta
 * @Description
 * @date 2023/9/22 14:58
 * @Version 1.0
 */
@Getter
public class GroupMeta {

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

	public GroupMeta(long total, int size) {
		Assert.isTrue(total > 0, "total must not be less 0");
		Assert.isTrue(size > 0, "size must not be less 0");
		this.total = total;
		this.size = size;
		// 切分数目
		long groupNum = total / size;
		this.groupNum = total % size != 0 ? groupNum + 1 : groupNum;
	}

}
