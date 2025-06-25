package com.relaxed.common.jsch.sftp.handler;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.relaxed.common.core.util.file.FileHandler;
import com.relaxed.common.jsch.sftp.client.ISftpClient;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * SftpFileHandler
 *
 * @author Yakir
 */
@RequiredArgsConstructor
@Slf4j
public class SftpFileHandler implements FileHandler {

	private final String supportType;

	private final ISftpClient sftpClient;

	@Override
	public String supportType() {
		return supportType;
	}

	@SneakyThrows
	@Override
	public String upload(String dirPath, String filename, String separator, MultipartFile file) {
		try (InputStream inputStream = file.getInputStream();) {
			sftpClient.open(sftp -> sftp.upload(dirPath, filename, inputStream));
		}
		String fileId = IdUtil.getSnowflakeNextIdStr();
		return fileId;
	}

	@Override
	public boolean delete(String rootPath, String relativePath, String separator) {
		PathInfo pathInfo = new PathInfo(rootPath, relativePath, separator);
		return sftpClient.supplyOpen(sftp -> {
			sftp.delete(pathInfo.getRootPath(), pathInfo.getRelativePath());
			return true;
		});
	}

	@Override
	public void writeToStream(String rootPath, String relativePath, OutputStream outputStream, String separator) {
		PathInfo pathInfo = new PathInfo(rootPath, relativePath, separator);
		byte[] content = sftpClient
				.supplyOpen(sftp -> sftp.download(pathInfo.getRootPath(), pathInfo.getRelativePath()));
		IoUtil.write(outputStream, true, content);
	}

	@Override
	public byte[] downloadByte(String rootPath, String relativePath, String separator) {
		PathInfo pathInfo = new PathInfo(rootPath, relativePath, separator);
		return sftpClient.supplyOpen(sftp -> sftp.download(pathInfo.getRootPath(), pathInfo.getRelativePath()));

	}

	@Override
	public boolean isExist(String rootPath, String relativePath, String separator) {
		PathInfo pathInfo = new PathInfo(rootPath, relativePath, separator);
		return sftpClient.supplyOpen(sftp -> sftp.isExist(pathInfo.getFullPath()));

	}

	@Override
	public Object getTargetObject() {
		return sftpClient;
	}

	@Getter
	class PathInfo {

		private String rootPath;

		private String relativePath;

		private String separator;

		private String fullPath;

		public PathInfo(String rootPath, String relativePath, String separator) {
			Assert.notBlank(relativePath, "relativePath不能为空");
			this.separator = separator;
			this.rootPath = StrUtil.isBlank(rootPath) ? StrPool.DOT : StrUtil.removeSuffix(rootPath, separator);
			this.relativePath = StrUtil.removePrefix(relativePath, separator);
			this.fullPath = this.rootPath + separator + this.relativePath;
		}

		public String getFullPath() {
			return this.fullPath;
		}

	}

}
