package com.relaxed.common.core.util.http.part;

import lombok.SneakyThrows;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class FilePart extends com.relaxed.common.core.util.http.part.StreamPart {

	private String contentType;

	private File file;

	public FilePart(File file) {
		this(file.getName(), file);

	}

	public FilePart(String fileName, File file) {
		super.fileName = fileName;
		this.contentType = getContentType(file);
		this.file = file;

	}

	public FilePart(String fileName, String contentType, File file) {
		super.fileName = fileName;
		this.contentType = contentType;
		this.file = file;
	}

	@Override
	public String getContentType() {
		return contentType;
	}

	@SneakyThrows
	@Override
	public InputStream getStream() {
		return new FileInputStream(file);
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
