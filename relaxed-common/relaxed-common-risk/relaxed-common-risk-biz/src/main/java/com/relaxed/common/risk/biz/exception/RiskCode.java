package com.relaxed.common.risk.biz.exception;

import com.relaxed.common.model.result.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Yakir
 * @Topic RiskCode
 * @Description
 * @date 2021/9/19 17:37
 * @Version 1.0
 */
@RequiredArgsConstructor
@Getter
public enum RiskCode implements ResultCode {

    /**
     * 模型必备字段不允许删除
     */
    FIELD_NOT_ALLOWED_DEL(10000,"模型必备字段不允许删除"),

    ;
    private final Integer code;
    private final String message;
}
