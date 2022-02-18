package com.relaxed.starter.download.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DownloadModel {

	/**
	 * 文件名称 不带后缀
	 */
	private String fileName;

	/**
	 * 文件后缀 .txt
	 */
	private String fileSuffix;

	/**
	 * 文件父级路径
	 */
	private String parentPath;

}
