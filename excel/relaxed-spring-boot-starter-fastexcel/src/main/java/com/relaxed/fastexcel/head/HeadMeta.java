package com.relaxed.fastexcel.head;

import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * Excel表头元数据类 用于存储Excel表头的相关信息 主要功能: 1. 存储表头信息 2. 存储需要忽略的字段 3. 支持多级表头结构 4. 支持字段过滤
 *
 * @author Yakir
 * @since 1.0.0
 */
@Data
public class HeadMeta {

	/**
	 * 表头信息 格式说明: 1. 外层List表示表头的行数 2. 内层List表示每行的列数 3. String表示表头单元格的内容 示例: [ ["姓名", "年龄",
	 * "性别"], ["基本信息", "基本信息", "基本信息"] ]
	 */
	private List<List<String>> head;

	/**
	 * 需要忽略的字段名称集合 这些字段将不会出现在Excel中 主要用于: 1. 过滤敏感信息 2. 排除不需要导出的字段 3. 动态控制字段显示
	 */
	private Set<String> ignoreHeadFields;

}
