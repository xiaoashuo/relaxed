package com.relaxed.extend.mybatis.plus.toolkit;

import com.relaxed.extend.mybatis.plus.conditions.query.LambdaAliasQueryWrapperX;
import com.relaxed.extend.mybatis.plus.conditions.query.LambdaQueryWrapperX;
import com.relaxed.extend.mybatis.plus.conditions.query.TableAliasFunction;
import com.relaxed.extend.mybatis.plus.conditions.update.LambdaUpdateWrapperX;

/**
 * MyBatis-Plus 扩展条件构造器工具类
 * <p>
 * 提供了创建增强版的 Lambda 查询构造器的静态工厂方法。 支持以下类型的条件构造器：
 * <ul>
 * <li>LambdaQueryWrapperX - 增强的 Lambda 查询构造器</li>
 * <li>LambdaUpdateWrapperX - 增强的 Lambda 更新构造器</li>
 * <li>LambdaAliasQueryWrapperX - 支持表别名的 Lambda 查询构造器</li>
 * </ul>
 *
 * @author Hccake
 */
public final class WrappersX {

	/**
	 * 私有构造方法，防止实例化
	 */
	private WrappersX() {
	}

	/**
	 * 获取增强的 Lambda 查询构造器
	 * <p>
	 * 创建一个空的查询构造器，用于构建查询条件
	 * @param <T> 实体类泛型
	 * @return 返回增强的 Lambda 查询构造器
	 */
	public static <T> LambdaQueryWrapperX<T> lambdaQueryX() {
		return new LambdaQueryWrapperX<>();
	}

	/**
	 * 获取增强的 Lambda 查询构造器
	 * <p>
	 * 根据实体对象创建查询构造器，实体对象的非空属性会作为等值查询条件
	 * @param entity 实体对象
	 * @param <T> 实体类泛型
	 * @return 返回增强的 Lambda 查询构造器
	 */
	public static <T> LambdaQueryWrapperX<T> lambdaQueryX(T entity) {
		return new LambdaQueryWrapperX<>(entity);
	}

	/**
	 * 获取增强的 Lambda 查询构造器
	 * <p>
	 * 根据实体类 Class 创建查询构造器
	 * @param entityClass 实体类 Class
	 * @param <T> 实体类泛型
	 * @return 返回增强的 Lambda 查询构造器
	 * @since 3.3.1
	 */
	public static <T> LambdaQueryWrapperX<T> lambdaQueryX(Class<T> entityClass) {
		return new LambdaQueryWrapperX<>(entityClass);
	}

	/**
	 * 获取增强的 Lambda 更新构造器
	 * <p>
	 * 创建一个空的更新构造器，用于构建更新条件和设置更新字段
	 * @param <T> 实体类泛型
	 * @return 返回增强的 Lambda 更新构造器
	 */
	public static <T> LambdaUpdateWrapperX<T> lambdaUpdateX() {
		return new LambdaUpdateWrapperX<>();
	}

	/**
	 * 获取增强的 Lambda 更新构造器
	 * <p>
	 * 根据实体对象创建更新构造器，实体对象的非空属性会作为等值更新条件
	 * @param entity 实体对象
	 * @param <T> 实体类泛型
	 * @return 返回增强的 Lambda 更新构造器
	 */
	public static <T> LambdaUpdateWrapperX<T> lambdaUpdateX(T entity) {
		return new LambdaUpdateWrapperX<>(entity);
	}

	/**
	 * 获取增强的 Lambda 更新构造器
	 * <p>
	 * 根据实体类 Class 创建更新构造器
	 * @param entityClass 实体类 Class
	 * @param <T> 实体类泛型
	 * @return 返回增强的 Lambda 更新构造器
	 * @since 3.3.1
	 */
	public static <T> LambdaUpdateWrapperX<T> lambdaUpdateX(Class<T> entityClass) {
		return new LambdaUpdateWrapperX<>(entityClass);
	}

	/**
	 * 获取支持表别名的 Lambda 查询构造器
	 * <p>
	 * 根据实体对象创建查询构造器，支持设置表别名
	 * @param entity 实体对象
	 * @param <T> 实体类泛型
	 * @return 返回支持表别名的 Lambda 查询构造器
	 */
	public static <T> LambdaAliasQueryWrapperX<T> lambdaAliasQueryX(T entity) {
		return new LambdaAliasQueryWrapperX<>(entity);
	}

	/**
	 * 获取支持表别名的 Lambda 查询构造器
	 * <p>
	 * 根据实体对象和表别名函数创建查询构造器
	 * @param <T> 实体对象
	 * @param entity 实体对象
	 * @param tableAliasFunction 表别名生成函数
	 * @return 返回支持表别名的 Lambda 查询构造器
	 */
	public static <T> LambdaAliasQueryWrapperX<T> lambdaAliasQueryX(T entity, TableAliasFunction tableAliasFunction) {
		return new LambdaAliasQueryWrapperX<>(entity, tableAliasFunction);
	}

	/**
	 * 获取支持表别名的 Lambda 查询构造器
	 * <p>
	 * 根据实体类 Class 创建查询构造器
	 * @param entityClass 实体类 Class
	 * @param <T> 实体类泛型
	 * @return 返回支持表别名的 Lambda 查询构造器
	 * @since 3.3.1
	 */
	public static <T> LambdaAliasQueryWrapperX<T> lambdaAliasQueryX(Class<T> entityClass) {
		return new LambdaAliasQueryWrapperX<>(entityClass);
	}

	/**
	 * 获取支持表别名的 Lambda 查询构造器
	 * <p>
	 * 根据实体类 Class 和表别名函数创建查询构造器
	 * @param <T> 实体对象
	 * @param entityClass 实体类 Class
	 * @param tableAliasFunction 表别名生成函数
	 * @return 返回支持表别名的 Lambda 查询构造器
	 */
	public static <T> LambdaAliasQueryWrapperX<T> lambdaAliasQueryX(Class<T> entityClass,
			TableAliasFunction tableAliasFunction) {
		return new LambdaAliasQueryWrapperX<>(entityClass, tableAliasFunction);
	}

}
