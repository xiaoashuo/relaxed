package com.relaxed.poi.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 图片类型枚举，定义了 Word 文档中支持的图片格式类型。
 *
 * @author Yakir
 * @since 1.0.0
 */
@Getter
@RequiredArgsConstructor
public enum PicTypeEnum {

	/**
	 * PNG图片格式，支持透明背景
	 */
	PNG(".png"),

	/**
	 * JPG图片格式，支持高压缩比
	 */
	JPG(".jpg"),

	/**
	 * JPEG图片格式，与JPG格式相同
	 */
	JPEG(".jpeg");

	/**
	 * 图片文件扩展名
	 */
	private final String picName;

}
