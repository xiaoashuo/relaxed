package com.relaxed.starter.download.handler;

import com.relaxed.starter.download.annotation.ResponseDownload;

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
	 * @param o obj
	 * @param responseDownload
	 * @return
	 */
	boolean support(Object o, ResponseDownload responseDownload);

	/**
	 * 校验
	 * @param o obj
	 * @param responseDownload 注解
	 */
	void check(Object o, ResponseDownload responseDownload);

	/**
	 * 返回的对象
	 * @param o obj
	 * @param response 输出对象
	 * @param responseDownload 注解
	 */
	void download(Object o, HttpServletResponse response, ResponseDownload responseDownload);

}
