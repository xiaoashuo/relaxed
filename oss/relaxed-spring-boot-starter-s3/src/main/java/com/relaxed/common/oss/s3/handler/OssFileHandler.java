package com.relaxed.common.oss.s3.handler;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.relaxed.common.core.util.file.FileHandler;
import com.relaxed.common.oss.s3.OssClient;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
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
	public boolean delete(String rootPath, String relativePath) {
		ossClient.delete(rootPath + relativePath);
		return true;
	}

	@Override
	public void writeToStream(String rootPath, String relativePath, OutputStream outputStream) {
		byte[] content = ossClient.download(rootPath + relativePath);
		IoUtil.write(outputStream, true, content);
	}

	@Override
	public byte[] downloadByte(String rootPath, String relativePath) {
		return ossClient.download(rootPath + relativePath);
	}

	@Override
	public boolean isExist(String rootPath, String relativePath) {
		return ossClient.isExist(rootPath + relativePath);
	}

	@Override
	public Object getTargetObject() {
		return ossClient;
	}

}
