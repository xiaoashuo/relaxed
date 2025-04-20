package com.relaxed.common.ip;

import org.lionsoul.ip2region.DbSearcher;

/**
 * IP地址搜索器接口，用于获取IP地址查询的委托对象。 该接口定义了获取IP地址查询核心功能的方法，通过委托模式将具体的IP地址查询实现
 * 与接口定义分离，提供了更好的扩展性和灵活性。
 *
 * @author Yakir
 * @since 1.0
 */
public interface IpSearcher {

	/**
	 * 获取IP地址查询的委托对象。 该方法返回一个DbSearcher实例，用于执行具体的IP地址查询操作。
	 * DbSearcher是IP2Region库提供的核心查询类，支持高效的内存和文件查询模式。
	 * @return 返回IP地址查询的委托对象，用于执行具体的IP地址查询操作
	 */
	DbSearcher getDelegate();

}
