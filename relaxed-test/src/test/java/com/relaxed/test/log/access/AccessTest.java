package com.relaxed.test.log.access;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.relaxed.common.log.biz.util.FlatMapUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Yakir
 * @Topic AccessTest
 * @Description
 * @date 2024/12/25 9:51
 * @Version 1.0
 */
public class AccessTest {

	public static void main(String[] args) {
		// json 体支持路径替换 form 仅支持key替换 当前未实现递归检查,可切割key 进行get获取替换
		String jsonExpression = "{\"persion\":{\"name\":\"zs\",\"datas\":[{\"id\":1},{\"id\":2}]}}";
		JSONObject jsonExpObj = JSONUtil.parseObj(jsonExpression);
		Object name = JSONUtil.getByPath(jsonExpObj, "persion.name");
		System.out.println(name);
		Object firstId = JSONUtil.getByPath(jsonExpObj, "persion.datas[0].id");
		System.out.println(firstId);
		JSONUtil.putByPath(jsonExpObj, "persion.datas[0].id", "123");
		firstId = JSONUtil.getByPath(jsonExpObj, "persion.datas[0].id");
		System.out.println(firstId);

		// 构建一个嵌套的 Map（模拟可能为空的情况）
		Map<String, Object> buildMap = buildMap();
		System.out.println(buildMap);
		// setValueByPath(buildMap,"persion.datas[0].id","123");
	}

	private static Map<String, Object> buildMap() {
		Map<String, Object> root = new HashMap<>();
		Map<String, Object> person = new HashMap<>();
		person.put("name", "zs");
		Map<String, Object> dataItem = new HashMap<>();
		dataItem.put("id", 1);
		Map<String, Object> dataItem2 = new HashMap<>();
		dataItem2.put("id", 1);

		List<Map<String, Object>> datas = new ArrayList<>();
		datas.add(dataItem); // datas[0] 存在
		datas.add(dataItem2); // datas[0] 存在

		person.put("datas", datas);
		root.put("persion", person);
		return root;
	}

}
