package com.relaxed.common.core.util.batch.core;

import com.relaxed.common.core.util.batch.BatchUtil;
import org.springframework.util.Assert;

/**
 * 批量任务的分组配置类。 负责计算和管理批量任务的分组信息，包括： 1. 总数据量 2. 每批次大小 3. 分组数量
 *
 * 分组数量的计算规则： - 如果总数据量能被批次大小整除，分组数 = 总数据量 / 批次大小 - 如果不能整除，分组数 = (总数据量 / 批次大小) + 1
 *
 * @author Yakir
 * @since 1.0
 */
public class BatchGroup {

	/**
	 * 父级BatchUtil实例的引用
	 */
	private BatchUtil _parent;

	/**
	 * 分组数量 由总数据量和批次大小计算得出
	 */
	private long groupNum;

	/**
	 * 每批次处理的数据量
	 */
	private int size;

	/**
	 * 待处理的总数据量
	 */
	private long totalCount;

	/**
	 * 构造函数
	 * @param parent 父级BatchUtil实例
	 */
	public BatchGroup(BatchUtil parent) {
		this._parent = parent;
	}

	/**
	 * 设置总数据量
	 * @param totalCount 总数据量，必须大于0
	 * @return 当前BatchGroup实例
	 */
	public BatchGroup totalCount(long totalCount) {
		this.totalCount = totalCount;
		return this;
	}

	/**
	 * 设置每批次大小
	 * @param size 批次大小，必须大于0
	 * @return 当前BatchGroup实例
	 */
	public BatchGroup size(int size) {
		this.size = size;
		return this;
	}

	/**
	 * 获取分组数量
	 * @return 计算得到的分组数量
	 */
	public long getGroupNum() {
		return groupNum;
	}

	/**
	 * 获取每批次大小
	 * @return 批次大小
	 */
	public int getSize() {
		return size;
	}

	/**
	 * 获取总数据量
	 * @return 总数据量
	 */
	public long getTotalCount() {
		return totalCount;
	}

	/**
	 * 结束分组配置，计算分组数量并返回父级BatchUtil实例
	 * @return 父级BatchUtil实例
	 * @throws IllegalArgumentException 当totalCount或size小于等于0时抛出
	 */
	public BatchUtil end() {
		Assert.isTrue(totalCount > 0, "total must not be less 0");
		Assert.isTrue(size > 0, "size must not be less 0");
		// 切分数目
		long groupNum = totalCount / size;
		this.groupNum = totalCount % size != 0 ? groupNum + 1 : groupNum;
		return _parent;
	}

}
