package com.relaxed.common.datasource.provider;

import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;

/**
 * 数据源属性提供者接口 定义数据源属性的创建和配置方法 可以通过实现此接口来自定义数据源属性的创建逻辑 支持自定义数据源连接池属性、连接参数等配置
 *
 * @author Yakir
 */
public interface PropertyProvider {

	/**
	 * 创建数据源配置属性 根据提供的参数创建并配置数据源属性对象 可以自定义数据源的连接池属性、连接参数等
	 * @param dsName 数据源名称，用于标识数据源，将作为连接池名称
	 * @param url 数据库连接URL，指定数据库服务器的地址和连接参数
	 * @param username 数据库用户名，用于连接认证
	 * @param password 数据库密码，用于连接认证
	 * @return 配置好的数据源属性对象，包含完整的连接信息
	 */
	DataSourceProperty prodDataSourceProperty(String dsName, String url, String username, String password);

}
