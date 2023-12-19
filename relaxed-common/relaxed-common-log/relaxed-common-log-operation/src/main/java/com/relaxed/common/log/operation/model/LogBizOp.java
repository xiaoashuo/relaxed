package com.relaxed.common.log.operation.model;

import lombok.Builder;
import lombok.Data;

/**
 * @author Yakir
 * @Topic LogBizOp
 * @Description
 * @date 2023/12/18 13:51
 * @Version 1.0
 */
@Builder
@Data
public class LogBizOp {
    private String operator;

    private String bizNo;

    private String module;

    private String type;

    private String success;

    private String fail;

    private String details;

    private String condition;

}
