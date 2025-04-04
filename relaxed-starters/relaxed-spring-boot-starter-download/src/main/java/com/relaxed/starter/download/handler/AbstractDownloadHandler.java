package com.relaxed.starter.download.handler;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.relaxed.starter.download.annotation.ResponseDownload;
import com.relaxed.starter.download.domain.DownloadModel;
import com.relaxed.starter.download.exception.DownloadException;
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
 * @author Yakir
 * @Topic AbstractDownloadHandler
 * @Description
 * @date 2022/2/18 14:28
 * @Version 1.0
 */
public abstract class AbstractDownloadHandler implements DownloadHandler, ApplicationContextAware {

	protected ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void download(DownloadModel downloadModel, HttpServletResponse response, ResponseDownload responseDownload) {
		String fileName = extractFileName(responseDownload, downloadModel);
		String contentType = MediaTypeFactory.getMediaType(fileName).map(MediaType::toString)
				.orElse(responseDownload.contentType());
		response.setContentType(contentType);
		response.setCharacterEncoding(responseDownload.charset());
		buildHeaders(response, responseDownload, fileName);
		write(downloadModel, response, responseDownload);

	}

	/**
	 * 写入文件
	 * @param downloadModel
	 * @param response
	 * @param responseDownload
	 */
	protected abstract void write(DownloadModel downloadModel, HttpServletResponse response,
			ResponseDownload responseDownload);

	/**
	 * 构建响应头
	 * @param response
	 * @param responseDownload
	 */
	protected void buildHeaders(HttpServletResponse response, ResponseDownload responseDownload, String fileName) {
		boolean inline = responseDownload.inline();
		String contentDisplay = inline ? "inline" : "attachment";
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, contentDisplay + ";filename=" + fileName);
		// 注解上指定的响应头
		String[] headers = responseDownload.headers();
		if (headers.length % 2 == 0) {
			for (int i = 0; i < headers.length; i += 2) {
				response.setHeader(headers[i], headers[i + 1]);
			}
		}
	}

	/**
	 * 提取文件名
	 * @param responseDownload
	 * @param downloadModel
	 * @return
	 */
	@SneakyThrows
	protected String extractFileName(ResponseDownload responseDownload, DownloadModel downloadModel) {
		String displayFileName = StrUtil.isBlank(downloadModel.getDisplayFileName()) ? downloadModel.getFileName()
				: downloadModel.getDisplayFileName();
		return URLEncoder.encode(displayFileName, "UTF-8");
	}

}
