package com.relaxed.starter.download.handler;

import cn.hutool.core.io.file.PathUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.relaxed.common.jsch.sftp.client.ISftpClient;
import com.relaxed.common.oss.s3.OssClient;
import com.relaxed.starter.download.annotation.ResponseDownload;
import com.relaxed.starter.download.domain.DownloadModel;
import com.relaxed.starter.download.enums.DownTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
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
public class OssDownloadHandler extends AbstractDownloadHandler {

	private final OssClient ossClient;

	@Override
	public boolean support(DownloadModel downloadModel, ResponseDownload responseDownload) {
		return DownTypeEnum.OSS.equals(responseDownload.channel());
	}

	@SneakyThrows
	@Override
	protected void write(DownloadModel downloadModel, HttpServletResponse response, ResponseDownload responseDownload) {
		String parentPath = downloadModel.getParentPath();
		String fileName = downloadModel.getFileName();
		try {
			byte[] download = ossClient.download(parentPath + "/" + fileName);
			ServletUtil.write(response, new ByteArrayInputStream(download));
		}
		catch (Exception e) {
			log.error("下载文件,渠道{},路径{}，名称{}异常", responseDownload.channel(), parentPath, fileName, e);
			throw e;
		}
	}

}
