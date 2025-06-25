package com.relaxed.common.oss.s3.handler;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.relaxed.common.core.util.file.FileHandler;
import com.relaxed.common.oss.s3.OssClient;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * OssFileHandler
 *
 * @author Yakir
 */
@Slf4j
@RequiredArgsConstructor
public class OssFileHandler implements FileHandler {

	private final String supportType;

	private final OssClient ossClient;

	@Override
	public String supportType() {
		return supportType;
	}

	@SneakyThrows
	@Override
	public String upload(String dirPath, String filename, String separator, MultipartFile file) {
		try (InputStream inputStream = file.getInputStream();) {
			ossClient.upload(inputStream, file.getSize(), dirPath + separator + filename);
		}
		String fileId = IdUtil.getSnowflakeNextIdStr();
		return fileId;
	}

	@Override
	public boolean delete(String rootPath, String relativePath, String separator) {
		PathInfo pathInfo = new PathInfo(rootPath, relativePath, separator);
		ossClient.delete(pathInfo.getFullPath());
		return true;
	}

	@Override
	public void writeToStream(String rootPath, String relativePath, OutputStream outputStream, String separator) {
		PathInfo pathInfo = new PathInfo(rootPath, relativePath, separator);
		byte[] content = ossClient.download(pathInfo.getFullPath());
		IoUtil.write(outputStream, true, content);
	}

	@Override
	public byte[] downloadByte(String rootPath, String relativePath, String separator) {
		PathInfo pathInfo = new PathInfo(rootPath, relativePath, separator);
		return ossClient.download(pathInfo.getFullPath());
	}

	@Override
	public boolean isExist(String rootPath, String relativePath, String separator) {
		PathInfo pathInfo = new PathInfo(rootPath, relativePath, separator);
		return ossClient.isExist(pathInfo.getFullPath());
	}

	@Getter
	public static class PathInfo {

		private String rootPath;

		private String relativePath;

		private String separator;

		private String fullPath;

		public PathInfo(String rootPath, String relativePath, String separator) {
			Assert.notBlank(relativePath, "relativePath不能为空");
			this.separator = separator;
			this.rootPath = StrUtil.removeSuffix(rootPath, separator);
			this.relativePath = StrUtil.removePrefix(relativePath, separator);
			String targetPath;
			if (StrUtil.isBlank(this.rootPath)) {
				targetPath = this.relativePath;
			}
			else {
				targetPath = this.rootPath + separator + this.relativePath;
			}
			this.fullPath = targetPath;
		}

		public String getFullPath() {
			return this.fullPath;
		}

	}

	@Override
	public Object getTargetObject() {
		return ossClient;
	}


}
