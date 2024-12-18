package com.relaxed.poi.domain;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Yakir
 * @Topic LabelData
 * @Description
 * @date 2024/3/25 13:54
 * @Version 1.0
 */
@Accessors(chain = true)
@Data
public class LabelData {

	/**
	 * 标签名称
	 */
	private String labelName;

	/**
	 * 文件内容类型
	 */
	private String contentType;

}
