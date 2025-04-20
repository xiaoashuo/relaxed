package com.relaxed.starter.download.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 下载类型枚举，定义了支持的文件下载渠道类型。 包括本地文件系统、SFTP服务器、OSS对象存储和自定义下载渠道。
 *
 * @author Yakir
 * @since 1.0
 */
@RequiredArgsConstructor
@Getter
public enum DownTypeEnum {

	/**
	 * 本地文件系统下载
	 */
	LOCAL,

	/**
	 * SFTP服务器下载
	 */
	SFTP,

	/**
	 * OSS对象存储下载
	 */
	OSS,

	/**
	 * 自定义下载渠道
	 */
	OTHER;

}
