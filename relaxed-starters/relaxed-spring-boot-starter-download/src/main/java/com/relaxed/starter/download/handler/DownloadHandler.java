package com.relaxed.starter.download.handler;

import com.relaxed.starter.download.annotation.ResponseDownload;
import com.relaxed.starter.download.domain.DownloadModel;

import javax.servlet.http.HttpServletResponse;

/**
 * 下载处理器接口，定义了文件下载的基本操作。 实现此接口的类需要提供文件下载的支持判断和具体的下载实现。
 *
 * @author Yakir
 * @since 1.0
 */
public interface DownloadHandler {

	/**
	 * 判断是否支持指定的下载模型和下载配置
	 * @param downloadModel 下载模型，包含文件信息
	 * @param responseDownload 下载配置，包含下载参数
	 * @return true 如果支持此下载请求，false 否则
	 */
	boolean support(DownloadModel downloadModel, ResponseDownload responseDownload);

	/**
	 * 执行文件下载操作 将文件内容写入HTTP响应
	 * @param downloadModel 下载模型，包含文件信息
	 * @param response HTTP响应对象，用于输出文件内容
	 * @param responseDownload 下载配置，包含下载参数
	 */
	void download(DownloadModel downloadModel, HttpServletResponse response, ResponseDownload responseDownload);

}
