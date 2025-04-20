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
 * SFTP download handler implementation that handles file downloads from SFTP servers.
 * This handler extends {@link AbstractDownloadHandler} and uses {@link ISftpClient} to
 * retrieve files from SFTP servers and write them to the HTTP response.
 *
 * @author Yakir
 * @since 1.0
 */
@Slf4j
@RequiredArgsConstructor
public class SftpDownloadHandler extends AbstractDownloadHandler {

	private final ISftpClient iSftpClient;

	/**
	 * Checks if this handler supports the given download model and response download
	 * configuration.
	 * @param downloadModel the download model containing file information
	 * @param responseDownload the response download configuration
	 * @return true if the download channel is SFTP, false otherwise
	 */
	@Override
	public boolean support(DownloadModel downloadModel, ResponseDownload responseDownload) {
		return DownTypeEnum.SFTP.equals(responseDownload.channel());
	}

	/**
	 * Writes the file from SFTP server to the HTTP response. Opens an input stream from
	 * the SFTP server and writes the file content to the response.
	 * @param downloadModel the download model containing file information
	 * @param response the HTTP response to write the file to
	 * @param responseDownload the response download configuration
	 */
	@SneakyThrows
	@Override
	protected void write(DownloadModel downloadModel, HttpServletResponse response, ResponseDownload responseDownload) {
		String parentPath = downloadModel.getParentPath();
		String fileName = downloadModel.getFileName();
		try (InputStream inputStream = iSftpClient.supplyOpen(sftp -> sftp.getInputStream(parentPath, fileName))) {
			ServletUtil.write(response, inputStream);
		}
		catch (Exception e) {
			log.error("下载文件,渠道{},路径{}，名称{}异常", responseDownload.channel(), parentPath, fileName, e);
			throw e;
		}
	}

}
