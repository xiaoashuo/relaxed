package com.relaxed.common.core.util.file;

import cn.hutool.core.io.FileUtil;
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


    @Override
    public String supportType() {
        return FileConstants.DEFAULT_HANDLE_TYPE;
    }

    @SneakyThrows
    @Override
    public String upload(String dirPath, String filename, String separator, MultipartFile file) {
        File desc = getAbsoluteFile(separator,dirPath, filename);
        file.transferTo(desc);
        String
                fileId = IdUtil.getSnowflakeNextId() + "";
        return fileId;
    }

    @Override
    public boolean delete(String rootPath, String filename) {
        return FileUtil.del(rootPath + filename);
    }

    @Override
    public File downloadFile(String rootPath, String relativePath) {
        return new File(rootPath, relativePath);
    }

    @Override
    public byte[] downloadByte(String rootPath, String relativePath) {
        return FileUtil.readBytes(downloadFile(rootPath, relativePath));
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
