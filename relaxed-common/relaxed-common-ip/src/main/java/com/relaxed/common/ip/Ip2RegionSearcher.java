package com.relaxed.common.ip;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.lionsoul.ip2region.DbSearcher;

/**
 * IP2Region查询器实现类，用于执行IP地址查询操作。 该类实现了IpSearcher接口，通过委托DbSearcher实例来完成具体的IP地址查询功能。
 * 使用IP2Region库提供的DbSearcher进行高效的IP地址查询，支持内存和文件两种查询模式。
 *
 * @author Yakir
 * @since 1.0
 */
@RequiredArgsConstructor
public class Ip2RegionSearcher implements IpSearcher {

	/**
	 * IP地址查询委托对象，用于执行实际的IP地址查询操作。 该对象由IP2Region库提供，支持高效的IP地址查询，包括国家、省份、城市等详细信息。
	 */
	private final DbSearcher delegate;

	/**
	 * 获取IP地址查询委托对象。 该方法返回配置好的DbSearcher实例，用于执行具体的IP地址查询操作。
	 * @return IP地址查询委托对象，用于执行具体的IP地址查询操作
	 */
	@Override
	public DbSearcher getDelegate() {
		return delegate;
	}

}
