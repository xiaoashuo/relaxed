package com.relaxed.poi.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.File;

/**
 * @author Yakir
 * @Topic PicContentData
 * @Description
 * @date 2024/3/25 15:43
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class PicContentData extends LabelData {

	/**
	 * 图片宽度
	 */
	private Integer width;

	/**
	 * 图片高度
	 */
	private Integer height;

	/**
	 * 图片地址（网络图片插入时使用）
	 */
	private String picUrl;

	/**
	 * 本地图片
	 */
	private File localFile;

}
