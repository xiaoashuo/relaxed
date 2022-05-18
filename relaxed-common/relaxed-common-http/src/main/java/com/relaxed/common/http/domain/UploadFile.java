package com.relaxed.common.http.domain;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.crypto.digest.MD5;
import lombok.Data;

import java.io.File;
import java.io.InputStream;

/**
 * @author Yakir
 * @Topic UploadFile
 * @Description
 * @date 2022/5/18 8:52
 * @Version 1.0
 */
@Data
public class UploadFile {

	/**
	 * @param name 表单名称，不能重复
	 * @param file 文件
	 */
	public UploadFile(String name, File file) {
		this(name, file.getName(), FileUtil.readBytes(file));
	}

	/**
	 * @param name 表单名称，不能重复
	 * @param fileName 文件名
	 * @param input 文件流
	 */
	public UploadFile(String name, String fileName, InputStream input) {
		this(name, fileName, IoUtil.readBytes(input));
	}

	/**
	 * @param name 表单名称，不能重复
	 * @param fileName 文件名
	 * @param fileData 文件数据
	 */
	public UploadFile(String name, String fileName, byte[] fileData) {
		super();
		this.name = name;
		this.fileName = fileName;
		this.fileData = fileData;
		this.md5 = new MD5().digestHex(fileData);
	}

	private String name;

	private String fileName;

	private byte[] fileData;

	private String md5;

}
