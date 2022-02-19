package com.relaxed.starter.download.handler;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.relaxed.starter.download.annotation.ResponseDownload;
import com.relaxed.starter.download.domain.DownloadModel;
import com.relaxed.starter.download.enums.DownTypeEnum;
import com.relaxed.starter.download.exception.DownloadException;
import com.relaxed.starter.download.functions.DownloadCallback;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class DownloadHandlerChain implements ApplicationContextAware {

	private final List<DownloadHandler> downloadHandlerList;

	protected ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

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
		if (DownTypeEnum.OTHER.equals(responseDownload.channel()) && isInterface(responseDownload.customHandler())) {
			throw new DownloadException("@ResponseDownload customHandler 必须为自定义实列bean");
		}
	}

	public void process(Object returnValue, HttpServletResponse response, ResponseDownload responseDownload) {
		check(returnValue, responseDownload);
		DownloadModel downloadModel = (DownloadModel) returnValue;
		if (DownTypeEnum.OTHER.equals(responseDownload.channel())) {
			applicationContext.getBean(responseDownload.customHandler()).download(downloadModel, response,
					responseDownload);
			return;
		}
		try {
			downloadHandlerList.stream().filter(handler -> handler.support(downloadModel, responseDownload)).findFirst()
					.ifPresent(handler -> handler.download(downloadModel, response, responseDownload));
		}
		finally {
			Optional.ofNullable(downloadModel.getDownloadCallback()).ifPresent(DownloadCallback::postProcess);
		}

	}

	/**
	 * 是否为 custom downloadHandler
	 * @param downloadHandlerClass
	 * @return true 是 默认值 false 不是
	 */
	private boolean isInterface(Class<? extends DownloadHandler> downloadHandlerClass) {
		return Modifier.isInterface(downloadHandlerClass.getModifiers());
	}

}
