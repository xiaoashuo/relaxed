package com.relaxed.test.poi;

import lombok.Data;

/**
 * @author Yakir
 * @Topic TestData
 * @Description
 * @date 2024/3/26 11:08
 * @Version 1.0
 */
@Data
public class TestData {

	// 序号
	private String serialNumber;

	// 保险公司
	private String insCompany;

	// 车牌号/标的名称
	private String licensePlateNo;

	// 车架号/标的编号
	private String vehicleIdNo;

	// 投保人
	private String policyholder;

	// 被保险人
	private String insuredName;

	// 起保日期
	private String insValidityDate;

	// 终保日期
	private String insExpiryDate;

	// 金额
	private String busInsAmt;

	// 交强险（含车船税）金额
	private String clivtaInsAmtAndvehicleTaxAmt;

}
