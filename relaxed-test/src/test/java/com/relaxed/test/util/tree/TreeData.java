package com.relaxed.test.util.tree;

public class TreeData {

	public static String getData() {
		return data;
	}

	public static String data = "[{\n" + "\t\"id\": 1,\n" + "\t\"url\": \"/main/system\",\n" + "\t\"name\": \"系统管理\",\n"
			+ "\t\"sort\": 2,\n" + "\t\"type\": 1,\n" + "\t\"parentId\": 0,\n" + "\t\"permission\": \"\",\n"
			+ "\t\"createdTime\": \"2022-03-25 14:21:37\",\n" + "\t\"updatedTime\": \"2022-03-25 14:21:37\"\n"
			+ "}, {\n" + "\t\"id\": 2,\n" + "\t\"url\": \"/main/system/user\",\n" + "\t\"name\": \"用户管理\",\n"
			+ "\t\"sort\": 3,\n" + "\t\"type\": 2,\n" + "\t\"parentId\": 1,\n" + "\t\"permission\": \"\",\n"
			+ "\t\"createdTime\": \"2022-03-25 14:22:20\",\n" + "\t\"updatedTime\": \"2022-03-25 14:22:20\"\n"
			+ "}, {\n" + "\t\"id\": 3,\n" + "\t\"url\": \"/main/system/department\",\n" + "\t\"name\": \"部门管理\",\n"
			+ "\t\"sort\": 3,\n" + "\t\"type\": 2,\n" + "\t\"parentId\": 1,\n" + "\t\"permission\": \"\",\n"
			+ "\t\"createdTime\": \"2022-03-25 14:31:47\",\n" + "\t\"updatedTime\": \"2022-03-25 14:31:47\"\n"
			+ "}, {\n" + "\t\"id\": 4,\n" + "\t\"url\": \"/main/system/menu\",\n" + "\t\"name\": \"菜单管理\",\n"
			+ "\t\"sort\": 103,\n" + "\t\"type\": 2,\n" + "\t\"parentId\": 1,\n" + "\t\"permission\": \"\",\n"
			+ "\t\"createdTime\": \"2022-03-25 14:37:12\",\n" + "\t\"updatedTime\": \"2022-03-25 14:37:12\"\n"
			+ "}, {\n" + "\t\"id\": 5,\n" + "\t\"url\": \"\",\n" + "\t\"name\": \"创建用户\",\n" + "\t\"sort\": 1,\n"
			+ "\t\"type\": 3,\n" + "\t\"parentId\": 2,\n" + "\t\"permission\": \"system:users:create\",\n"
			+ "\t\"createdTime\": \"2022-03-25 14:26:05\",\n" + "\t\"updatedTime\": \"2022-03-25 14:26:05\"\n"
			+ "}, {\n" + "\t\"id\": 6,\n" + "\t\"url\": \"\",\n" + "\t\"name\": \"删除用户\",\n" + "\t\"sort\": 2,\n"
			+ "\t\"type\": 3,\n" + "\t\"parentId\": 2,\n" + "\t\"permission\": \"system:users:delete\",\n"
			+ "\t\"createdTime\": \"2022-03-25 14:26:32\",\n" + "\t\"updatedTime\": \"2022-03-25 14:26:32\"\n"
			+ "}, {\n" + "\t\"id\": 7,\n" + "\t\"url\": \"\",\n" + "\t\"name\": \"修改用户\",\n" + "\t\"sort\": 3,\n"
			+ "\t\"type\": 3,\n" + "\t\"parentId\": 2,\n" + "\t\"permission\": \"system:users:update\",\n"
			+ "\t\"createdTime\": \"2022-03-25 14:26:55\",\n" + "\t\"updatedTime\": \"2022-03-25 14:26:55\"\n"
			+ "}, {\n" + "\t\"id\": 8,\n" + "\t\"url\": \"\",\n" + "\t\"name\": \"查询用户\",\n" + "\t\"sort\": 4,\n"
			+ "\t\"type\": 3,\n" + "\t\"parentId\": 2,\n" + "\t\"permission\": \"system:users:query\",\n"
			+ "\t\"createdTime\": \"2022-03-25 14:27:25\",\n" + "\t\"updatedTime\": \"2022-03-25 14:27:25\"\n"
			+ "}, {\n" + "\t\"id\": 9,\n" + "\t\"url\": \"/main/product\",\n" + "\t\"name\": \"商品中心\",\n"
			+ "\t\"sort\": 1,\n" + "\t\"type\": 1,\n" + "\t\"parentId\": 0,\n" + "\t\"permission\": \"\",\n"
			+ "\t\"createdTime\": \"2022-03-25 14:44:59\",\n" + "\t\"updatedTime\": \"2022-03-25 14:44:59\"\n"
			+ "}, {\n" + "\t\"id\": 10,\n" + "\t\"url\": \"\",\n" + "\t\"name\": \"删除故事\",\n" + "\t\"sort\": 1,\n"
			+ "\t\"type\": 3,\n" + "\t\"parentId\": 43,\n" + "\t\"permission\": \"system:story:delete\",\n"
			+ "\t\"createdTime\": \"2022-03-25 14:56:30\",\n" + "\t\"updatedTime\": \"2022-03-25 14:56:30\"\n"
			+ "}, {\n" + "\t\"id\": 11,\n" + "\t\"url\": \"\",\n" + "\t\"name\": \"修改故事\",\n" + "\t\"sort\": 1,\n"
			+ "\t\"type\": 3,\n" + "\t\"parentId\": 43,\n" + "\t\"permission\": \"system:story:update\",\n"
			+ "\t\"createdTime\": \"2022-03-25 14:57:01\",\n" + "\t\"updatedTime\": \"2022-03-25 14:57:01\"\n"
			+ "}, {\n" + "\t\"id\": 12,\n" + "\t\"url\": \"\",\n" + "\t\"name\": \"查询故事\",\n" + "\t\"sort\": 1,\n"
			+ "\t\"type\": 3,\n" + "\t\"parentId\": 43,\n" + "\t\"permission\": \"system:story:query\",\n"
			+ "\t\"createdTime\": \"2022-03-25 14:57:30\",\n" + "\t\"updatedTime\": \"2022-03-25 14:57:30\"\n"
			+ "}, {\n" + "\t\"id\": 15,\n" + "\t\"url\": \"/main/product/category\",\n" + "\t\"name\": \"商品类别\",\n"
			+ "\t\"sort\": 1,\n" + "\t\"type\": 2,\n" + "\t\"parentId\": 9,\n" + "\t\"permission\": \"\",\n"
			+ "\t\"createdTime\": \"2022-03-25 14:47:10\",\n" + "\t\"updatedTime\": \"2022-03-25 14:47:10\"\n"
			+ "}, {\n" + "\t\"id\": 16,\n" + "\t\"url\": \"/main/product/goods\",\n" + "\t\"name\": \"商品信息\",\n"
			+ "\t\"sort\": 1,\n" + "\t\"type\": 2,\n" + "\t\"parentId\": 9,\n" + "\t\"permission\": \"\",\n"
			+ "\t\"createdTime\": \"2022-03-25 14:51:21\",\n" + "\t\"updatedTime\": \"2022-03-25 14:51:21\"\n"
			+ "}, {\n" + "\t\"id\": 17,\n" + "\t\"url\": \"\",\n" + "\t\"name\": \"创建部门\",\n" + "\t\"sort\": 1,\n"
			+ "\t\"type\": 3,\n" + "\t\"parentId\": 3,\n" + "\t\"permission\": \"system:department:create\",\n"
			+ "\t\"createdTime\": \"2022-03-25 14:32:42\",\n" + "\t\"updatedTime\": \"2022-03-25 14:32:42\"\n"
			+ "}, {\n" + "\t\"id\": 18,\n" + "\t\"url\": \"\",\n" + "\t\"name\": \"删除部门\",\n" + "\t\"sort\": 2,\n"
			+ "\t\"type\": 3,\n" + "\t\"parentId\": 3,\n" + "\t\"permission\": \"system:department:delete\",\n"
			+ "\t\"createdTime\": \"2022-03-25 14:33:24\",\n" + "\t\"updatedTime\": \"2022-03-25 14:33:24\"\n"
			+ "}, {\n" + "\t\"id\": 19,\n" + "\t\"url\": \"\",\n" + "\t\"name\": \"修改部门\",\n" + "\t\"sort\": 3,\n"
			+ "\t\"type\": 3,\n" + "\t\"parentId\": 3,\n" + "\t\"permission\": \"system:department:update\",\n"
			+ "\t\"createdTime\": \"2022-03-25 14:33:38\",\n" + "\t\"updatedTime\": \"2022-03-25 14:33:38\"\n"
			+ "}, {\n" + "\t\"id\": 20,\n" + "\t\"url\": \"\",\n" + "\t\"name\": \"查询部门\",\n" + "\t\"sort\": 4,\n"
			+ "\t\"type\": 3,\n" + "\t\"parentId\": 3,\n" + "\t\"permission\": \"system:department:query\",\n"
			+ "\t\"createdTime\": \"2022-03-25 14:35:02\",\n" + "\t\"updatedTime\": \"2022-03-25 14:35:02\"\n"
			+ "}, {\n" + "\t\"id\": 21,\n" + "\t\"url\": \"\",\n" + "\t\"name\": \"创建菜单\",\n" + "\t\"sort\": 1,\n"
			+ "\t\"type\": 3,\n" + "\t\"parentId\": 4,\n" + "\t\"permission\": \"system:menu:create\",\n"
			+ "\t\"createdTime\": \"2022-03-25 14:37:55\",\n" + "\t\"updatedTime\": \"2022-03-25 14:37:55\"\n"
			+ "}, {\n" + "\t\"id\": 22,\n" + "\t\"url\": \"\",\n" + "\t\"name\": \"删除菜单\",\n" + "\t\"sort\": 1,\n"
			+ "\t\"type\": 3,\n" + "\t\"parentId\": 4,\n" + "\t\"permission\": \"system:menu:delete\",\n"
			+ "\t\"createdTime\": \"2022-03-25 14:38:30\",\n" + "\t\"updatedTime\": \"2022-03-25 14:38:30\"\n"
			+ "}, {\n" + "\t\"id\": 23,\n" + "\t\"url\": \"\",\n" + "\t\"name\": \"修改菜单\",\n" + "\t\"sort\": 1,\n"
			+ "\t\"type\": 3,\n" + "\t\"parentId\": 4,\n" + "\t\"permission\": \"system:menu:update\",\n"
			+ "\t\"createdTime\": \"2022-03-25 14:39:55\",\n" + "\t\"updatedTime\": \"2022-03-25 14:39:55\"\n"
			+ "}, {\n" + "\t\"id\": 24,\n" + "\t\"url\": \"\",\n" + "\t\"name\": \"查询菜单\",\n" + "\t\"sort\": 1,\n"
			+ "\t\"type\": 3,\n" + "\t\"parentId\": 4,\n" + "\t\"permission\": \"system:menu:query\",\n"
			+ "\t\"createdTime\": \"2022-03-25 14:40:24\",\n" + "\t\"updatedTime\": \"2022-03-25 14:40:24\"\n"
			+ "}, {\n" + "\t\"id\": 25,\n" + "\t\"url\": \"/main/system/role\",\n" + "\t\"name\": \"角色管理\",\n"
			+ "\t\"sort\": 1,\n" + "\t\"type\": 2,\n" + "\t\"parentId\": 1,\n" + "\t\"permission\": \"\",\n"
			+ "\t\"createdTime\": \"2022-03-25 14:41:21\",\n" + "\t\"updatedTime\": \"2022-03-25 14:41:21\"\n"
			+ "}, {\n" + "\t\"id\": 26,\n" + "\t\"url\": \"\",\n" + "\t\"name\": \"创建角色\",\n" + "\t\"sort\": 1,\n"
			+ "\t\"type\": 3,\n" + "\t\"parentId\": 25,\n" + "\t\"permission\": \"system:role:create\",\n"
			+ "\t\"createdTime\": \"2022-03-25 14:41:56\",\n" + "\t\"updatedTime\": \"2022-03-25 14:41:56\"\n"
			+ "}, {\n" + "\t\"id\": 27,\n" + "\t\"url\": \"\",\n" + "\t\"name\": \"删除角色\",\n" + "\t\"sort\": 1,\n"
			+ "\t\"type\": 3,\n" + "\t\"parentId\": 25,\n" + "\t\"permission\": \"system:role:delete\",\n"
			+ "\t\"createdTime\": \"2022-03-25 14:42:16\",\n" + "\t\"updatedTime\": \"2022-03-25 14:42:16\"\n"
			+ "}, {\n" + "\t\"id\": 28,\n" + "\t\"url\": \"\",\n" + "\t\"name\": \"修改角色\",\n" + "\t\"sort\": 1,\n"
			+ "\t\"type\": 3,\n" + "\t\"parentId\": 25,\n" + "\t\"permission\": \"system:role:update\",\n"
			+ "\t\"createdTime\": \"2022-03-25 14:42:40\",\n" + "\t\"updatedTime\": \"2022-03-25 14:42:40\"\n"
			+ "}, {\n" + "\t\"id\": 29,\n" + "\t\"url\": \"\",\n" + "\t\"name\": \"查询角色\",\n" + "\t\"sort\": 1,\n"
			+ "\t\"type\": 3,\n" + "\t\"parentId\": 25,\n" + "\t\"permission\": \"system:role:query\",\n"
			+ "\t\"createdTime\": \"2022-03-25 14:42:58\",\n" + "\t\"updatedTime\": \"2022-03-25 14:42:58\"\n"
			+ "}, {\n" + "\t\"id\": 30,\n" + "\t\"url\": \"\",\n" + "\t\"name\": \"创建类别\",\n" + "\t\"sort\": 1,\n"
			+ "\t\"type\": 3,\n" + "\t\"parentId\": 15,\n" + "\t\"permission\": \"system:category:create\",\n"
			+ "\t\"createdTime\": \"2022-03-25 14:48:56\",\n" + "\t\"updatedTime\": \"2022-03-25 14:48:56\"\n"
			+ "}, {\n" + "\t\"id\": 31,\n" + "\t\"url\": \"\",\n" + "\t\"name\": \"删除类别\",\n" + "\t\"sort\": 1,\n"
			+ "\t\"type\": 3,\n" + "\t\"parentId\": 15,\n" + "\t\"permission\": \"system:category:delete\",\n"
			+ "\t\"createdTime\": \"2022-03-25 14:49:20\",\n" + "\t\"updatedTime\": \"2022-03-25 14:49:20\"\n"
			+ "}, {\n" + "\t\"id\": 32,\n" + "\t\"url\": \"\",\n" + "\t\"name\": \"修改类别\",\n" + "\t\"sort\": 1,\n"
			+ "\t\"type\": 3,\n" + "\t\"parentId\": 15,\n" + "\t\"permission\": \"system:category:update\",\n"
			+ "\t\"createdTime\": \"2022-03-25 14:50:00\",\n" + "\t\"updatedTime\": \"2022-03-25 14:50:00\"\n"
			+ "}, {\n" + "\t\"id\": 33,\n" + "\t\"url\": \"\",\n" + "\t\"name\": \"查询类别\",\n" + "\t\"sort\": 1,\n"
			+ "\t\"type\": 3,\n" + "\t\"parentId\": 15,\n" + "\t\"permission\": \"system:category:query\",\n"
			+ "\t\"createdTime\": \"2022-03-25 14:50:12\",\n" + "\t\"updatedTime\": \"2022-03-25 14:50:12\"\n"
			+ "}, {\n" + "\t\"id\": 34,\n" + "\t\"url\": \"\",\n" + "\t\"name\": \"创建商品\",\n" + "\t\"sort\": 1,\n"
			+ "\t\"type\": 3,\n" + "\t\"parentId\": 16,\n" + "\t\"permission\": \"system:goods:create\",\n"
			+ "\t\"createdTime\": \"2022-03-25 14:52:11\",\n" + "\t\"updatedTime\": \"2022-03-25 14:52:11\"\n"
			+ "}, {\n" + "\t\"id\": 35,\n" + "\t\"url\": \"\",\n" + "\t\"name\": \"删除商品\",\n" + "\t\"sort\": 1,\n"
			+ "\t\"type\": 3,\n" + "\t\"parentId\": 16,\n" + "\t\"permission\": \"system:goods:delete\",\n"
			+ "\t\"createdTime\": \"2022-03-25 14:52:35\",\n" + "\t\"updatedTime\": \"2022-03-25 14:52:35\"\n"
			+ "}, {\n" + "\t\"id\": 36,\n" + "\t\"url\": \"\",\n" + "\t\"name\": \"修改商品\",\n" + "\t\"sort\": 1,\n"
			+ "\t\"type\": 3,\n" + "\t\"parentId\": 16,\n" + "\t\"permission\": \"system:goods:update\",\n"
			+ "\t\"createdTime\": \"2022-03-25 14:52:52\",\n" + "\t\"updatedTime\": \"2022-03-25 14:52:52\"\n"
			+ "}, {\n" + "\t\"id\": 37,\n" + "\t\"url\": \"\",\n" + "\t\"name\": \"查询商品\",\n" + "\t\"sort\": 1,\n"
			+ "\t\"type\": 3,\n" + "\t\"parentId\": 16,\n" + "\t\"permission\": \"system:goods:query\",\n"
			+ "\t\"createdTime\": \"2022-03-25 14:53:11\",\n" + "\t\"updatedTime\": \"2022-03-25 14:53:11\"\n"
			+ "}, {\n" + "\t\"id\": 38,\n" + "\t\"url\": \"/main/analysis\",\n" + "\t\"name\": \"系统总览\",\n"
			+ "\t\"sort\": 1,\n" + "\t\"type\": 1,\n" + "\t\"parentId\": 0,\n" + "\t\"permission\": \"\",\n"
			+ "\t\"createdTime\": \"2022-03-25 14:03:09\",\n" + "\t\"updatedTime\": \"2022-03-25 14:03:09\"\n"
			+ "}, {\n" + "\t\"id\": 39,\n" + "\t\"url\": \"/main/analysis/overview\",\n" + "\t\"name\": \"核心技术\",\n"
			+ "\t\"sort\": 106,\n" + "\t\"type\": 2,\n" + "\t\"parentId\": 38,\n" + "\t\"permission\": \"\",\n"
			+ "\t\"createdTime\": \"2022-03-25 14:03:50\",\n" + "\t\"updatedTime\": \"2022-03-25 14:03:50\"\n"
			+ "}, {\n" + "\t\"id\": 40,\n" + "\t\"url\": \"/main/analysis/dashboard\",\n" + "\t\"name\": \"商品统计\",\n"
			+ "\t\"sort\": 106,\n" + "\t\"type\": 2,\n" + "\t\"parentId\": 38,\n" + "\t\"permission\": \"\",\n"
			+ "\t\"createdTime\": \"2022-03-25 14:04:16\",\n" + "\t\"updatedTime\": \"2022-03-25 14:04:16\"\n"
			+ "}, {\n" + "\t\"id\": 41,\n" + "\t\"url\": \"/main/story\",\n" + "\t\"name\": \"随便聊聊\",\n"
			+ "\t\"sort\": 1,\n" + "\t\"type\": 1,\n" + "\t\"parentId\": 0,\n" + "\t\"permission\": \"\",\n"
			+ "\t\"createdTime\": \"2022-03-25 14:54:04\",\n" + "\t\"updatedTime\": \"2022-03-25 14:54:04\"\n"
			+ "}, {\n" + "\t\"id\": 42,\n" + "\t\"url\": \"/main/story/chat\",\n" + "\t\"name\": \"你的故事\",\n"
			+ "\t\"sort\": 1,\n" + "\t\"type\": 2,\n" + "\t\"parentId\": 41,\n" + "\t\"permission\": \"\",\n"
			+ "\t\"createdTime\": \"2022-03-25 14:54:55\",\n" + "\t\"updatedTime\": \"2022-03-25 14:54:55\"\n"
			+ "}, {\n" + "\t\"id\": 43,\n" + "\t\"url\": \"/main/story/list\",\n" + "\t\"name\": \"故事列表\",\n"
			+ "\t\"sort\": 1,\n" + "\t\"type\": 2,\n" + "\t\"parentId\": 41,\n" + "\t\"permission\": \"\",\n"
			+ "\t\"createdTime\": \"2022-03-25 14:55:35\",\n" + "\t\"updatedTime\": \"2022-03-25 14:55:35\"\n" + "}]";

}
