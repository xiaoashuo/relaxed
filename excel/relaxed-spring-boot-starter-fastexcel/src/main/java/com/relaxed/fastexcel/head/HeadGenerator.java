package com.relaxed.fastexcel.head;

/**
 * Excel表头生成器接口 用于自定义生成Excel表头信息 主要功能: 1. 支持自定义表头生成逻辑 2. 支持动态表头生成 3. 支持多级表头生成 4. 支持表头样式定制
 *
 * @author Hccake
 * @since 1.0.0
 */
public interface HeadGenerator {

	/**
	 * 生成Excel表头信息 根据数据类型生成对应的表头信息 实现方式: 1. 可以通过反射获取类的字段信息 2. 可以通过注解获取字段的显示名称 3.
	 * 可以自定义表头的层级结构 4. 可以设置表头的样式信息
	 * @param clazz 当前工作表的数据类型
	 * @return 表头元数据信息
	 * @see HeadMeta
	 */
	HeadMeta head(Class<?> clazz);

}
