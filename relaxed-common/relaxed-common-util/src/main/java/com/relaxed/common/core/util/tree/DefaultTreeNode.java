package com.relaxed.common.core.util.tree;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Yakir
 * @Topic DefaultTreeNode
 * @Description
 * @date 2021/9/2 11:54
 * @Version 1.0
 */
@Data
public class DefaultTreeNode implements AbstractTreeNode<DefaultTreeNode> {

	/**
	 * 节点id
	 */
	private String id;

	/**
	 * 父节点id
	 */
	private String pId;

	/**
	 * 节点名称
	 */
	private String name;

	/**
	 * 是否打开节点
	 */
	private Boolean open;

	/**
	 * 是否被选中
	 */
	private Boolean checked = false;

	/**
	 * 排序，越小越靠前
	 */
	private BigDecimal sort;

	/**
	 * 子节点
	 */
	private List<DefaultTreeNode> children;

	@Override
	public String getNodeId() {
		return id;
	}

	@Override
	public String getParentId() {
		return pId;
	}

	@Override
	public void setChildrenNodes(List<DefaultTreeNode> childrenNodes) {
		this.children = childrenNodes;
	}

}
