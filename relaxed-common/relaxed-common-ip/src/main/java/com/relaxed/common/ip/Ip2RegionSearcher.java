package com.relaxed.common.ip;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.lionsoul.ip2region.DbSearcher;

/**
 * @author Yakir
 * @Topic Ip2RegionSearcher
 * @Description
 * @date 2021/9/1 11:10
 * @Version 1.0
 */
@RequiredArgsConstructor
public class Ip2RegionSearcher implements IpSearcher {

	/**
	 * DB SEARCHER 委托者
	 */
	private final DbSearcher delegate;

	/**
	 * 返回db search
	 * @author yakir
	 * @date 2021/9/1 11:14
	 * @return org.lionsoul.ip2region.DbSearcher
	 */
	@Override
	public DbSearcher getDelegate() {
		return delegate;
	}

}
