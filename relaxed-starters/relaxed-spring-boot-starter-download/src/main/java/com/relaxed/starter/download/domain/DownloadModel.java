package com.relaxed.starter.download.domain;

import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import com.relaxed.starter.download.functions.DownloadCallback;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.util.Assert;

import java.io.File;
import java.util.Map;

/**
 * @author Yakir
 * @Topic DownloadModel
 * @Description <pre>
 * 若文件地址 为  /test/helloWorld.txt
 *  download model
 *    fileName helloWorld
 *    fileSuffix .txt
 *    fullPath /test
 * </pre>
 * @date 2022/2/18 15:07
 * @Version 1.0
 */
@Data
@Accessors(chain = true)
public class DownloadModel {

	/**
	 * 文件名称 带后缀 123.png
	 */
	private String fileName;

	/**
	 * 文件类型 png
	 */
	private String fileType;

	/**
	 * 文件父级路径 /test
	 */
	private String parentPath;

	/**
	 * 全文件路径 /test/123.png
	 */
	private String fullFilePath;

	/**
	 * 路径分隔符 默认为 系统分隔符
	 */
	private String separator = File.separator;

	/**
	 * 额外参数
	 */
	private Map<String, Object> extra;

	/**
	 * 后置处理器 执行完之后可以做一些收尾工作
	 */
	private DownloadCallback downloadCallback;

	public DownloadModel() {
	}

	public DownloadModel(String parentPath, String fileName, String fileType) {
		this(parentPath, fileName, fileType, File.separator);
	}

	public DownloadModel(String parentPath, String fileName, String fileType, String separator) {
		Assert.hasText(fileName, "filename must be exists");
		this.fileName = fileName;
		this.fileType = fileType;
		this.parentPath = parentPath;
		this.separator = separator;
		this.fullFilePath = parentPath + this.separator + fileName;
	}

	public DownloadModel(String fullFilePath) {
		this(fullFilePath, File.separator);
	}

	public DownloadModel(String fullFilePath, String separator) {
		Assert.hasText(fullFilePath, "fullFilePath must be exists");
		this.separator = separator;
		int lastSeparator = fullFilePath.lastIndexOf(separator);
		this.fileName = StrUtil.sub(fullFilePath, lastSeparator + 1, fullFilePath.length());
		this.parentPath = StrUtil.sub(fullFilePath, 0, lastSeparator);
		this.fullFilePath = fullFilePath;
		this.fileType = StrUtil.sub(fileName, fileName.lastIndexOf(StrPool.DOT) + 1, fileName.length());
	}

}
