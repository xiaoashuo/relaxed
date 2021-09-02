package com.relaxed.common.core.util.tree;

import java.util.List;

/**
 * @author Yakir
 * @Topic AbstractTreeNode
 * @Description 树形节点抽象
 * @date 2021/9/2 11:46
 * @Version 1.0
 */
public interface AbstractTreeNode<T> {

	/**
	 * 获取当前节点id
	 * @author yakir
	 * @date 2021/9/2 11:49
	 * @return java.lang.String
	 */
	String getNodeId();

	/**
	 * 获取父节点id
	 * @author yakir
	 * @date 2021/9/2 11:49
	 * @return java.lang.String
	 */
	String getParentId();

	/**
	 * 设置子节点列表
	 * @author yakir
	 * @date 2021/9/2 11:50
	 * @param childrenNodes
	 */
	void setChildrenNodes(List<T> childrenNodes);

}
