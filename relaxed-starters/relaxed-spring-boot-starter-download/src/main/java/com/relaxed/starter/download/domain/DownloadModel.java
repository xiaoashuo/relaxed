package com.relaxed.starter.download.domain;

import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import com.relaxed.starter.download.functions.DownloadCallback;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.util.Assert;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 下载模型类，用于封装文件下载的相关信息。 例如，如果文件路径为 /test/helloWorld.txt： - fileName: helloWorld.txt -
 * fileType: txt - parentPath: /test - fullFilePath: /test/helloWorld.txt
 *
 * @author Yakir
 * @since 1.0
 */
@Data
@Accessors(chain = true)
public class DownloadModel {

	/**
	 * 文件名（包含后缀），例如：123.png
	 */
	private String fileName;

	/**
	 * 展示文件名，用于在下载时显示给用户
	 */
	private String displayFileName;

	/**
	 * 文件类型，例如：png
	 */
	private String fileType;

	/**
	 * 文件父级路径，例如：/test
	 */
	private String parentPath;

	/**
	 * 完整的文件路径，例如：/test/123.png
	 */
	private String fullFilePath;

	/**
	 * 路径分隔符，默认为系统分隔符
	 */
	private String separator = File.separator;

	/**
	 * 额外参数，用于存储下载过程中需要的其他信息
	 */
	private Map<String, Object> extra = new HashMap<>();

	/**
	 * 下载回调函数，用于在下载完成后执行一些收尾工作
	 */
	private DownloadCallback downloadCallback;

	/**
	 * 默认构造函数
	 */
	public DownloadModel() {
	}

	/**
	 * 使用父路径、文件名和文件类型构造下载模型
	 * @param parentPath 父级路径
	 * @param fileName 文件名
	 * @param fileType 文件类型
	 */
	public DownloadModel(String parentPath, String fileName, String fileType) {
		this(parentPath, fileName, fileType, File.separator);
	}

	/**
	 * 使用父路径、文件名、文件类型和分隔符构造下载模型
	 * @param parentPath 父级路径
	 * @param fileName 文件名
	 * @param fileType 文件类型
	 * @param separator 路径分隔符
	 * @throws IllegalArgumentException 如果文件名为空
	 */
	public DownloadModel(String parentPath, String fileName, String fileType, String separator) {
		Assert.hasText(fileName, "filename must be exists");
		this.fileName = fileName;
		this.fileType = fileType;
		this.parentPath = parentPath;
		this.separator = separator;
		this.fullFilePath = parentPath + this.separator + fileName;
	}

	/**
	 * 使用完整文件路径构造下载模型
	 * @param fullFilePath 完整的文件路径
	 */
	public DownloadModel(String fullFilePath) {
		this(fullFilePath, File.separator);
	}

	/**
	 * 使用完整文件路径和分隔符构造下载模型
	 * @param fullFilePath 完整的文件路径
	 * @param separator 路径分隔符
	 * @throws IllegalArgumentException 如果完整文件路径为空
	 */
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
