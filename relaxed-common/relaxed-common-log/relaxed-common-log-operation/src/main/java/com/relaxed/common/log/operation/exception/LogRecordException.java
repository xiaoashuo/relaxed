package com.relaxed.common.log.operation.exception;

/**
 * @author Yakir
 * @Topic LogRecordException
 * @Description
 * @date 2023/12/18 14:01
 * @Version 1.0
 */
public class LogRecordException extends RuntimeException{


    public LogRecordException(String message) {
        super(message);
    }

    public LogRecordException(String message, Throwable cause) {
        super(message, cause);
    }

    public LogRecordException(Throwable cause) {
        super(cause);
    }

    public LogRecordException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
