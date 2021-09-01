package com.relaxed.common.ip;

import org.lionsoul.ip2region.DbSearcher;

/**
 * @author Yakir
 * @Topic IpSearcher
 * @Description
 * @date 2021/9/1 11:09
 * @Version 1.0
 */
public interface IpSearcher {

	/**
	 * real target
	 * @author yakir
	 * @date 2021/9/1 11:51
	 * @return org.lionsoul.ip2region.DbSearcher
	 */
	DbSearcher getDelegate();

}
