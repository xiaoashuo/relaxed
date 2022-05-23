package com.relaxed.common.http.core.resource;

import lombok.Data;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

/**
 * @author Yakir
 * @Topic ByteResource
 * @Description
 * @date 2022/5/23 15:13
 * @Version 1.0
 */
@Data
public class ByteResource implements Resource {

	private String name;

	private String contentType;

	private byte[] fileData;

	public ByteResource(String name, byte[] fileData) {
		this(name, null, fileData);
	}

	public ByteResource(String name, String contentType, byte[] fileData) {
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
		return new ByteArrayInputStream(getFileData());
	}

}
