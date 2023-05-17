package com.relaxed.common.core.util.file;

import cn.hutool.core.util.IdUtil;
import lombok.SneakyThrows;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @author Yakir
 * @Topic LocalFileHandler
 * @Description
 * @date 2023/5/17 11:43
 * @Version 1.0
 */
public class LocalFileHandler implements FileHandler {
    @SneakyThrows
    @Override
    public String upload(String basePath, String filename, String separator, MultipartFile file) {
        File desc = getAbsoluteFile(separator,basePath, filename);
        file.transferTo(desc);
        String fileId = IdUtil.getSnowflakeNextId() + "";
        return fileId;
    }


    private File getAbsoluteFile(String separator, String dirPath, String fileName) {
        File desc = new File(dirPath + separator + fileName);

        if (!desc.exists()) {
            if (!desc.getParentFile().exists()) {
                desc.getParentFile().mkdirs();
            }
        }
        return desc;
    }
}
