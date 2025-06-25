package com.relaxed.common.core.util.file;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

/**
 * @author Yakir
 * @Topic LocalFileHandler
 * @Description
 * @date 2023/5/17 11:43
 * @Version 1.0
 */
public class LocalFileHandler implements FileHandler {

	private static final LocalFileClient localClient = new LocalFileClient() {
	};

	@Override
	public String supportType() {
		return FileConstants.DEFAULT_HANDLE_TYPE;
	}

	@SneakyThrows
	@Override
	public String upload(String dirPath, String filename, String separator, MultipartFile file) {
		File desc = getAbsoluteFile(separator, dirPath, filename);
		file.transferTo(desc);
		String fileId = IdUtil.getSnowflakeNextIdStr();
		return fileId;
	}

	@Override
	public boolean delete(String rootPath, String relativePath, String separator) {
		PathInfo pathInfo = new PathInfo(rootPath, relativePath, separator);
		File file = new File(pathInfo.getFullPath());
		boolean result = FileUtil.del(file);
		if (result) {
			File parentFile = file.getParentFile();
			if (parentFile.listFiles() != null && parentFile.listFiles().length == 0) {
				FileUtil.del(parentFile);
			}
		}
		return result;
	}

	@SneakyThrows
	@Override
	public void writeToStream(String rootPath, String relativePath, OutputStream outputStream, String separator) {
		PathInfo pathInfo = new PathInfo(rootPath, relativePath, separator);
		File file = new File(pathInfo.getFullPath());
		try (FileInputStream inputStream = new FileInputStream(file); OutputStream tmp = outputStream) {
			IoUtil.copy(inputStream, tmp);
		}
		catch (Exception e) {
			throw e;
		}
	}

	@Override
	public byte[] downloadByte(String rootPath, String relativePath, String separator) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		writeToStream(rootPath, relativePath, outputStream, separator);
		return outputStream.toByteArray();
	}

	private File getAbsoluteFile(String separator, String dirPath, String fileName) {
		PathInfo pathInfo = new PathInfo(dirPath, fileName, separator);
		File file = new File(pathInfo.getFullPath());

		if (!file.exists()) {
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
		}
		return file;
	}

	@Override
	public boolean isExist(String rootPath, String relativePath, String separator) {
		PathInfo pathInfo = new PathInfo(rootPath, relativePath, separator);
		File desc = new File(pathInfo.getFullPath());
		return desc.exists();
	}

	@Override
	public Object getTargetObject() {
		return localClient;
	}

	public interface LocalFileClient {

	};

	@Getter
	public static class PathInfo {

		private String rootPath;

		private String relativePath;

		private String separator;

		private String fullPath;

		public PathInfo(String rootPath, String relativePath, String separator) {
			Assert.notBlank(relativePath, "relativePath不能为空");
			this.separator = separator;
			this.rootPath = rootPath;
			this.relativePath = relativePath;
			String targetPath;
			if (StrUtil.isBlank(this.rootPath)) {
				targetPath = this.relativePath;
			}
			else {
				targetPath = this.rootPath + separator + StrUtil.removePrefix(this.relativePath, separator);
			}
			this.fullPath = targetPath;
		}

		public String getFullPath() {
			return this.fullPath;
		}

	}

}
