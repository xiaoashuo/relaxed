package com.relaxed.common.core.batch.params;

import lombok.ToString;

/**
 * @author Yakir
 * @Topic DataWrapper
 * @Description
 * @date 2022/11/14 18:17
 * @Version 1.0
 */
@ToString
public class DataWrapper<T> {

	/**
	 * 批次起始位置索引
	 */
	private int batchStartPosIndex;

	/**
	 * 当前索引位置 batchStartPosIndex+rangePosIndex
	 */
	private int currentPosIndex;

	/**
	 * 分组参数
	 */
	private BatchGroup batchGroup;

	private T data;

	public DataWrapper batchStartPosIndex(int batchStartPosIndex) {
		this.batchStartPosIndex = batchStartPosIndex;
		return this;
	}

	public DataWrapper batchGroup(BatchGroup batchGroup) {
		this.batchGroup = batchGroup;
		return this;
	}

	public DataWrapper data(T data) {
		this.data = data;
		return this;
	}

	public DataWrapper currentPosIndex(int currentPosIndex) {
		this.currentPosIndex = currentPosIndex;
		return this;
	}

	public int getCurrentPosIndex() {
		return currentPosIndex;
	}

	public int getBatchStartPosIndex() {
		return batchStartPosIndex;
	}

	public T getData() {
		return data;
	}

	public BatchGroup getBatchGroup() {
		return batchGroup;
	}

}
