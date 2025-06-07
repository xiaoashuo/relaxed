package com.relaxed.starter.download.handler;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.relaxed.common.core.util.file.FileHandler;
import com.relaxed.common.core.util.file.FileHandlerLoader;
import com.relaxed.starter.download.annotation.ResponseDownload;
import com.relaxed.starter.download.domain.DownloadModel;
import com.relaxed.starter.download.exception.DownloadException;
import com.relaxed.starter.download.functions.DownloadCallback;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * 下载处理器链，用于管理和执行文件下载操作。 支持多种下载处理器，可以根据下载类型选择合适的处理器执行下载操作。 同时支持自定义下载处理器的注册和使用。
 *
 * @author Yakir
 * @since 1.0
 */
@RequiredArgsConstructor
public class DownloadHandlerChain implements ApplicationContextAware {

	private final DownloadHandler downloadHandler;

	protected ApplicationContext applicationContext;

	/**
	 * 设置Spring应用上下文
	 * @param applicationContext Spring应用上下文
	 * @throws BeansException 如果设置上下文时发生错误
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	/**
	 * 检查下载参数的有效性 验证返回值的类型、文件类型、文件名和响应头的配置
	 * @param o 返回值对象
	 * @param responseDownload 下载注解配置
	 * @throws DownloadException 如果参数验证失败
	 */
	public void check(Object o, ResponseDownload responseDownload) {
		if (!(o instanceof DownloadModel)) {
			throw new DownloadException("return value  type must be  DownloadModel");
		}
		DownloadModel downloadModel = (DownloadModel) o;
		if (StrUtil.isEmpty(downloadModel.getFileType())) {
			throw new DownloadException("@ResponseDownload fileType 配置不存在");
		}

		if (StrUtil.isEmpty(downloadModel.getFileName())) {
			throw new DownloadException("filename can not be null");
		}
		String[] headers = responseDownload.headers();
		if (ArrayUtil.isNotEmpty(headers) && headers.length % 2 != 0) {
			throw new DownloadException("@ResponseDownload headers 必须为2的倍数");
		}
		FileHandler fileHandler = FileHandlerLoader.load(responseDownload.channel());
		if (fileHandler == null) {
			throw new DownloadException("@ResponseDownload " + responseDownload.channel() + " 对应处理器不存在");
		}
	}

	/**
	 * 执行文件下载操作 根据下载类型选择合适的处理器，执行下载操作 支持自定义下载处理器的使用
	 * @param returnValue 返回值对象
	 * @param response HTTP响应对象
	 * @param responseDownload 下载注解配置
	 */
	public void process(Object returnValue, HttpServletResponse response, ResponseDownload responseDownload) {
		check(returnValue, responseDownload);
		DownloadModel downloadModel = (DownloadModel) returnValue;
		try {
			boolean isSupport = downloadHandler.support(downloadModel, responseDownload);
			Assert.isTrue(isSupport, "渠道:{},对应处理器不存在");
			downloadHandler.download(downloadModel, response, responseDownload);
		}
		finally {
			Optional.ofNullable(downloadModel.getDownloadCallback()).ifPresent(DownloadCallback::postProcess);
		}
	}

}
