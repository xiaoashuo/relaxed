package com.relaxed.starter.download.handler.ext;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import com.relaxed.common.core.util.file.FileHandler;
import com.relaxed.common.jsch.sftp.client.ISftpClient;
import com.relaxed.starter.download.enums.DownTypeEnum;
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

	private final ISftpClient sftpClient;

	@Override
	public String supportType() {
		return DownTypeEnum.SFTP;
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
	public boolean delete(String rootPath, String relativePath) {
		return sftpClient.supplyOpen(sftp -> {
			sftp.delete(rootPath, relativePath);
			return true;
		});
	}

	@Override
	public void writeToStream(String rootPath, String relativePath, OutputStream outputStream) {
		byte[] content = sftpClient.supplyOpen(sftp -> sftp.download(rootPath, relativePath));
		IoUtil.write(outputStream, true, content);
	}

	@Override
	public byte[] downloadByte(String rootPath, String relativePath) {
		return sftpClient.supplyOpen(sftp -> sftp.download(rootPath, relativePath));
	}

}
