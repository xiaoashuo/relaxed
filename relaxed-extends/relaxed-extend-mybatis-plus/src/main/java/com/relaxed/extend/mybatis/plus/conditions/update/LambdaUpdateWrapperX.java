package com.relaxed.extend.mybatis.plus.conditions.update;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.AbstractLambdaWrapper;
import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.Update;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.relaxed.extend.mybatis.plus.conditions.query.LambdaQueryWrapperX;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Yakir
 * @Topic LambdaUpdateWrapperX
 * @Description
 * @date 2021/12/29 17:07
 * @Version 1.0
 */
public class LambdaUpdateWrapperX<T> extends AbstractLambdaWrapper<T, LambdaUpdateWrapperX<T>>
		implements Update<LambdaUpdateWrapperX<T>, SFunction<T, ?>> {

	/**
	 * SQL 更新字段内容，例如：name='1', age=2
	 */
	private final List<String> sqlSet;

	/**
	 * 不建议直接 new 该实例，使用 Wrappers.lambdaUpdate()
	 */
	public LambdaUpdateWrapperX() {
		// 如果无参构造函数，请注意实体 NULL 情况 SET 必须有否则 SQL 异常
		this((T) null);
	}

	/**
	 * 不建议直接 new 该实例，使用 Wrappers.lambdaUpdate(entity)
	 */
	public LambdaUpdateWrapperX(T entity) {
		super.setEntity(entity);
		super.initNeed();
		this.sqlSet = new ArrayList<>();
	}

	/**
	 * 不建议直接 new 该实例，使用 Wrappers.lambdaUpdate(entity)
	 */
	public LambdaUpdateWrapperX(Class<T> entityClass) {
		super.setEntityClass(entityClass);
		super.initNeed();
		this.sqlSet = new ArrayList<>();
	}

	/**
	 * 不建议直接 new 该实例，使用 Wrappers.lambdaUpdate(...)
	 */
	LambdaUpdateWrapperX(T entity, Class<T> entityClass, List<String> sqlSet, AtomicInteger paramNameSeq,
			Map<String, Object> paramNameValuePairs, MergeSegments mergeSegments, SharedString lastSql,
			SharedString sqlComment, SharedString sqlFirst) {
		super.setEntity(entity);
		super.setEntityClass(entityClass);
		this.sqlSet = sqlSet;
		this.paramNameSeq = paramNameSeq;
		this.paramNameValuePairs = paramNameValuePairs;
		this.expression = mergeSegments;
		this.lastSql = lastSql;
		this.sqlComment = sqlComment;
		this.sqlFirst = sqlFirst;
	}

	@Override
	public LambdaUpdateWrapperX<T> set(boolean condition, SFunction<T, ?> column, Object val) {
		if (condition) {
			sqlSet.add(String.format("%s=%s", columnToString(column), formatSql("{0}", val)));
		}
		return typedThis;
	}

	@Override
	public LambdaUpdateWrapperX<T> setSql(boolean condition, String sql) {
		if (condition && StringUtils.isNotBlank(sql)) {
			sqlSet.add(sql);
		}
		return typedThis;
	}

	@Override
	public String getSqlSet() {
		if (CollectionUtils.isEmpty(sqlSet)) {
			return null;
		}
		return String.join(StringPool.COMMA, sqlSet);
	}

	@Override
	protected LambdaUpdateWrapperX<T> instance() {
		return new LambdaUpdateWrapperX<>(getEntity(), getEntityClass(), null, paramNameSeq, paramNameValuePairs,
				new MergeSegments(), SharedString.emptyString(), SharedString.emptyString(),
				SharedString.emptyString());
	}

	@Override
	public void clear() {
		super.clear();
		sqlSet.clear();
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

	public LambdaUpdateWrapperX<T> setIfPresent(SFunction<T, ?> column, Object val) {
		return set(isPresent(val), column, val);
	}

	public LambdaUpdateWrapperX<T> eqIfPresent(SFunction<T, ?> column, Object val) {
		return super.eq(isPresent(val), column, val);
	}

	public LambdaUpdateWrapperX<T> neIfPresent(SFunction<T, ?> column, Object val) {
		return super.ne(isPresent(val), column, val);
	}

	public LambdaUpdateWrapperX<T> gtIfPresent(SFunction<T, ?> column, Object val) {
		return super.gt(isPresent(val), column, val);
	}

	public LambdaUpdateWrapperX<T> geIfPresent(SFunction<T, ?> column, Object val) {
		return super.ge(isPresent(val), column, val);
	}

	public LambdaUpdateWrapperX<T> ltIfPresent(SFunction<T, ?> column, Object val) {
		return super.lt(isPresent(val), column, val);
	}

	public LambdaUpdateWrapperX<T> leIfPresent(SFunction<T, ?> column, Object val) {
		return super.le(isPresent(val), column, val);
	}

	public LambdaUpdateWrapperX<T> likeIfPresent(SFunction<T, ?> column, Object val) {
		return super.like(isPresent(val), column, val);
	}

	public LambdaUpdateWrapperX<T> notLikeIfPresent(SFunction<T, ?> column, Object val) {
		return super.notLike(isPresent(val), column, val);
	}

	public LambdaUpdateWrapperX<T> likeLeftIfPresent(SFunction<T, ?> column, Object val) {
		return super.likeLeft(isPresent(val), column, val);
	}

	public LambdaUpdateWrapperX<T> likeRightIfPresent(SFunction<T, ?> column, Object val) {
		return super.likeRight(isPresent(val), column, val);
	}

	public LambdaUpdateWrapperX<T> inIfPresent(SFunction<T, ?> column, Object... values) {
		return super.in(isPresent(values), column, values);
	}

	public LambdaUpdateWrapperX<T> inIfPresent(SFunction<T, ?> column, Collection<?> values) {
		return super.in(isPresent(values), column, values);
	}

	public LambdaUpdateWrapperX<T> notInIfPresent(SFunction<T, ?> column, Object... values) {
		return super.notIn(isPresent(values), column, values);
	}

	public LambdaUpdateWrapperX<T> notInIfPresent(SFunction<T, ?> column, Collection<?> values) {
		return super.notIn(isPresent(values), column, values);
	}

}
