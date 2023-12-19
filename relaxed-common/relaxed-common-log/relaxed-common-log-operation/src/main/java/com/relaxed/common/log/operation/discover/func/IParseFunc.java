package com.relaxed.common.log.operation.discover.func;

/**
 * @author Yakir
 * @Topic IFuncService
 * @Description
 * @date 2023/12/15 9:37
 * @Version 1.0
 */
public interface IParseFunc {

    /**
     * 命名空间
     * @return
     */
    String namespace();
    /**
     * function名称
     * @return name
     */
    String name();

    /**
     * function实现
     * @param args function入参
     * @return parsed string
     */
    String apply(Object[] args);
}
