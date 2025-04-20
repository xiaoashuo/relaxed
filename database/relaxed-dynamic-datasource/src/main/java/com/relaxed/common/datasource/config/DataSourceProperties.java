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

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * 数据源配置属性类 用于配置动态数据源的基本属性和查询配置 支持从数据库查询数据源配置信息
 * 通过@ConfigurationProperties注解绑定spring.datasource前缀的配置
 *
 * @author lengleng
 */
@Data
@ConfigurationProperties("spring.datasource")
public class DataSourceProperties {

	/**
	 * 数据库用户名 用于连接数据库的认证信息 必填项，用于建立数据库连接
	 */
	private String username;

	/**
	 * 数据库密码 用于连接数据库的认证信息 必填项，用于建立数据库连接
	 */
	private String password;

	/**
	 * 数据库连接URL 指定数据库服务器的地址和连接参数 格式示例：jdbc:mysql://localhost:3306/database
	 */
	private String url;

	/**
	 * 数据库驱动类名 默认为MySQL驱动 支持自定义其他数据库驱动
	 */
	private String driverClassName = "com.mysql.cj.jdbc.Driver";

	/**
	 * 查询数据源配置的SQL语句 用于从数据库中获取数据源配置信息 默认查询未删除的数据源配置 可以通过配置文件修改查询语句
	 */
	private String queryDsSql = "select * from gen_datasource_conf where del_flag = 0";

	/**
	 * 结果集属性映射配置 用于映射数据库查询结果到数据源属性 通过@NestedConfigurationProperty注解支持嵌套配置
	 */
	@NestedConfigurationProperty
	private ResultSetKey rsk = new ResultSetKey();

	/**
	 * 结果集属性映射类 定义数据库查询结果列与数据源属性的映射关系 支持自定义列名映射
	 */
	@Data
	public static class ResultSetKey {

		/**
		 * 数据源名称对应的列名 默认为"name" 用于标识不同的数据源
		 */
		private String name = "name";

		/**
		 * 数据库连接URL对应的列名 默认为"url" 用于指定数据库连接地址
		 */
		private String url = "url";

		/**
		 * 数据库用户名对应的列名 默认为"username" 用于数据库认证
		 */
		private String username = "username";

		/**
		 * 数据库密码对应的列名 默认为"password" 用于数据库认证
		 */
		private String password = "password";

	}

}
