package com.relaxed.poi.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.File;

/**
 * 图片内容数据类，用于存储 Word 文档中的图片数据。 支持网络图片和本地图片，支持自定义图片尺寸，支持链式调用。
 *
 * @author Yakir
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class PicContentData extends LabelData {

	/**
	 * 图片宽度，单位为像素
	 */
	private Integer width;

	/**
	 * 图片高度，单位为像素
	 */
	private Integer height;

	/**
	 * 网络图片的URL地址，用于从网络加载图片
	 */
	private String picUrl;

	/**
	 * 本地图片文件，用于从本地文件系统加载图片
	 */
	private File localFile;

}
