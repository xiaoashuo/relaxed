package com.relaxed.common.http.core.resource;

import cn.hutool.core.io.FileUtil;
import lombok.Data;

import java.io.File;
import java.io.InputStream;

/**
 * @author Yakir
 * @Topic FileResource
 * @Description
 * @date 2022/5/23 15:09
 * @Version 1.0
 */
@Data
public class FileResource implements Resource {

	private String name;

	private String fileName;

	private String contentType;

	private File file;

	public FileResource(String name, File file) {
		this(name, file.getName(), null, file);
	}

	public FileResource(String name, String contentType, File file) {
		this(name, file.getName(), contentType, file);
	}

	public FileResource(String name, String fileName, String contentType, File file) {
		this.name = name;
		this.fileName = fileName;
		this.contentType = contentType;
		this.file = file;
	}

	@Override
	public InputStream getStream() {
		return FileUtil.getInputStream(getFile());
	}

}
