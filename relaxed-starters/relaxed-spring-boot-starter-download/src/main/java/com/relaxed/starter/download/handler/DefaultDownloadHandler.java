package com.relaxed.starter.download.handler;

import com.relaxed.common.core.util.file.FileHandlerLoader;
import com.relaxed.common.core.util.file.FileStoreUtils;
import com.relaxed.starter.download.annotation.ResponseDownload;
import com.relaxed.starter.download.domain.DownloadModel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;

/**
 * DefaultDownloadHandler 文件下载处理器，用于处理文件系统的文件下载。 继承自
 * {@link AbstractDownloadHandler}，实现了本地文件的读取和下载功能。
 *
 * @author Yakir
 */
@Slf4j
public class DefaultDownloadHandler extends AbstractDownloadHandler {

	/**
	 * 判断是否支持本地文件下载
	 * @param downloadModel 下载模型
	 * @param responseDownload 下载注解配置
	 * @return true 如果下载渠道是本地文件，false 否则
	 */
	@Override
	public boolean support(DownloadModel downloadModel, ResponseDownload responseDownload) {
		return FileHandlerLoader.load(responseDownload.channel()) != null;
	}

	/**
	 * 将本地文件写入HTTP响应 从本地文件系统读取文件内容并写入响应流
	 * @param downloadModel 下载模型
	 * @param response HTTP响应对象
	 * @param responseDownload 下载注解配置
	 */
	@SneakyThrows
	@Override
	protected void write(DownloadModel downloadModel, HttpServletResponse response, ResponseDownload responseDownload) {
		String parentPath = downloadModel.getParentPath();
		String fileName = downloadModel.getFileName();
		String channel = responseDownload.channel();
		try {
			FileStoreUtils.writeToStream(channel, parentPath, fileName, response.getOutputStream());
		}
		catch (Exception e) {
			log.error("下载文件,渠道{} 路径{}，名称{}异常", responseDownload.channel(), parentPath, fileName, e);
			throw e;
		}

	}

}
