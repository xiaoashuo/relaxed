package com.relaxed.extend.mybatis.plus.alias;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.relaxed.extend.mybatis.plus.conditions.query.TableAliasFunction;
import org.springframework.core.annotation.AnnotationUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 表别名助手类
 * <p>
 * 提供表别名的缓存和解析功能。 使用 ConcurrentHashMap 缓存表别名，提高性能。
 */

public class TableAliasHelper {

	private TableAliasHelper() {
	}

	private static final String COMMA = ",";

	private static final String DOT = ".";

	/**
	 * 存储类对应的表别名
	 */
	private static final Map<Class<?>, String> TABLE_ALIAS_CACHE = new ConcurrentHashMap<>();

	/**
	 * 储存类对应的带别名的查询字段
	 */
	private static final Map<Class<?>, String> TABLE_ALIAS_SELECT_COLUMNS_CACHE = new ConcurrentHashMap<>();

	/**
	 * 带别名的查询字段sql
	 * @param clazz 实体类class
	 * @return sql片段
	 */
	public static String tableAliasSelectSql(Class<?> clazz) {
		return tableAliasSelectSql(clazz, TableAliasHelper::tableAlias);
	}

	public static String tableAliasSelectSql(Class<?> clazz, TableAliasFunction tableAliasFunction) {
		String tableAliasSelectSql = TABLE_ALIAS_SELECT_COLUMNS_CACHE.get(clazz);
		if (tableAliasSelectSql == null) {
			String tableAlias = tableAliasFunction.alias(clazz);

			TableInfo tableInfo = TableInfoHelper.getTableInfo(clazz);
			String allSqlSelect = tableInfo.getAllSqlSelect();
			String[] split = allSqlSelect.split(COMMA);
			StringBuilder stringBuilder = new StringBuilder();
			for (String column : split) {
				stringBuilder.append(tableAlias).append(DOT).append(column).append(COMMA);
			}
			stringBuilder.deleteCharAt(stringBuilder.length() - 1);
			tableAliasSelectSql = stringBuilder.toString();

			TABLE_ALIAS_SELECT_COLUMNS_CACHE.put(clazz, tableAliasSelectSql);
		}
		return tableAliasSelectSql;
	}

	/**
	 * 获取实体类对应别名
	 * @param clazz 实体类
	 * @return 表别名
	 */
	public static String tableAlias(Class<?> clazz) {
		String tableAlias = TABLE_ALIAS_CACHE.get(clazz);
		if (tableAlias == null) {
			TableAlias annotation = AnnotationUtils.findAnnotation(clazz, TableAlias.class);
			if (annotation == null) {
				throw new TableAliasNotFoundException(
						"[tableAliasSelectSql] No TableAlias annotations found in class: " + clazz);
			}
			tableAlias = annotation.value();
			TABLE_ALIAS_CACHE.put(clazz, tableAlias);
		}
		return tableAlias;
	}

}
