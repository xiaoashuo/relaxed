package com.relaxed.common.core.util.file;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
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
	public boolean delete(String rootPath, String relativePath) {
		File file = FileUtil.file(rootPath + relativePath);
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
	public void writeToStream(String rootPath, String relativePath, OutputStream outputStream) {
		File file = new File(rootPath, relativePath);
		try (FileInputStream inputStream = new FileInputStream(file); OutputStream tmp = outputStream) {
			IoUtil.copy(inputStream, tmp);
		}
		catch (Exception e) {
			throw e;
		}
	}

	@Override
	public byte[] downloadByte(String rootPath, String relativePath) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		writeToStream(rootPath, relativePath, outputStream);
		return outputStream.toByteArray();
	}

	private File getAbsoluteFile(String separator, String dirPath, String fileName) {
		File desc = new File(dirPath + separator + fileName);

		if (!desc.exists()) {
			if (!desc.getParentFile().exists()) {
				desc.getParentFile().mkdirs();
			}
		}
		return desc;
	}

	@Override
	public boolean isExist(String rootPath, String relativePath) {
		File desc = new File(rootPath, relativePath);
		return desc.exists();
	}

	@Override
	public Object getTargetObject() {
		return localClient;
	}

	public interface LocalFileClient {

	};

}
