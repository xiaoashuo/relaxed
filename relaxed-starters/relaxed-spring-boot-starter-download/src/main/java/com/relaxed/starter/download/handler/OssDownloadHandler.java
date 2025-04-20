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
 * OSS对象存储下载处理器，用于处理阿里云OSS等对象存储服务的文件下载。 继承自
 * {@link AbstractDownloadHandler}，实现了从OSS下载文件并写入HTTP响应的功能。
 *
 * @author Yakir
 * @since 1.0
 */
@Slf4j
@RequiredArgsConstructor
public class OssDownloadHandler extends AbstractDownloadHandler {

	private final OssClient ossClient;

	/**
	 * 判断是否支持OSS文件下载
	 * @param downloadModel 下载模型
	 * @param responseDownload 下载注解配置
	 * @return true 如果下载渠道是OSS，false 否则
	 */
	@Override
	public boolean support(DownloadModel downloadModel, ResponseDownload responseDownload) {
		return DownTypeEnum.OSS.equals(responseDownload.channel());
	}

	/**
	 * 将OSS文件写入HTTP响应 从OSS下载文件内容并写入响应流
	 * @param downloadModel 下载模型
	 * @param response HTTP响应对象
	 * @param responseDownload 下载注解配置
	 */
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
