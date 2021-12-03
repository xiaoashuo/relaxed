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
        if (sourcePath.startsWith(OssConstants.SLASH + bucket)) {
            return sourcePath.substring((OssConstants.SLASH + bucket).length());
        }
        else {
            return sourcePath;
        }
    }
}
