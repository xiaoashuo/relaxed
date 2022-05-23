package com.relaxed.common.http.core.resource;

import lombok.Data;

import java.io.InputStream;

/**
 * @author Yakir
 * @Topic InputStreamResource
 * @Description
 * @date 2022/5/23 15:44
 * @Version 1.0
 */
@Data
public class InputStreamResource implements Resource {

	private String name;

	private String contentType;

	private InputStream fileData;

	public InputStreamResource(String name, InputStream fileData) {
		this(name, null, fileData);
	}

	public InputStreamResource(String name, String contentType, InputStream fileData) {
		this.name = name;
		this.contentType = contentType;
		this.fileData = fileData;
	}

	@Override
	public String getFileName() {
		return getName();
	}

	@Override
	public InputStream getStream() {
		return getFileData();
	}

}
