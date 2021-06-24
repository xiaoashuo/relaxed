package com.relaxed.common.datasource.provider;

import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;

/**
 * @author Yakir
 * @Topic DbPropertyProvider
 * @Description
 * @date 2021/6/24 8:50
 * @Version 1.0
 */
public interface PropertyProvider {

	/**
	 * 获得数据源配置实体
	 * @param dsName 数据源名称
	 * @param url 数据库连接
	 * @param username 数据库用户名
	 * @param password 数据库密码
	 * @return 数据源配置
	 */
	DataSourceProperty prodDataSourceProperty(String dsName, String url, String username, String password);

}
