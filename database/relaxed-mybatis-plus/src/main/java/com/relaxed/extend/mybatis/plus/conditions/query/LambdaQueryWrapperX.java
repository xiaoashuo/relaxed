package com.relaxed.extend.mybatis.plus.conditions.query;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.AbstractLambdaWrapper;
import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

/**
 * 增强的 Lambda 查询构造器 继承自 MyBatis-Plus 的 LambdaQueryWrapper，提供了更多便捷的查询条件构造方法。主要增强功能包括：
 * <ul>
 * <li>支持字段值非空判断的条件构造方法</li>
 * <li>支持字段间比较的条件构造方法</li>
 * <li>支持链式调用的条件构造方法</li>
 * </ul>
 */
public class LambdaQueryWrapperX<T> extends AbstractLambdaWrapper<T, LambdaQueryWrapperX<T>>
		implements Query<LambdaQueryWrapperX<T>, T, SFunction<T, ?>> {

	/**
	 * 查询字段
	 */
	private SharedString sqlSelect = new SharedString();

	/**
	 * 创建空的查询构造器
	 * <p>
	 * 不建议直接使用此构造器，推荐使用 WrappersX.lambdaQueryX() 方法创建实例。
	 * </p>
	 */
	public LambdaQueryWrapperX() {
		this((T) null);
	}

	/**
	 * 根据实体对象创建查询构造器
	 * <p>
	 * 不建议直接使用此构造器，推荐使用 WrappersX.lambdaQueryX(entity) 方法创建实例。实体对象的非空属性会作为等值查询条件。
	 * </p>
	 * @param entity 实体对象
	 */
	public LambdaQueryWrapperX(T entity) {
		super.setEntity(entity);
		super.initNeed();
	}

	/**
	 * 根据实体类创建查询构造器
	 * <p>
	 * 不建议直接使用此构造器，推荐使用 WrappersX.lambdaQueryX(entityClass) 方法创建实例。
	 * </p>
	 * @param entityClass 实体类
	 */
	public LambdaQueryWrapperX(Class<T> entityClass) {
		super.setEntityClass(entityClass);
		super.initNeed();
	}

	/**
	 * 创建查询构造器实例
	 * <p>
	 * 用于生成嵌套 SQL 查询条件。
	 * </p>
	 * @param entity 实体对象
	 * @param entityClass 实体类
	 * @param sqlSelect 查询字段
	 * @param paramNameSeq 参数名序列
	 * @param paramNameValuePairs 参数名值对
	 * @param mergeSegments 合并的SQL片段
	 * @param lastSql 最后执行的SQL
	 * @param sqlComment SQL注释
	 * @param sqlFirst SQL前置语句
	 */
	LambdaQueryWrapperX(T entity, Class<T> entityClass, SharedString sqlSelect, AtomicInteger paramNameSeq,
			Map<String, Object> paramNameValuePairs, MergeSegments mergeSegments, SharedString lastSql,
			SharedString sqlComment, SharedString sqlFirst) {
		super.setEntity(entity);
		super.setEntityClass(entityClass);
		this.paramNameSeq = paramNameSeq;
		this.paramNameValuePairs = paramNameValuePairs;
		this.expression = mergeSegments;
		this.sqlSelect = sqlSelect;
		this.lastSql = lastSql;
		this.sqlComment = sqlComment;
		this.sqlFirst = sqlFirst;
	}

	/**
	 * 创建新的查询构造器实例
	 * <p>
	 * 用于生成嵌套 SQL 查询条件，不传递 sqlSelect。
	 * </p>
	 * @return 新的查询构造器实例
	 */
	@Override
	protected LambdaQueryWrapperX<T> instance() {
		return new LambdaQueryWrapperX<>(getEntity(), getEntityClass(), null, paramNameSeq, paramNameValuePairs,
				new MergeSegments(), SharedString.emptyString(), SharedString.emptyString(),
				SharedString.emptyString());
	}

	/**
	 * 设置查询字段
	 * <p>
	 * 根据条件设置查询字段列表。
	 * </p>
	 * @param condition 是否设置查询字段
	 * @param columns 查询字段列表
	 * @return 当前查询构造器
	 */
	protected LambdaQueryWrapperX<T> doSelect(boolean condition, List<SFunction<T, ?>> columns) {
		if (condition && CollectionUtils.isNotEmpty(columns)) {
			this.sqlSelect.setStringValue(columnsToString(false, columns));
		}
		return typedThis;
	}

	/**
	 * 设置查询字段
	 * <p>
	 * 根据条件设置查询字段列表。
	 * </p>
	 * @param condition 是否设置查询字段
	 * @param columns 查询字段列表
	 * @return 当前查询构造器
	 */
	@Override
	public LambdaQueryWrapperX<T> select(boolean condition, List<SFunction<T, ?>> columns) {
		return doSelect(condition, columns);
	}

	/**
	 * 过滤查询的字段信息(主键除外) 支持多种过滤方式：
	 * <ul>
	 * <li>根据字段名过滤：select(i -&gt; i.getProperty().startsWith("test"))</li>
	 * <li>根据字段类型过滤：select(TableFieldInfo::isCharSequence)</li>
	 * <li>根据填充策略过滤：select(i -&gt; i.getFieldFill() == FieldFill.DEFAULT)</li>
	 * <li>选择所有字段：select(i -&gt; true)</li>
	 * <li>只选择主键字段：select(i -&gt; false)</li>
	 * </ul>
	 * @param entityClass 实体类
	 * @param predicate 字段过滤条件
	 * @return 当前查询构造器
	 */
	@Override
	public LambdaQueryWrapperX<T> select(Class<T> entityClass, Predicate<TableFieldInfo> predicate) {
		if (entityClass == null) {
			entityClass = getEntityClass();
		}
		else {
			setEntityClass(entityClass);
		}
		Assert.notNull(entityClass, "entityClass can not be null");
		this.sqlSelect.setStringValue(TableInfoHelper.getTableInfo(entityClass).chooseSelect(predicate));
		return typedThis;
	}

	/**
	 * 设置查询字段
	 * <p>
	 * 设置要查询的字段列表。
	 * </p>
	 * @param columns 查询字段
	 * @return 当前查询构造器
	 */
	@SafeVarargs
	@Override
	public final LambdaQueryWrapperX<T> select(SFunction<T, ?>... columns) {
		return doSelect(true, CollectionUtils.toList(columns));
	}

	/**
	 * 根据条件设置查询字段
	 * <p>
	 * 根据条件设置要查询的字段列表。
	 * </p>
	 * @param condition 是否设置查询字段
	 * @param columns 查询字段
	 * @return 当前查询构造器
	 */
	@Override
	public LambdaQueryWrapperX<T> select(boolean condition, SFunction<T, ?>... columns) {
		return doSelect(condition, CollectionUtils.toList(columns));
	}

	/**
	 * 获取查询字段
	 * <p>
	 * 返回当前设置的查询字段。
	 * </p>
	 * @return 查询字段
	 */
	@Override
	public String getSqlSelect() {
		return sqlSelect.getStringValue();
	}

	/**
	 * 清除所有设置
	 * <p>
	 * 清除所有已设置的查询条件、查询字段等。
	 * </p>
	 */
	@Override
	public void clear() {
		super.clear();
		sqlSelect.toNull();
	}

	/**
	 * 判断对象是否非空
	 * <p>
	 * 支持多种类型的非空判断：
	 * <ul>
	 * <li>字符串：非空且非空白</li>
	 * <li>集合：非空且包含元素</li>
	 * <li>数组：非空且长度大于0</li>
	 * <li>其他对象：非null</li>
	 * </ul>
	 * </p>
	 * @param obj 要判断的对象
	 * @return 如果对象非空返回true，否则返回false
	 */
	@SuppressWarnings("rawtypes")
	private boolean isPresent(Object obj) {
		if (null == obj) {
			return false;
		}
		else if (obj instanceof CharSequence) {
			return StrUtil.isNotBlank((CharSequence) obj);
		}
		else if (obj instanceof Collection) {
			return CollectionUtil.isNotEmpty((Collection) obj);
		}
		if (obj.getClass().isArray()) {
			return ArrayUtil.isNotEmpty(obj);
		}
		return true;
	}

	/**
	 * 等于条件（值非空时生效）
	 * <p>
	 * 当值非空时，添加等于条件。
	 * </p>
	 * @param column 字段
	 * @param val 值
	 * @return 当前查询构造器
	 */
	public LambdaQueryWrapperX<T> eqIfPresent(SFunction<T, ?> column, Object val) {
		return super.eq(isPresent(val), column, val);
	}

	/**
	 * 等于条件（字段间比较）
	 * <p>
	 * 根据条件添加字段间的等于比较。
	 * </p>
	 * @param condition 是否添加条件
	 * @param column1 第一个字段
	 * @param column2 第二个字段
	 * @return 当前查询构造器
	 */
	public LambdaQueryWrapperX<T> eqIfCondition(boolean condition, SFunction<T, ?> column1, SFunction<T, ?> column2) {
		String col1 = this.columnToString(column1);
		String col2 = this.columnToString(column2);
		return super.apply(condition, col1 + SqlKeyword.EQ.getSqlSegment() + col2);
	}

	/**
	 * 等于条件（字段间比较）
	 * <p>
	 * 添加字段间的等于比较。
	 * </p>
	 * @param column1 第一个字段
	 * @param column2 第二个字段
	 * @return 当前查询构造器
	 */
	public LambdaQueryWrapperX<T> eq(SFunction<T, ?> column1, SFunction<T, ?> column2) {
		return eqIfCondition(true, column1, column2);
	}

	/**
	 * 不等于条件（值非空时生效）
	 * <p>
	 * 当值非空时，添加不等于条件。
	 * </p>
	 * @param column 字段
	 * @param val 值
	 * @return 当前查询构造器
	 */
	public LambdaQueryWrapperX<T> neIfPresent(SFunction<T, ?> column, Object val) {
		return super.ne(isPresent(val), column, val);
	}

	/**
	 * 不等于条件（字段间比较）
	 * <p>
	 * 根据条件添加字段间的不等于比较。
	 * </p>
	 * @param condition 是否添加条件
	 * @param column1 第一个字段
	 * @param column2 第二个字段
	 * @return 当前查询构造器
	 */
	public LambdaQueryWrapperX<T> neIfCondition(boolean condition, SFunction<T, ?> column1, SFunction<T, ?> column2) {
		String col1 = this.columnToString(column1);
		String col2 = this.columnToString(column2);
		return super.apply(condition, col1 + SqlKeyword.NE.getSqlSegment() + col2);
	}

	/**
	 * 不等于条件（字段间比较）
	 * <p>
	 * 添加字段间的不等于比较。
	 * </p>
	 * @param column1 第一个字段
	 * @param column2 第二个字段
	 * @return 当前查询构造器
	 */
	public LambdaQueryWrapperX<T> ne(SFunction<T, ?> column1, SFunction<T, ?> column2) {
		return neIfCondition(true, column1, column2);
	}

	/**
	 * 大于条件（值非空时生效）
	 * <p>
	 * 当值非空时，添加大于条件。
	 * </p>
	 * @param column 字段
	 * @param val 值
	 * @return 当前查询构造器
	 */
	public LambdaQueryWrapperX<T> gtIfPresent(SFunction<T, ?> column, Object val) {
		return super.gt(isPresent(val), column, val);
	}

	/**
	 * 大于条件（字段间比较）
	 * <p>
	 * 根据条件添加字段间的大于比较。
	 * </p>
	 * @param condition 是否添加条件
	 * @param column1 第一个字段
	 * @param column2 第二个字段
	 * @return 当前查询构造器
	 */
	public LambdaQueryWrapperX<T> gtIfCondition(boolean condition, SFunction<T, ?> column1, SFunction<T, ?> column2) {
		String col1 = this.columnToString(column1);
		String col2 = this.columnToString(column2);
		return super.apply(condition, col1 + SqlKeyword.GT.getSqlSegment() + col2);
	}

	/**
	 * 大于条件（字段间比较）
	 * <p>
	 * 添加字段间的大于比较。
	 * </p>
	 * @param column1 第一个字段
	 * @param column2 第二个字段
	 * @return 当前查询构造器
	 */
	public LambdaQueryWrapperX<T> gt(SFunction<T, ?> column1, SFunction<T, ?> column2) {
		return gtIfCondition(true, column1, column2);
	}

	/**
	 * 大于等于条件（值非空时生效）
	 * <p>
	 * 当值非空时，添加大于等于条件。
	 * </p>
	 * @param column 字段
	 * @param val 值
	 * @return 当前查询构造器
	 */
	public LambdaQueryWrapperX<T> geIfPresent(SFunction<T, ?> column, Object val) {
		return super.ge(isPresent(val), column, val);
	}

	/**
	 * 大于等于条件（字段间比较）
	 * <p>
	 * 根据条件添加字段间的大于等于比较。
	 * </p>
	 * @param condition 是否添加条件
	 * @param column1 第一个字段
	 * @param column2 第二个字段
	 * @return 当前查询构造器
	 */
	public LambdaQueryWrapperX<T> geIfCondition(boolean condition, SFunction<T, ?> column1, SFunction<T, ?> column2) {
		String col1 = this.columnToString(column1);
		String col2 = this.columnToString(column2);
		return super.apply(condition, col1 + SqlKeyword.GE.getSqlSegment() + col2);
	}

	/**
	 * 大于等于条件（字段间比较）
	 * <p>
	 * 添加字段间的大于等于比较。
	 * </p>
	 * @param column1 第一个字段
	 * @param column2 第二个字段
	 * @return 当前查询构造器
	 */
	public LambdaQueryWrapperX<T> ge(SFunction<T, ?> column1, SFunction<T, ?> column2) {
		return geIfCondition(true, column1, column2);
	}

	/**
	 * 小于条件（值非空时生效）
	 * <p>
	 * 当值非空时，添加小于条件。
	 * </p>
	 * @param column 字段
	 * @param val 值
	 * @return 当前查询构造器
	 */
	public LambdaQueryWrapperX<T> ltIfPresent(SFunction<T, ?> column, Object val) {
		return super.lt(isPresent(val), column, val);
	}

	/**
	 * 小于条件（字段间比较）
	 * <p>
	 * 根据条件添加字段间的小于比较。
	 * </p>
	 * @param condition 是否添加条件
	 * @param column1 第一个字段
	 * @param column2 第二个字段
	 * @return 当前查询构造器
	 */
	public LambdaQueryWrapperX<T> ltIfCondition(boolean condition, SFunction<T, ?> column1, SFunction<T, ?> column2) {
		String col1 = this.columnToString(column1);
		String col2 = this.columnToString(column2);
		return super.apply(condition, col1 + SqlKeyword.LT.getSqlSegment() + col2);
	}

	/**
	 * 小于条件（字段间比较）
	 * <p>
	 * 添加字段间的小于比较。
	 * </p>
	 * @param column1 第一个字段
	 * @param column2 第二个字段
	 * @return 当前查询构造器
	 */
	public LambdaQueryWrapperX<T> lt(SFunction<T, ?> column1, SFunction<T, ?> column2) {
		return ltIfCondition(true, column1, column2);
	}

	/**
	 * 小于等于条件（值非空时生效）
	 * <p>
	 * 当值非空时，添加小于等于条件。
	 * </p>
	 * @param column 字段
	 * @param val 值
	 * @return 当前查询构造器
	 */
	public LambdaQueryWrapperX<T> leIfPresent(SFunction<T, ?> column, Object val) {
		return super.le(isPresent(val), column, val);
	}

	/**
	 * 小于等于条件（字段间比较）
	 * <p>
	 * 根据条件添加字段间的小于等于比较。
	 * </p>
	 * @param condition 是否添加条件
	 * @param column1 第一个字段
	 * @param column2 第二个字段
	 * @return 当前查询构造器
	 */
	public LambdaQueryWrapperX<T> leIfCondition(boolean condition, SFunction<T, ?> column1, SFunction<T, ?> column2) {
		String col1 = this.columnToString(column1);
		String col2 = this.columnToString(column2);
		return super.apply(condition, col1 + SqlKeyword.LE.getSqlSegment() + col2);
	}

	/**
	 * 小于等于条件（字段间比较）
	 * <p>
	 * 添加字段间的小于等于比较。
	 * </p>
	 * @param column1 第一个字段
	 * @param column2 第二个字段
	 * @return 当前查询构造器
	 */
	public LambdaQueryWrapperX<T> le(SFunction<T, ?> column1, SFunction<T, ?> column2) {
		return leIfCondition(true, column1, column2);
	}

	/**
	 * 模糊匹配条件（值非空时生效）
	 * <p>
	 * 当值非空时，添加模糊匹配条件。
	 * </p>
	 * @param column 字段
	 * @param val 值
	 * @return 当前查询构造器
	 */
	public LambdaQueryWrapperX<T> likeIfPresent(SFunction<T, ?> column, Object val) {
		return super.like(isPresent(val), column, val);
	}

	/**
	 * 不匹配条件（值非空时生效）
	 * <p>
	 * 当值非空时，添加不匹配条件。
	 * </p>
	 * @param column 字段
	 * @param val 值
	 * @return 当前查询构造器
	 */
	public LambdaQueryWrapperX<T> notLikeIfPresent(SFunction<T, ?> column, Object val) {
		return super.notLike(isPresent(val), column, val);
	}

	/**
	 * 左模糊匹配条件（值非空时生效）
	 * <p>
	 * 当值非空时，添加左模糊匹配条件。
	 * </p>
	 * @param column 字段
	 * @param val 值
	 * @return 当前查询构造器
	 */
	public LambdaQueryWrapperX<T> likeLeftIfPresent(SFunction<T, ?> column, Object val) {
		return super.likeLeft(isPresent(val), column, val);
	}

	/**
	 * 右模糊匹配条件（值非空时生效）
	 * <p>
	 * 当值非空时，添加右模糊匹配条件。
	 * </p>
	 * @param column 字段
	 * @param val 值
	 * @return 当前查询构造器
	 */
	public LambdaQueryWrapperX<T> likeRightIfPresent(SFunction<T, ?> column, Object val) {
		return super.likeRight(isPresent(val), column, val);
	}

	/**
	 * IN条件（值非空时生效）
	 * <p>
	 * 当值非空时，添加IN条件。
	 * </p>
	 * @param column 字段
	 * @param values 值数组
	 * @return 当前查询构造器
	 */
	public LambdaQueryWrapperX<T> inIfPresent(SFunction<T, ?> column, Object... values) {
		return super.in(isPresent(values), column, values);
	}

	/**
	 * IN条件（值非空时生效）
	 * <p>
	 * 当值非空时，添加IN条件。
	 * </p>
	 * @param column 字段
	 * @param values 值集合
	 * @return 当前查询构造器
	 */
	public LambdaQueryWrapperX<T> inIfPresent(SFunction<T, ?> column, Collection<?> values) {
		return super.in(isPresent(values), column, values);
	}

	/**
	 * NOT IN条件（值非空时生效）
	 * <p>
	 * 当值非空时，添加NOT IN条件。
	 * </p>
	 * @param column 字段
	 * @param values 值数组
	 * @return 当前查询构造器
	 */
	public LambdaQueryWrapperX<T> notInIfPresent(SFunction<T, ?> column, Object... values) {
		return super.notIn(isPresent(values), column, values);
	}

	/**
	 * NOT IN条件（值非空时生效）
	 * <p>
	 * 当值非空时，添加NOT IN条件。
	 * </p>
	 * @param column 字段
	 * @param values 值集合
	 * @return 当前查询构造器
	 */
	public LambdaQueryWrapperX<T> notInIfPresent(SFunction<T, ?> column, Collection<?> values) {
		return super.notIn(isPresent(values), column, values);
	}

}
