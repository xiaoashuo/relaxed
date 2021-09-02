package com.relaxed.common.core.util.tree;

import java.util.List;

/**
 * @author Yakir
 * @Topic AbstractTreeBuildFactory
 * @Description 树构建的抽象类，定义构建tree的基本步骤
 * @date 2021/9/2 11:51
 * @Version 1.0
 */
public interface AbstractTreeBuildFactory<T> {

	/**
	 * 树节点构建整体过程
	 * @author yakir
	 * @date 2021/9/2 11:53
	 * @param nodes 待处理节点集合
	 * @return java.util.List<T> 处理后的树形节点集合
	 */
	List<T> treeBuild(List<T> nodes);

}
