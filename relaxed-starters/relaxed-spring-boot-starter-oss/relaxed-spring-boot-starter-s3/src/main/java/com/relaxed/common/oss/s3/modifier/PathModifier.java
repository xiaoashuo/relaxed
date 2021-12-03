package com.relaxed.common.oss.s3.modifier;

/**
 * @author Yakir
 * @Topic PathModifier
 * @Description
 * @date 2021/12/3 14:50
 * @Version 1.0
 */
public interface PathModifier {

    /**
     * 修改请求path
     * @author yakir
     * @date 2021/12/3 14:56
     * @param optionName 操作名称
     * @param sourcePath 源路径
     * @return java.lang.String
     */
    String modifyRequestPath(String bucket,String optionName,String sourcePath);
}
