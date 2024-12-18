package com.relaxed.poi.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Yakir
 * @Topic PicTypeEnum
 * @Description
 * @date 2024/3/25 13:56
 * @Version 1.0
 */
@Getter
@RequiredArgsConstructor
public enum PicTypeEnum {

	/**
	 * png图片
	 */
	PNG(".png"),
	/**
	 * JPG图片
	 */
	JPG(".jpg"),
	/**
	 * jpeg
	 */
	JPEG(".jpeg");

	;

	private final String picName;

}
