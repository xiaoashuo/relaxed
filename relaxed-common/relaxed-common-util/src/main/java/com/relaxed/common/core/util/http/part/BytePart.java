package com.relaxed.common.core.util.http.part;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class BytePart extends com.relaxed.common.core.util.http.part.StreamPart {

	private final byte[] content;

	public BytePart(byte[] content, String fileName) {
		super.fileName = fileName;
		this.content = content;
	}

	@Override
	public String getContentType() {
		return null;
	}

	@Override
	public InputStream getStream() {
		return new ByteArrayInputStream(content);
	}

}
