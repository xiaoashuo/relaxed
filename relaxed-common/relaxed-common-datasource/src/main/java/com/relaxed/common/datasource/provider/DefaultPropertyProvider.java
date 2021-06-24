package com.relaxed.common.datasource.provider;

import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;

/**
 * @author Yakir
 * @Topic DefaultDbPropertyProvider
 * @Description
 * @date 2021/6/24 8:51
 * @Version 1.0
 */
public class DefaultPropertyProvider implements PropertyProvider {

	@Override
	public DataSourceProperty prodDataSourceProperty(String dsName, String url, String username, String password) {
		DataSourceProperty dataSourceProperty = new DataSourceProperty();
		dataSourceProperty.setPoolName(dsName);
		dataSourceProperty.setUrl(url);
		dataSourceProperty.setUsername(username);
		dataSourceProperty.setPassword(password);
		return dataSourceProperty;
	}

}
