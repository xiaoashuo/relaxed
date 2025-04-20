/*
 * Copyright (c) 2020 pig4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.relaxed.common.datasource.config;

import com.baomidou.dynamic.datasource.provider.AbstractJdbcDataSourceProvider;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.relaxed.common.datasource.provider.PropertyProvider;
import org.jasypt.encryption.StringEncryptor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * JDBC动态数据源提供者 继承自AbstractJdbcDataSourceProvider，实现从数据库查询数据源配置 支持加密密码的解密和数据源属性的自定义处理
 *
 * @author lengleng
 */
public class JdbcDynamicDataSourceProvider extends AbstractJdbcDataSourceProvider {

	/**
	 * 默认主数据源名称 用于标识系统默认的数据源
	 */
	private final static String DS_MASTER = "master";

	/**
	 * 数据源配置属性 包含数据源的基本配置信息
	 */
	private final DataSourceProperties properties;

	/**
	 * 字符串加密器 用于解密数据库中的加密密码
	 */
	private final StringEncryptor stringEncryptor;

	/**
	 * 数据源属性提供者 用于自定义数据源属性的创建逻辑
	 */
	private final PropertyProvider propertyProvider;

	/**
	 * 构造函数
	 * @param stringEncryptor 字符串加密器
	 * @param properties 数据源配置属性
	 * @param propertyProvider 数据源属性提供者
	 */
	public JdbcDynamicDataSourceProvider(StringEncryptor stringEncryptor, DataSourceProperties properties,
			PropertyProvider propertyProvider) {
		super(properties.getDriverClassName(), properties.getUrl(), properties.getUsername(), properties.getPassword());
		this.stringEncryptor = stringEncryptor;
		this.properties = properties;
		this.propertyProvider = propertyProvider;
	}

	/**
	 * 执行SQL语句获取数据源配置 从数据库查询数据源配置信息，并转换为DataSourceProperty对象 同时添加默认的主数据源配置
	 * @param statement SQL语句执行器
	 * @return 数据源配置映射，key为数据源名称，value为数据源属性
	 * @throws SQLException 数据库操作异常
	 */
	@Override
	protected Map<String, DataSourceProperty> executeStmt(Statement statement) throws SQLException {
		ResultSet rs = statement.executeQuery(properties.getQueryDsSql());
		Map<String, DataSourceProperty> map = new HashMap<>(8);
		DataSourceProperties.ResultSetKey rsk = properties.getRsk();

		while (rs.next()) {
			String name = rs.getString(rsk.getName());
			String username = rs.getString(rsk.getUsername());
			String password = rs.getString(rsk.getPassword());
			String url = rs.getString(rsk.getUrl());
			DataSourceProperty property = propertyProvider.prodDataSourceProperty(name, url, username,
					stringEncryptor.decrypt(password));
			map.put(name, property);
		}

		// 添加默认主数据源
		DataSourceProperty property = propertyProvider.prodDataSourceProperty(DS_MASTER, properties.getUrl(),
				properties.getUsername(), properties.getPassword());
		map.put(DS_MASTER, property);
		return map;
	}

}
