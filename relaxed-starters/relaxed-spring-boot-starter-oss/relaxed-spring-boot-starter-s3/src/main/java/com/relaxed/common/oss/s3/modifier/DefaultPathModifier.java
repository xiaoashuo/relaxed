package com.relaxed.common.oss.s3.modifier;

import com.relaxed.common.oss.s3.OssConstants;

/**
 * @author Yakir
 * @Topic DefaultPathModifier
 * @Description
 * @date 2021/12/3 14:57
 * @Version 1.0
 */
public class DefaultPathModifier implements PathModifier{

    @Override
    public String modifyRequestPath(String bucket,String optionName, String sourcePath) {
       return  sourcePath.replaceFirst("/" + bucket, "");
    }
}
