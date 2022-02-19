package com.relaxed.starter.download.handler;

import com.relaxed.starter.download.annotation.ResponseDownload;
import com.relaxed.starter.download.domain.DownloadModel;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Yakir
 * @Topic DownloadHandler
 * @Description
 * @date 2022/2/18 14:27
 * @Version 1.0
 */
public interface DownloadHandler {

	/**
	 * 是否支持
	 * @param downloadModel DownloadModel
	 * @param responseDownload
	 * @return
	 */
	boolean support(DownloadModel downloadModel, ResponseDownload responseDownload);

	/**
	 * 返回的对象
	 * @param downloadModel DownloadModel
	 * @param response 输出对象
	 * @param responseDownload 注解
	 */
	void download(DownloadModel downloadModel, HttpServletResponse response, ResponseDownload responseDownload);

}
