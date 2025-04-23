package com.relaxed.common.core.util.tree;

import lombok.ToString;

import java.util.List;

@ToString
public class SimpleTreeNode<I> implements TreeNode<I> {

	/**
	 * 节点ID
	 */
	private I id;

	/**
	 * 父节点ID
	 */
	private I parentId;

	/**
	 * 子节点集合
	 */
	private List<SimpleTreeNode<I>> children;

	@Override
	public I getKey() {
		return id;
	}

	@Override
	public I getParentKey() {
		return parentId;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends TreeNode<I>> void setChildren(List<T> children) {
		this.children = (List<SimpleTreeNode<I>>) children;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends TreeNode<I>> List<T> getChildren() {
		return (List<T>) children;
	}

}
