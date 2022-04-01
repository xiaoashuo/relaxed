package com.relaxed.common.core.util.tree;

import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Data
public class Menu implements AbstractTreeNode<Menu> {

	/**
	 * 主键
	 */
	private Integer id;

	/**
	 * url地址
	 */
	private String url;

	/**
	 * 菜单名称
	 */
	private String name;

	/**
	 * 排序
	 */
	private Integer sort;

	/**
	 * 类型 1 目录 2菜单 3按钮
	 */
	private Integer type;

	/**
	 * 图标
	 */
	private String icon;

	/**
	 * 父级id
	 */
	private Integer parentId;

	/**
	 * 权限
	 */
	private String permission;

	/**
	 * 创建时间
	 */
	private LocalDateTime createdTime;

	/**
	 * 更新时间
	 */
	private LocalDateTime updatedTime;

	/**
	 * 子列表
	 */
	private List<Menu> children;

	@Override
	public String getNodeId() {
		return String.valueOf(id);
	}

	public String getParentId() {
		return String.valueOf(parentId);
	}

	@Override
	public void setChildrenNodes(List<Menu> childrenNodes) {
		this.children = childrenNodes;
	}

}
