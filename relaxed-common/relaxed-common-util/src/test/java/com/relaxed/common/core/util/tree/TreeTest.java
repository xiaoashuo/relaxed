package com.relaxed.common.core.util.tree;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.util.List;

public class TreeTest {

	public static void main(String[] args) {
		String data = TreeData.getData();
		List<Menu> menuList = JSONUtil.toList(data, Menu.class);
		DefaultTreeBuildFactory<Menu> factory = new DefaultTreeBuildFactory<>("0");
		List<Menu> menus = factory.treeBuild(menuList);
		System.out.println(menus);
	}

}
