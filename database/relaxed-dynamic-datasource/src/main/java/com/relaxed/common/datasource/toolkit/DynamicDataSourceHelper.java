package com.relaxed.common.datasource.toolkit;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.DataSourceCreator;
import com.baomidou.dynamic.datasource.creator.DefaultDataSourceCreator;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.relaxed.common.datasource.provider.PropertyProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * 动态数据源帮助类 提供数据源管理、密码加密解密、数据源校验等功能 支持动态添加和删除数据源
 *
 * @author Yakir
 */
@Slf4j
@RequiredArgsConstructor
public class DynamicDataSourceHelper {

	/**
	 * 密码加密工具 用于数据库密码的加密和解密
	 */
	private final StringEncryptor stringEncryptor;

	/**
	 * 数据连接池创建者 用于创建和管理数据源连接池
	 */
	private final DefaultDataSourceCreator dataSourceCreator;

	/**
	 * 动态路由数据源 用于管理多个数据源的路由和切换
	 */
	private final DynamicRoutingDataSource dynamicRoutingDataSource;

	/**
	 * 数据源属性提供者 用于创建和配置数据源属性
	 */
	private final PropertyProvider propertyProvider;

	/**
	 * 加密明文密码 使用配置的加密工具对密码进行加密
	 * @param pass 明文密码
	 * @return 加密后的密码
	 */
	public String encryptPass(String pass) {
		return stringEncryptor.encrypt(pass);
	}

	/**
	 * 解密密码 使用配置的加密工具对密码进行解密
	 * @param password 加密后的密码
	 * @return 解密后的明文密码
	 */
	public String decryptPassword(String password) {
		return stringEncryptor.decrypt(password);
	}

	/**
	 * 校验数据源配置是否可用 通过尝试建立数据库连接来验证数据源配置是否正确
	 * @param dataSourceProperty 数据源配置信息
	 * @return 是否可用，true表示不可用，false表示可用
	 */
	public boolean isErrorDataSourceProperty(DataSourceProperty dataSourceProperty) {
		try (Connection ignored = DriverManager.getConnection(dataSourceProperty.getUrl(),
				dataSourceProperty.getUsername(), dataSourceProperty.getPassword())) {
			if (log.isDebugEnabled()) {
				log.debug("check connection success, dataSourceProperty: {}", dataSourceProperty);
			}
		}
		catch (Exception e) {
			log.error("get connection error, dataSourceProperty: {}", dataSourceProperty, e);
			return true;
		}
		return false;
	}

	/**
	 * 添加动态数据源 根据配置信息创建新的数据源并添加到动态路由中
	 * @param dataSourceProperty 数据源配置信息
	 */
	public void addDynamicDataSource(DataSourceProperty dataSourceProperty) {
		DataSource dataSource = dataSourceCreator.createDataSource(dataSourceProperty);
		dynamicRoutingDataSource.addDataSource(dataSourceProperty.getPoolName(), dataSource);
	}

	/**
	 * 删除数据源 从动态路由中移除指定的数据源
	 * @param name 数据源名称
	 */
	public void removeDataSource(String name) {
		dynamicRoutingDataSource.removeDataSource(name);
	}

	/**
	 * 创建数据源配置属性 使用属性提供者创建数据源配置对象
	 * @param dsName 数据源名称
	 * @param url 数据库连接URL
	 * @param username 数据库用户名
	 * @param password 数据库密码
	 * @return 数据源配置属性对象
	 */
	public DataSourceProperty prodDataSourceProperty(String dsName, String url, String username, String password) {
		return propertyProvider.prodDataSourceProperty(dsName, url, username, password);
	}

}
