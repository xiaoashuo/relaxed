package com.relaxed.starter.download.handler;

import cn.hutool.extra.servlet.ServletUtil;
import com.relaxed.common.jsch.sftp.client.ISftpClient;
import com.relaxed.starter.download.annotation.ResponseDownload;
import com.relaxed.starter.download.domain.DownloadModel;
import com.relaxed.starter.download.enums.DownTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

/**
 * @author Yakir
 * @Topic SftpDownloadHandler
 * @Description
 * @date 2022/2/18 14:34
 * @Version 1.0
 */
@Slf4j
@RequiredArgsConstructor
public class SftpDownloadHandler extends AbstractDownloadHandler {

	private final ISftpClient iSftpClient;

	@Override
	public boolean support(DownloadModel downloadModel, ResponseDownload responseDownload) {
		return DownTypeEnum.SFTP.equals(responseDownload.channel());
	}

	@SneakyThrows
	@Override
	protected void write(DownloadModel downloadModel, HttpServletResponse response, ResponseDownload responseDownload) {
		String parentPath = downloadModel.getParentPath();
		String fileName = downloadModel.getFileName();
		try (InputStream inputStream = iSftpClient.exec(sftp -> sftp.getInputStream(parentPath, fileName))) {
			ServletUtil.write(response, inputStream);
		}
		catch (Exception e) {
			log.error("下载文件,渠道{},路径{}，名称{}异常", responseDownload.channel(), parentPath, fileName, e);
			throw e;
		}
	}

}
