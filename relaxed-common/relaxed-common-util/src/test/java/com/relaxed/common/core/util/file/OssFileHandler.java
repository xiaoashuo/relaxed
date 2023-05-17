package com.relaxed.common.core.util.file;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author Yakir
 * @Topic OssFileHandler
 * @Description
 * @date 2023/5/17 14:10
 * @Version 1.0
 */
public class OssFileHandler implements FileHandler {
    @Override
    public String supportType() {
        return "oss";
    }

    @Override
    public String upload(String dirPath, String filename, String separator, MultipartFile file) {
        return null;
    }

    @Override
    public boolean delete(String rootPath, String relativePath) {
        return false;
    }
}
