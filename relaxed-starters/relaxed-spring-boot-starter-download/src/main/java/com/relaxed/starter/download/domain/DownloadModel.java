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
	 * 文件父级路径
	 */
	private String parentPath;

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

	public DownloadModel(String parentPath, String fileName) {
		Assert.hasText(fileName, "filename must be exists");
		this.fileName = fileName;
		this.parentPath = parentPath;
		this.fileType = StrUtil.sub(fileName, fileName.lastIndexOf(StrPool.DOT) + 1, fileName.length());
	}

	public DownloadModel(String parentPath, String fileType, String fileName) {
		Assert.hasText(fileName, "filename must be exists");
		this.fileName = fileName;
		this.fileType = fileType;
		this.parentPath = parentPath;
		this.fileType = StrUtil.sub(fileName, fileName.lastIndexOf(StrPool.DOT) + 1, fileName.length());
	}

	/**
	 * 获取全文件路径 带名称
	 * @return
	 */
	public String getFullFilePath() {
		return this.parentPath + File.separator + fileName;
	}

}
