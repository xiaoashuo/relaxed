package com.relaxed.test.util.file;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Yakir
 * @Topic FileMeta
 * @Description
 * @date 2022/11/27 15:48
 * @Version 1.0
 */
@Data
@Accessors(chain = true)
public class FileMeta {

	/**
	 * 原始文件名称
	 */
	private String originalFilename;

	/**
	 * 存储文件名称
	 */
	private String filename;

	/**
	 * 文件唯一标识 code
	 */
	private String fileId;

	/**
	 * 基础文件路径
	 */
	private String basePath;

	/**
	 * 文件路径
	 */
	private String relativePath;

	/**
	 * 分隔符
	 */
	private String seperator;

	/**
	 * 获取全文件路径
	 * @return /upload/q.txt
	 */
	public String getRelativeFilePath() {
		String noRootPath = relativePath + seperator + filename;
		return StrUtil.addPrefixIfNot(noRootPath, seperator);
	}

	/**
	 * 获取本地文件全路径
	 * @return
	 */
	public String getFullFilePath() {
		String fullPath = basePath + getRelativeFilePath();
		return fullPath;
	}

}
