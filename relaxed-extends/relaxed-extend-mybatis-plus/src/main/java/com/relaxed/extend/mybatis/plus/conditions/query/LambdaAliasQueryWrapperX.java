package com.relaxed.extend.mybatis.plus.conditions.query;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.AbstractLambdaWrapper;
import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.relaxed.extend.mybatis.plus.alias.TableAliasHelper;

import java.util.Collection;
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
public class LambdaAliasQueryWrapperX<T> extends LambdaQueryWrapperX<T> {

	private final String tableAlias;

	private TableAliasFunction tableAliasFunction;

	/**
	 * 带别名的查询列 sql 片段，默认为null，第一次使用时加载<br/>
	 * eg. t.id,t.name
	 */
	private String allAliasSqlSelect = null;

	public LambdaAliasQueryWrapperX(T entity) {
		this(entity, TableAliasHelper::tableAlias);
	}

	public LambdaAliasQueryWrapperX(T entity, TableAliasFunction tableAliasFunction) {
		super(entity);
		this.tableAliasFunction = tableAliasFunction;
		this.tableAlias = this.tableAliasFunction.alias(getEntityClass());
	}

	public LambdaAliasQueryWrapperX(Class<T> entityClass) {
		this(entityClass, TableAliasHelper::tableAlias);
	}

	public LambdaAliasQueryWrapperX(Class<T> entityClass, TableAliasFunction tableAliasFunction) {
		super(entityClass);
		this.tableAliasFunction = tableAliasFunction;
		this.tableAlias = this.tableAliasFunction.alias(getEntityClass());
	}

	/**
	 * 获取查询带别名的查询字段 TODO 暂时没有想到好的方法进行查询字段注入 本来的想法是 自定义注入 SqlFragment 但是目前 mybatis-plus 的
	 * TableInfo 解析在 xml 解析之后进行，导致 include 标签被提前替换， 先在 wrapper 中做简单处理吧
	 * @return String allAliasSqlSelect
	 */
	public String getAllAliasSqlSelect() {
		if (allAliasSqlSelect == null) {
			allAliasSqlSelect = TableAliasHelper.tableAliasSelectSql(getEntityClass(), this.tableAliasFunction);
		}
		return allAliasSqlSelect;
	}
	/**
	 * 用于生成嵌套 sql
	 * <p>
	 * 故 sqlSelect 不向下传递
	 * </p>
	 */
	@Override
	protected LambdaAliasQueryWrapperX<T> instance() {
		return new LambdaAliasQueryWrapperX<>(getEntityClass());
	}
	/**
	 * 查询条件构造时添加上表别名
	 * @param column 字段Lambda
	 * @return 表别名.字段名，如：t.id
	 */
	@Override
	protected String columnToString(SFunction<T, ?> column) {
		if (column instanceof OtherTableColumnAliasFunction) {
			@SuppressWarnings("unchecked")
			OtherTableColumnAliasFunction<T> otherTableColumnAlias = (OtherTableColumnAliasFunction) column;
			return otherTableColumnAlias.apply(null);
		}
		String columnName = super.columnToString(column, true);
		return tableAlias == null ? columnName : tableAlias + "." + columnName;
	}


	
}
