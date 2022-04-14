package com.relaxed.common.core.util.http.part;

import cn.hutool.core.util.StrUtil;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;

public abstract class StreamPart implements Part {

	protected String fileName;

	protected String getContentType(File file) {
		String contentType;
		// 没有传入文件类型，同时根据文件获取不到类型，默认采用application/octet-stream
		contentType = new MimetypesFileTypeMap().getContentType(file);
		String filename = file.getName();
		// contentType非空采用filename匹配默认的图片类型
		if (StrUtil.isNotEmpty(contentType)) {
			if (filename.endsWith(".png")) {
				contentType = "image/png";
			}
			else if (filename.endsWith(".jpg") || filename.endsWith(".jpeg") || filename.endsWith(".jpe")) {
				contentType = "image/jpeg";
			}
			else if (filename.endsWith(".gif")) {
				contentType = "image/gif";
			}
			else if (filename.endsWith(".ico")) {
				contentType = "image/image/x-icon";
			}
		}
		return contentType;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
