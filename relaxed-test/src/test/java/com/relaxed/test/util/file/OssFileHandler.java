package com.relaxed.test.util.file;

import com.relaxed.common.core.util.file.FileHandler;
import org.springframework.web.multipart.MultipartFile;

import java.io.OutputStream;

/**
 * @author Yakir
 * @Topic OssFileHandler
 * @Description
 * @date 2023/5/17 14:10
 * @Version 1.0
 */
public class OssFileHandler implements FileHandler {

	@Override
	public String supportType() {
		return "oss";
	}

	@Override
	public String upload(String dirPath, String filename, String separator, MultipartFile file) {
		return null;
	}

	@Override
	public boolean isExist(String rootPath, String relativePath, String separator) {
		return false;
	}

	@Override
	public boolean delete(String rootPath, String relativePath, String separator) {
		return false;
	}

	@Override
	public void writeToStream(String rootPath, String relativePath, OutputStream outputStream, String separator) {

	}

	@Override
	public byte[] downloadByte(String rootPath, String relativePath, String separator) {
		return new byte[0];
	}

	@Override
	public Object getTargetObject() {
		return null;
	}

}
