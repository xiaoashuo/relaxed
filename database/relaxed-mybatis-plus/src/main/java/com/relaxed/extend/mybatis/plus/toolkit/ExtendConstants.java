package com.relaxed.extend.mybatis.plus.toolkit;

/**
 * MyBatis-Plus 扩展常量定义接口
 * <p>
 * 该接口定义了在 MyBatis-Plus 扩展功能中使用的常量值。 这些常量主要用于在查询构建、参数处理等场景中作为标识符或键值使用。
 *
 * @author Yakir
 */
public interface ExtendConstants {

	/**
	 * 集合参数标识符
	 * <p>
	 * 在 MyBatis 参数处理中，用于标识集合类型的参数。 主要用于 IN 查询等场景下的参数处理。
	 */
	String COLLECTION = "collection";

}
