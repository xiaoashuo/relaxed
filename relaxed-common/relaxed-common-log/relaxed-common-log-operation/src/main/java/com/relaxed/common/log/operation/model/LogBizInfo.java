package com.relaxed.common.log.operation.model;

import lombok.Data;
import sun.reflect.generics.tree.ReturnType;

import java.util.Map;

/**
 * @author Yakir
 * @Topic LogBizInfo
 * @Description
 * @date 2023/12/18 14:31
 * @Version 1.0
 */
@Data
public class LogBizInfo {
    /**
     * 操作者
     */
    private String operator;

    /**
     * 业务id
     */
    private String bizNo;
    /**
     * 操作类型
     */
    private String type;

    /**
     * 成功操作内容
     */
    private String content;

    /**
     * 操作时间 时间戳单位：ms
     */
    private Long operateTime;

    /**
     * 操作花费的时间 单位：ms
     */
    private Long executeTime;

    /**
     * 是否调用成功
     */
    private Boolean success;
    /**
     * 函数执行成功文本
     */
    private String successText;
    /**
     * 函数执行失败的日志文本
     */
    private String failText;

    /**
     * 执行后返回的json字符串
     */
    private String result;

    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 详细
     */
    private String details;

    private Throwable throwable;

    private Map<String,String> fieldMap;

    /**
     * 函数执行结果map
     */
    private Map<String,String> funcResMap;

    public class FuncRes{


        private Class ReturnType;
        private Object val;
    }
}
