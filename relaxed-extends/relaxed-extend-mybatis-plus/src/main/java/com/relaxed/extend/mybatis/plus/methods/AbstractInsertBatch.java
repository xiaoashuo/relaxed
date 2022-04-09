package com.relaxed.extend.mybatis.plus.methods;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import com.relaxed.extend.mybatis.plus.toolkit.ExtendConstants;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Yakir
 * @Topic AbstractInsertBatch
 * @Description
 * @date 2021/7/11 10:25
 * @Version 1.0
 */
public abstract class AbstractInsertBatch extends AbstractMethod {

	protected AbstractInsertBatch(String methodName) {
		super(methodName);
	}

	@Override
	public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
		// === mybatis 主键逻辑处理：主键生成策略，以及主键回填=======
		KeyGenerator keyGenerator = NoKeyGenerator.INSTANCE;
		String keyColumn = null;
		String keyProperty = null;
		// 如果需要回填主键
		if (tableInfo.getKeyProperty() != null && !"".equals(tableInfo.getKeyProperty())) {
			// 表包含主键处理逻辑,如果不包含主键当普通字段处理
			if (tableInfo.getIdType() == IdType.AUTO) {
				/* 自增主键 */
				keyGenerator = Jdbc3KeyGenerator.INSTANCE;
				keyProperty = getKeyProperty(tableInfo);
				keyColumn = tableInfo.getKeyColumn();
			}
			else {
				if (null != tableInfo.getKeySequence()) {
					keyGenerator = TableInfoHelper.genKeyGenerator(this.methodName, tableInfo, builderAssistant);
					keyProperty = getKeyProperty(tableInfo);
					keyColumn = tableInfo.getKeyColumn();
				}
			}
		}
		String sql = String.format(getSql(), tableInfo.getTableName(), prepareFieldSql(tableInfo),
				prepareValuesSqlForMysqlBatch(tableInfo));
		SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
		return this.addInsertMappedStatement(mapperClass, modelClass, this.methodName, sqlSource, keyGenerator,
				keyProperty, keyColumn);
	}

	private String getKeyProperty(TableInfo tableInfo) {
		return tableInfo.getKeyProperty();
	}

	protected String prepareFieldSql(TableInfo tableInfo) {
		List<TableFieldInfo> fieldList = tableInfo.getFieldList();
		String columnFields = tableInfo.getKeyInsertSqlColumn(true, false)
				+ fieldList.stream().map(AbstractInsertBatch::getInsertSqlColumn).collect(Collectors.joining(NEWLINE));
		return SqlScriptUtils.convertTrim(columnFields, LEFT_BRACKET, RIGHT_BRACKET, null, COMMA);
	}

	protected String prepareValuesSqlForMysqlBatch(TableInfo tableInfo) {
		List<TableFieldInfo> fieldList = tableInfo.getFieldList();
		StringBuilder valueSql = new StringBuilder();
		valueSql.append("<foreach collection=\"" + ExtendConstants.COLLECTION + "\" item=\"" + ENTITY
				+ "\" index=\"index\"  separator=\",\" >");
		String valueList = tableInfo.getKeyInsertSqlProperty(true, ENTITY_DOT, true) + fieldList.stream()
				.map(AbstractInsertBatch::getInsertSqlPropertyMaybeIf).collect(Collectors.joining(NEWLINE));
		valueSql.append(SqlScriptUtils.convertTrim(valueList, LEFT_BRACKET, RIGHT_BRACKET, null, COMMA));
		valueSql.append("</foreach>");

		return valueSql.toString();
	}

	/**
	 * 获取 insert 时候插入值 sql 脚本片段
	 * <p>
	 * insert into table (字段) values (值)
	 * </p>
	 * <p>
	 * 位于 "值" 部位
	 * </p>
	 *
	 * <li>不生成 if 标签</li>
	 * @return sql 脚本片段
	 */
	private static String getInsertSqlPropertyMaybeIf(TableFieldInfo tableFieldInfo) {
		return tableFieldInfo.getInsertSqlProperty(ENTITY_DOT);
	}

	/**
	 * 获取 insert 时候字段 sql 脚本片段
	 * <p>
	 * insert into table (字段) values (值)
	 * </p>
	 * <p>
	 * 位于 "字段" 部位
	 * </p>
	 *
	 * <li>不生成 if 标签</li>
	 * @return sql 脚本片段
	 */
	private static String getInsertSqlColumn(TableFieldInfo tableFieldInfo) {
		return tableFieldInfo.getInsertSqlColumn();
	}

	/**
	 * 获取注册的脚本
	 * @return java.lang.String
	 */
	protected abstract String getSql();

}
