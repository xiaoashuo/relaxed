package com.relaxed.test.utils.file;

import lombok.SneakyThrows;
import org.springframework.util.ResourceUtils;

import java.io.File;

/**
 * @author Yakir
 * @Topic FileUtils
 * @Description
 * @date 2024/12/27 11:24
 * @Version 1.0
 */
public class FileUtils {

    /**
     * 加载类路径下文件路径
     * @param filepath 文件路径不需要以/开头
     * @return
     */
    @SneakyThrows
    public static File loadClassPathFile(String filepath){
        return ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + filepath);
    }
}
