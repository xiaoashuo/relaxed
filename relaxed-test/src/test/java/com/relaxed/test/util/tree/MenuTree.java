package com.relaxed.test.util.tree;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Yakir
 * @Topic MenuTree
 * @Description
 * @date 2022/8/10 10:55
 * @Version 1.0
 */
@Data
public class MenuTree implements TreeNode<Integer> {

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
	private List<MenuTree> children;

	@Override
	public Integer getKey() {
		return id;
	}

	@Override
	public Integer getParentKey() {
		return parentId;
	}

	@Override
	public <T extends TreeNode<Integer>> void setChildren(List<T> children) {
		this.children = (List<MenuTree>) children;
	}

	@Override
	public <T extends TreeNode<Integer>> List<T> getChildren() {
		return (List<T>) children;
	}

}
