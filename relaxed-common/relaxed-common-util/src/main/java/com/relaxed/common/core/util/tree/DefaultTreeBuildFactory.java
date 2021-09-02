package com.relaxed.common.core.util.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yakir
 * @Topic DefaultTreeBuildFactory
 * @Description
 * @date 2021/9/2 11:56
 * @Version 1.0
 */
public class DefaultTreeBuildFactory<T extends AbstractTreeNode> implements AbstractTreeBuildFactory<T> {

	/**
	 * 顶级节点的父节点id(默认-1)
	 */
	private String rootParentId = "-1";

	public DefaultTreeBuildFactory() {

	}

	public DefaultTreeBuildFactory(String rootParentId) {
		this.rootParentId = rootParentId;
	}

	@Override
	public List<T> treeBuild(List<T> nodes) {
		// 将每个节点的构造一个子树
		for (T treeNode : nodes) {
			this.buildChildNodes(nodes, treeNode, new ArrayList<>());
		}
		// 只保留上级是根节点的节点，也就是只留下所有一级节点
		ArrayList<T> results = new ArrayList<>();
		for (T node : nodes) {
			if (node.getParentId().equals(rootParentId)) {
				results.add(node);
			}
		}

		return results;
	}

	/**
	 * 查询子节点的集合
	 * @param totalNodes 所有节点的集合
	 * @param node 被查询节点的id
	 * @param childNodeLists 被查询节点的子节点集合
	 */
	private void buildChildNodes(List<T> totalNodes, T node, List<T> childNodeLists) {
		if (totalNodes == null || node == null) {
			return;
		}

		List<T> nodeSubLists = getSubChildsLevelOne(totalNodes, node);

		if (nodeSubLists.size() == 0) {

		}
		else {
			for (T nodeSubList : nodeSubLists) {
				buildChildNodes(totalNodes, nodeSubList, new ArrayList<>());
			}
		}

		childNodeLists.addAll(nodeSubLists);
		node.setChildrenNodes(childNodeLists);
	}

	/**
	 * 获取子一级节点的集合
	 * @param list 所有节点的集合
	 * @param node 被查询节点的model
	 * @author fengshuonan
	 */
	private List<T> getSubChildsLevelOne(List<T> list, T node) {
		List<T> nodeList = new ArrayList<>();
		for (T nodeItem : list) {
			if (nodeItem.getParentId().equals(node.getNodeId())) {
				nodeList.add(nodeItem);
			}
		}
		return nodeList;
	}

}
