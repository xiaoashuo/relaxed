package com.relaxed.common.risk.engine.model.dto;

import com.relaxed.common.model.result.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Yakir
 * @Topic RiskResultCode
 * @Description
 * @date 2021/8/29 11:00
 * @Version 1.0
 */
@RequiredArgsConstructor
@Getter
public enum RiskResultCode implements ResultCode {

	/**
	 * 模型不存在
	 */
	MODEL_NOT_EXISTS(20001, "模型不存在"),
	/**
	 * 模型未启用
	 */
	MODEL_DISABLED(20002, "模型未启用"),
	/**
	 * 字段验证未通过
	 */
	FIELD_VALID_NOT_PASSED(20003, "字段验证未通过"),
	/**
	 * 风控评估未通过
	 */
	RISK_EVAL_NOT_PASSED(20004, "风控评估未通过"),
	/**
	 * 风控评估报告过期
	 */
	RISK_EVAL_EXPIRED(20005, "风控评估报告已过期"),

	;

	private final Integer code;

	private final String message;

}
