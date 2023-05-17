package com.relaxed.common.core.util.file;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;


import java.io.File;

/**
 * @author Yakir
 * @Topic FileTest
 * @Description
 * @date 2023/5/17 11:17
 * @Version 1.0
 */
@Slf4j
public class FileTest {

    @Test
    void fileUpload(){
        String rootPath="D:\\other\\100000";
        String relativePath="file";
        String uploadFilePath="D:\\other\\100000\\jieqingzhengming_131331131313.pdf";
        FileMultipartFile fileMultipartFile = new FileMultipartFile("file", new File(uploadFilePath));
        FileMeta fileMeta = FileUtils.upload(rootPath, relativePath, fileMultipartFile, FileConfig.create().splitDate(true));
        log.info("upload  successfully,result:[{}] ,相对路径[{}],完整路径[{}]", JSONUtil.toJsonStr(fileMeta),fileMeta.getRelativeFilePath(),fileMeta.getFullFilePath());
    }

    @Test
    void fileDelete(){
        String rootPath="D:\\other\\100000";
        String relativePath="\\file\\20230517\\J2GyZCZfUXf1cnt3uXGuS.pdf";
        boolean deleted = FileUtils.delete(rootPath, relativePath);
        log.info("deleted  successfully,result:[{}]", deleted);
    }

    @Test
    void fileLoad(){
        FileHandler test = FileHandlerLoader.load(FileConstants.DEFAULT_HANDLE_TYPE);
        System.out.println(test);
    }
}
