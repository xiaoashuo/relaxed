package com.relaxed.test.util.tree;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.util.List;
import java.util.function.Function;

public class TreeTest {

	public static void main(String[] args) {
		String data = TreeData.getData();
		List<Menu> menuList = JSONUtil.toList(data, Menu.class);
		Function<Menu, MenuTree> convert = menu -> BeanUtil.toBean(menu, MenuTree.class);
		List<MenuTree> menuTreeList = TreeUtils.buildTree(menuList, 0, convert);
		System.out.println(menuTreeList);
	}

}
