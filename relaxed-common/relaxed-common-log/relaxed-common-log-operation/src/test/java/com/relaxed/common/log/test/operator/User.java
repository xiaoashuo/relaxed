package com.relaxed.common.log.test.operator;

import lombok.Data;

/**
 * @author Yakir
 * @Topic User
 * @Description
 * @date 2023/12/14 15:09
 * @Version 1.0
 */
@Data
public class User {

	/**
	 * 用户名称
	 */
	private String username;

	/**
	 * 状态 1 待发货 2 发货中 3 待收货 4 待评价 5已完成
	 */
	private Integer status;

	/**
	 * 物流单号
	 */
	private String bizNo;

}
