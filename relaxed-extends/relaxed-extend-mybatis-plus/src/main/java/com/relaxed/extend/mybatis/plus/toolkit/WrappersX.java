package com.relaxed.extend.mybatis.plus.toolkit;

import com.relaxed.extend.mybatis.plus.conditions.query.LambdaAliasQueryWrapperX;
import com.relaxed.extend.mybatis.plus.conditions.query.LambdaQueryWrapperX;
import com.relaxed.extend.mybatis.plus.conditions.query.TableAliasFunction;
import com.relaxed.extend.mybatis.plus.conditions.update.LambdaUpdateWrapperX;

/**
 * @author Hccake 2021/1/14
 * @version 1.0
 */
public final class WrappersX {

	private WrappersX() {
	}

	/**
	 * 获取 LambdaQueryWrapperX&lt;T&gt;
	 * @param <T> 实体类泛型
	 * @return LambdaQueryWrapperX&lt;T&gt;
	 */
	public static <T> LambdaQueryWrapperX<T> lambdaQueryX() {
		return new LambdaQueryWrapperX<>();
	}

	/**
	 * 获取 LambdaQueryWrapperX&lt;T&gt;
	 * @param entity 实体类
	 * @param <T> 实体类泛型
	 * @return LambdaQueryWrapperX&lt;T&gt;
	 */
	public static <T> LambdaQueryWrapperX<T> lambdaQueryX(T entity) {
		return new LambdaQueryWrapperX<>(entity);
	}

	/**
	 * 获取 LambdaQueryWrapperX&lt;T&gt;
	 * @param entityClass 实体类class
	 * @param <T> 实体类泛型
	 * @return LambdaQueryWrapperX&lt;T&gt;
	 * @since 3.3.1
	 */
	public static <T> LambdaQueryWrapperX<T> lambdaQueryX(Class<T> entityClass) {
		return new LambdaQueryWrapperX<>(entityClass);
	}

	/**
	 * 获取 LambdaUpdateWrapperX&lt;T&gt;
	 * @param <T> 实体类泛型
	 * @return LambdaQueryWrapperX&lt;T&gt;
	 */
	public static <T> LambdaUpdateWrapperX<T> lambdaUpdateX() {
		return new LambdaUpdateWrapperX<>();
	}

	/**
	 * 获取 LambdaQueryWrapperX&lt;T&gt;
	 * @param entity 实体类
	 * @param <T> 实体类泛型
	 * @return LambdaQueryWrapperX&lt;T&gt;
	 */
	public static <T> LambdaUpdateWrapperX<T> lambdaUpdateX(T entity) {
		return new LambdaUpdateWrapperX<>(entity);
	}

	/**
	 * 获取 LambdaUpdateWrapperX&lt;T&gt;
	 * @param entityClass 实体类class
	 * @param <T> 实体类泛型
	 * @return LambdaQueryWrapperX&lt;T&gt;
	 * @since 3.3.1
	 */
	public static <T> LambdaUpdateWrapperX<T> lambdaUpdateX(Class<T> entityClass) {
		return new LambdaUpdateWrapperX<>(entityClass);
	}

	/**
	 * 获取 LambdaAliasQueryWrapper&lt;T&gt;
	 * @param entity 实体类
	 * @param <T> 实体类泛型
	 * @return LambdaAliasQueryWrapper&lt;T&gt;
	 */
	public static <T> LambdaAliasQueryWrapperX<T> lambdaAliasQueryX(T entity) {
		return new LambdaAliasQueryWrapperX<>(entity);
	}

	/**
	 * 获取表别名自定义
	 * @author yakir
	 * @date 2022/4/12 10:48
	 * @param entity
	 * @param tableAliasFunction
	 * @return com.relaxed.extend.mybatis.plus.conditions.query.LambdaAliasQueryWrapperX<T>
	 */
	public static <T> LambdaAliasQueryWrapperX<T> lambdaAliasQueryX(T entity, TableAliasFunction tableAliasFunction) {
		return new LambdaAliasQueryWrapperX<>(entity, tableAliasFunction);
	}

	/**
	 * 获取 LambdaAliasQueryWrapper&lt;T&gt;
	 * @param entityClass 实体类class
	 * @param <T> 实体类泛型
	 * @return LambdaAliasQueryWrapper&lt;T&gt;
	 * @since 3.3.1
	 */
	public static <T> LambdaAliasQueryWrapperX<T> lambdaAliasQueryX(Class<T> entityClass) {
		return new LambdaAliasQueryWrapperX<>(entityClass);
	}

	/**
	 * 获取表别名自定义
	 * @author yakir
	 * @date 2022/4/12 10:48
	 * @param entityClass
	 * @param tableAliasFunction
	 * @return com.relaxed.extend.mybatis.plus.conditions.query.LambdaAliasQueryWrapperX<T>
	 */
	public static <T> LambdaAliasQueryWrapperX<T> lambdaAliasQueryX(Class<T> entityClass,
			TableAliasFunction tableAliasFunction) {
		return new LambdaAliasQueryWrapperX<>(entityClass, tableAliasFunction);
	}

}
