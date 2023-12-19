package com.relaxed.common.log.operation.model;

import lombok.Data;

/**
 * @author Yakir
 * @Topic MethodExecResult
 * @Description
 * @date 2023/12/18 14:49
 * @Version 1.0
 */
@Data
public class MethodExecResult {

    private boolean success;

    private Throwable throwable;

    private String errMsg;

    private Long operateTime;

    private Long executeTime;

    public MethodExecResult(boolean success) {
        this.success = success;
        this.operateTime = System.currentTimeMillis();
    }

    public void exception(Throwable throwable) {
        this.success = false;
        this.executeTime = System.currentTimeMillis() - this.operateTime;
        this.throwable = throwable;
        this.errMsg = throwable.getMessage();
    }
}
