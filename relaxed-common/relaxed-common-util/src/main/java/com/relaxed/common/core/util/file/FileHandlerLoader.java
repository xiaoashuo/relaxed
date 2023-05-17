package com.relaxed.common.core.util.file;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Yakir
 * @Topic FileHandlerLoader
 * @Description
 * @date 2023/5/17 13:45
 * @Version 1.0
 */
@Slf4j
public class FileHandlerLoader {

    private static ConcurrentHashMap<String,FileHandler> FILE_HANDLER_HOLDER=new ConcurrentHashMap<>();

    static {
        log.info("文件处理器加载启动");
        ServiceLoader<FileHandler> loadedDrivers = ServiceLoader
                .load(FileHandler.class);
        for (FileHandler drive : loadedDrivers) {
            String supportType = drive.supportType();
            FILE_HANDLER_HOLDER.put(supportType, drive);
            log.info("文件处理器已加载类型{},全路径名称{}",supportType,drive.getClass());
        }
        if (FILE_HANDLER_HOLDER.size()==0){
            log.info("文件处理器未加载到拓展处理器,默认使用本地处理器,类型{}",FileConstants.DEFAULT_HANDLE_TYPE);
            FILE_HANDLER_HOLDER.put(FileConstants.DEFAULT_HANDLE_TYPE,new LocalFileHandler());
        }

        log.info("文件处理器加载结束,已加载{}个",FILE_HANDLER_HOLDER.size());

    }


    public  static  FileHandler load(String supportType) {
        return FILE_HANDLER_HOLDER.get(supportType);
    }


}
