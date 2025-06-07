package com.relaxed.starter.download.handler;

import cn.hutool.core.util.StrUtil;
import com.relaxed.starter.download.annotation.ResponseDownload;
import com.relaxed.starter.download.domain.DownloadModel;
import lombok.SneakyThrows;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

/**
 * 抽象下载处理器，提供了文件下载的基本实现。 实现了 {@link DownloadHandler} 和 {@link ApplicationContextAware} 接口，
 * 提供了文件下载的通用功能，如设置响应头、提取文件名等。
 *
 * @author Yakir
 * @since 1.0
 */
public abstract class AbstractDownloadHandler implements DownloadHandler, ApplicationContextAware {

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
	 * 执行文件下载操作 设置响应头、内容类型和字符编码，然后调用具体的写入方法
	 * @param downloadModel 下载模型
	 * @param response HTTP响应对象
	 * @param responseDownload 下载注解配置
	 */
	@Override
	public void download(DownloadModel downloadModel, HttpServletResponse response, ResponseDownload responseDownload) {
		String fileName = extractFileName(responseDownload, downloadModel);
		String contentType = MediaTypeFactory.getMediaType(fileName).map(MediaType::toString)
				.orElse(responseDownload.contentType());
		response.setContentType(contentType);
		response.setCharacterEncoding(responseDownload.charset());
		buildHeaders(response, responseDownload, fileName);
		try {
			write(downloadModel, response, responseDownload);
		}
		catch (Exception e) {
			response.setContentType("application/json");
			throw e;
		}
	}

	/**
	 * 将文件内容写入HTTP响应 具体的写入逻辑由子类实现
	 * @param downloadModel 下载模型
	 * @param response HTTP响应对象
	 * @param responseDownload 下载注解配置
	 */
	protected abstract void write(DownloadModel downloadModel, HttpServletResponse response,
			ResponseDownload responseDownload);

	/**
	 * 构建HTTP响应头 设置内容处理方式（内联或附件）和自定义响应头
	 * @param response HTTP响应对象
	 * @param responseDownload 下载注解配置
	 * @param fileName 文件名
	 */
	protected void buildHeaders(HttpServletResponse response, ResponseDownload responseDownload, String fileName) {
		boolean inline = responseDownload.inline();
		String contentDisplay = inline ? "inline" : "attachment";
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, contentDisplay + ";filename=" + fileName);
		response.addHeader("Access-Control-Expose-Headers", "Content-Disposition,download-filename");
		response.setHeader("download-filename", fileName);
		// 注解上指定的响应头
		String[] headers = responseDownload.headers();
		if (headers.length % 2 == 0) {
			for (int i = 0; i < headers.length; i += 2) {
				response.setHeader(headers[i], headers[i + 1]);
			}
		}
	}

	/**
	 * 从下载模型和注解配置中提取文件名 优先使用显示文件名，如果没有则使用实际文件名
	 * @param responseDownload 下载注解配置
	 * @param downloadModel 下载模型
	 * @return 编码后的文件名
	 */
	@SneakyThrows
	protected String extractFileName(ResponseDownload responseDownload, DownloadModel downloadModel) {
		String displayFileName = StrUtil.isBlank(downloadModel.getDisplayFileName()) ? downloadModel.getFileName()
				: downloadModel.getDisplayFileName();
		return URLEncoder.encode(displayFileName, "UTF-8");
	}

}
