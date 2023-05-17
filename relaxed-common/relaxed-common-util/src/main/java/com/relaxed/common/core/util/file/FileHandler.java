package com.relaxed.common.core.util.file;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author Yakir
 * @Topic FileHandler
 * @Description
 * @date 2023/5/17 11:39
 * @Version 1.0
 */
public interface FileHandler {

    String upload(String basePath, String filename,String separator, MultipartFile file);
}
