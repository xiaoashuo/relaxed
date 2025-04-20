package com.relaxed.common.datasource.provider;

import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;

/**
 * 默认数据源属性提供者实现类 实现PropertyProvider接口，提供基本的数据源属性创建功能 设置数据源的基本属性，包括名称、URL、用户名和密码
 * 默认启用延迟初始化，提高系统启动性能
 *
 * @author Yakir
 */
public class DefaultPropertyProvider implements PropertyProvider {

	/**
	 * 创建数据源配置属性 设置数据源的基本属性，并启用延迟初始化 创建的数据源属性包含： 1. 连接池名称 2. 数据库连接URL 3. 数据库用户名 4. 数据库密码
	 * 5. 延迟初始化标志
	 * @param dsName 数据源名称，将作为连接池名称
	 * @param url 数据库连接URL，指定数据库服务器地址和参数
	 * @param username 数据库用户名，用于连接认证
	 * @param password 数据库密码，用于连接认证
	 * @return 配置好的数据源属性对象
	 */
	@Override
	public DataSourceProperty prodDataSourceProperty(String dsName, String url, String username, String password) {
		DataSourceProperty dataSourceProperty = new DataSourceProperty();
		dataSourceProperty.setPoolName(dsName);
		dataSourceProperty.setUrl(url);
		dataSourceProperty.setUsername(username);
		dataSourceProperty.setPassword(password);
		dataSourceProperty.setLazy(true);
		return dataSourceProperty;
	}

}
