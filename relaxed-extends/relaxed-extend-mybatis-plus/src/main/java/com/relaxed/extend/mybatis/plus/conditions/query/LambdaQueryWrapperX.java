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
 * @author Yakir
 * @Topic LambdaQueryWrapperX
 * @Description
 * @date 2021/12/29 16:47
 * @Version 1.0
 */
public class LambdaQueryWrapperX<T> extends AbstractLambdaWrapper<T, LambdaQueryWrapperX<T>>
		implements Query<LambdaQueryWrapperX<T>, T, SFunction<T, ?>> {

	/**
	 * 查询字段
	 */
	private SharedString sqlSelect = new SharedString();

	/**
	 * 不建议直接 new 该实例，使用 Wrappers.lambdaQuery(entity)
	 */
	public LambdaQueryWrapperX() {
		this((T) null);
	}

	/**
	 * 不建议直接 new 该实例，使用 Wrappers.lambdaQuery(entity)
	 */
	public LambdaQueryWrapperX(T entity) {
		super.setEntity(entity);
		super.initNeed();
	}

	/**
	 * 不建议直接 new 该实例，使用 Wrappers.lambdaQuery(entity)
	 */
	public LambdaQueryWrapperX(Class<T> entityClass) {
		super.setEntityClass(entityClass);
		super.initNeed();
	}

	/**
	 * 不建议直接 new 该实例，使用 Wrappers.lambdaQuery(...)
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

	@Override
	protected LambdaQueryWrapperX<T> instance() {
		return new LambdaQueryWrapperX<>(getEntity(), getEntityClass(), null, paramNameSeq, paramNameValuePairs,
				new MergeSegments(), SharedString.emptyString(), SharedString.emptyString(),
				SharedString.emptyString());
	}

	/**
	 * @since 3.5.4
	 */
	protected LambdaQueryWrapperX<T> doSelect(boolean condition, List<SFunction<T, ?>> columns) {
		if (condition && CollectionUtils.isNotEmpty(columns)) {
			this.sqlSelect.setStringValue(columnsToString(false, columns));
		}
		return typedThis;
	}

	@Override
	public LambdaQueryWrapperX<T> select(boolean condition, List<SFunction<T, ?>> columns) {
		return doSelect(condition, columns);
	}

	/**
	 * 过滤查询的字段信息(主键除外!)
	 * <p>
	 * 例1: 只要 java 字段名以 "test" 开头的 -> select(i -&gt; i.getProperty().startsWith("test"))
	 * </p>
	 * <p>
	 * 例2: 只要 java 字段属性是 CharSequence 类型的 -> select(TableFieldInfo::isCharSequence)
	 * </p>
	 * <p>
	 * 例3: 只要 java 字段没有填充策略的 -> select(i -&gt; i.getFieldFill() == FieldFill.DEFAULT)
	 * </p>
	 * <p>
	 * 例4: 要全部字段 -> select(i -&gt; true)
	 * </p>
	 * <p>
	 * 例5: 只要主键字段 -> select(i -&gt; false)
	 * </p>
	 * @param predicate 过滤方式
	 * @return this
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
	 * SELECT 部分 SQL 设置
	 * @param columns 查询字段
	 */
	@SafeVarargs
	@Override
	public final LambdaQueryWrapperX<T> select(SFunction<T, ?>... columns) {
		return doSelect(true, CollectionUtils.toList(columns));
	}

	@Override
	public LambdaQueryWrapperX<T> select(boolean condition, SFunction<T, ?>... columns) {
		return doSelect(condition, CollectionUtils.toList(columns));
	}

	@Override
	public String getSqlSelect() {
		return sqlSelect.getStringValue();
	}

	@Override
	public void clear() {
		super.clear();
		sqlSelect.toNull();
	}
	// ======= 分界线，以上 copy 自 mybatis-plus 源码 =====

	/**
	 * 当前条件只是否非null，且不为空
	 * @param obj 值
	 * @return boolean 不为空返回true
	 */
	@SuppressWarnings("rawtypes")
	private boolean isPresent(Object obj) {
		if (null == obj) {
			return false;
		}
		else if (obj instanceof CharSequence) {
			// 字符串比较特殊，如果是空字符串也不行
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

	public LambdaQueryWrapperX<T> eqIfPresent(SFunction<T, ?> column, Object val) {
		return super.eq(isPresent(val), column, val);
	}

	public LambdaQueryWrapperX<T> eqIfCondition(boolean condition, SFunction<T, ?> column1, SFunction<T, ?> column2) {
		String col1 = this.columnToString(column1);
		String col2 = this.columnToString(column2);
		return super.apply(condition, col1 + SqlKeyword.EQ.getSqlSegment() + col2);

	}

	public LambdaQueryWrapperX<T> eq(SFunction<T, ?> column1, SFunction<T, ?> column2) {
		return eqIfCondition(true, column1, column2);
	}

	public LambdaQueryWrapperX<T> neIfPresent(SFunction<T, ?> column, Object val) {
		return super.ne(isPresent(val), column, val);
	}

	public LambdaQueryWrapperX<T> neIfCondition(boolean condition, SFunction<T, ?> column1, SFunction<T, ?> column2) {
		String col1 = this.columnToString(column1);
		String col2 = this.columnToString(column2);
		return super.apply(condition, col1 + SqlKeyword.NE.getSqlSegment() + col2);

	}

	public LambdaQueryWrapperX<T> ne(SFunction<T, ?> column1, SFunction<T, ?> column2) {
		return neIfCondition(true, column1, column2);
	}

	public LambdaQueryWrapperX<T> gtIfPresent(SFunction<T, ?> column, Object val) {
		return super.gt(isPresent(val), column, val);
	}

	public LambdaQueryWrapperX<T> gtIfCondition(boolean condition, SFunction<T, ?> column1, SFunction<T, ?> column2) {
		String col1 = this.columnToString(column1);
		String col2 = this.columnToString(column2);
		return super.apply(condition, col1 + SqlKeyword.GT.getSqlSegment() + col2);

	}

	public LambdaQueryWrapperX<T> gt(SFunction<T, ?> column1, SFunction<T, ?> column2) {
		return gtIfCondition(true, column1, column2);
	}

	public LambdaQueryWrapperX<T> geIfPresent(SFunction<T, ?> column, Object val) {
		return super.ge(isPresent(val), column, val);
	}

	public LambdaQueryWrapperX<T> geIfCondition(boolean condition, SFunction<T, ?> column1, SFunction<T, ?> column2) {
		String col1 = this.columnToString(column1);
		String col2 = this.columnToString(column2);
		return super.apply(condition, col1 + SqlKeyword.GE.getSqlSegment() + col2);

	}

	public LambdaQueryWrapperX<T> ge(SFunction<T, ?> column1, SFunction<T, ?> column2) {
		return geIfCondition(true, column1, column2);
	}

	public LambdaQueryWrapperX<T> ltIfPresent(SFunction<T, ?> column, Object val) {
		return super.lt(isPresent(val), column, val);
	}

	public LambdaQueryWrapperX<T> ltIfCondition(boolean condition, SFunction<T, ?> column1, SFunction<T, ?> column2) {
		String col1 = this.columnToString(column1);
		String col2 = this.columnToString(column2);
		return super.apply(condition, col1 + SqlKeyword.LT.getSqlSegment() + col2);

	}

	public LambdaQueryWrapperX<T> lt(SFunction<T, ?> column1, SFunction<T, ?> column2) {
		return ltIfCondition(true, column1, column2);
	}

	public LambdaQueryWrapperX<T> leIfPresent(SFunction<T, ?> column, Object val) {
		return super.le(isPresent(val), column, val);
	}

	public LambdaQueryWrapperX<T> leIfCondition(boolean condition, SFunction<T, ?> column1, SFunction<T, ?> column2) {
		String col1 = this.columnToString(column1);
		String col2 = this.columnToString(column2);
		return super.apply(condition, col1 + SqlKeyword.LE.getSqlSegment() + col2);

	}

	public LambdaQueryWrapperX<T> le(SFunction<T, ?> column1, SFunction<T, ?> column2) {
		return leIfCondition(true, column1, column2);
	}

	public LambdaQueryWrapperX<T> likeIfPresent(SFunction<T, ?> column, Object val) {
		return super.like(isPresent(val), column, val);
	}

	public LambdaQueryWrapperX<T> notLikeIfPresent(SFunction<T, ?> column, Object val) {
		return super.notLike(isPresent(val), column, val);
	}

	public LambdaQueryWrapperX<T> likeLeftIfPresent(SFunction<T, ?> column, Object val) {
		return super.likeLeft(isPresent(val), column, val);
	}

	public LambdaQueryWrapperX<T> likeRightIfPresent(SFunction<T, ?> column, Object val) {
		return super.likeRight(isPresent(val), column, val);
	}

	public LambdaQueryWrapperX<T> inIfPresent(SFunction<T, ?> column, Object... values) {
		return super.in(isPresent(values), column, values);
	}

	public LambdaQueryWrapperX<T> inIfPresent(SFunction<T, ?> column, Collection<?> values) {
		return super.in(isPresent(values), column, values);
	}

	public LambdaQueryWrapperX<T> notInIfPresent(SFunction<T, ?> column, Object... values) {
		return super.notIn(isPresent(values), column, values);
	}

	public LambdaQueryWrapperX<T> notInIfPresent(SFunction<T, ?> column, Collection<?> values) {
		return super.notIn(isPresent(values), column, values);
	}

}
