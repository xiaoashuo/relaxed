package com.relaxed.common.datascope.util;

import java.util.Collection;

/**
 * 集合工具类
 * <p>
 * 提供集合相关的工具方法，用于判断集合是否为空。
 */
public final class CollectionUtils {

	private CollectionUtils() {
	}

	/**
	 * 判断集合是否为空
	 * <p>
	 * 如果集合为 null 或没有元素，则返回 true。
	 * @param collection 要检查的集合
	 * @return 如果集合为空则返回 true，否则返回 false
	 */
	public static boolean isEmpty(Collection<?> collection) {
		return collection == null || collection.isEmpty();
	}

	/**
	 * 判断集合是否不为空
	 * <p>
	 * 如果集合不为 null 且至少有一个元素，则返回 true。
	 * @param collection 要检查的集合
	 * @return 如果集合不为空则返回 true，否则返回 false
	 */
	public static boolean isNotEmpty(Collection<?> collection) {
		return !isEmpty(collection);
	}

}
