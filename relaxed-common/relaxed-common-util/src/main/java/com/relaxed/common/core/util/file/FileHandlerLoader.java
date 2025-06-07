package com.relaxed.common.core.util.file;

import cn.hutool.core.lang.Assert;
import lombok.extern.slf4j.Slf4j;

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

	private static ConcurrentHashMap<String, FileHandler> FILE_HANDLER_HOLDER = new ConcurrentHashMap<>();

	static {
		log.info("文件处理器加载启动");
		FILE_HANDLER_HOLDER.put(FileConstants.DEFAULT_HANDLE_TYPE, new LocalFileHandler());
		log.info("文件处理器已加载默认本地处理器,类型{}", FileConstants.DEFAULT_HANDLE_TYPE);
		ServiceLoader<FileHandler> loadedDrivers = ServiceLoader.load(FileHandler.class);
		for (FileHandler drive : loadedDrivers) {
			String supportType = drive.supportType();
			FILE_HANDLER_HOLDER.put(supportType, drive);
			log.info("文件处理器已加载类型{},全路径名称{}", supportType, drive.getClass());
		}
		log.info("文件处理器加载结束,已加载{}个", FILE_HANDLER_HOLDER.size());

	}

	/**
	 * 加载指定文件处理器
	 * @param supportType
	 * @return
	 */
	public static FileHandler load(String supportType) {
		return FILE_HANDLER_HOLDER.get(supportType);
	}

	/**
	 * 注册文件处理器
	 * @param fileHandler
	 * @return
	 */
	public static FileHandler register(FileHandler fileHandler) {
		Assert.notNull(fileHandler, "文件处理器不能为空");
		log.info("文件处理器已加载类型{},全路径名称{}", fileHandler.supportType(), fileHandler.getClass());
		return FILE_HANDLER_HOLDER.put(fileHandler.supportType(), fileHandler);
	}

	/**
	 * 文件处理器列表
	 * @return
	 */
	public static ConcurrentHashMap<String, FileHandler> list() {
		return FILE_HANDLER_HOLDER;
	}

}
