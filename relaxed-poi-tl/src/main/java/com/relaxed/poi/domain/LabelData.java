package com.relaxed.poi.domain;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 标签数据基类，用于存储 Word 文档中的标签数据。 包含标签名称和内容类型信息，支持链式调用。
 *
 * @author Yakir
 * @since 1.0.0
 */
@Accessors(chain = true)
@Data
public class LabelData {

	/**
	 * 标签名称，用于标识 Word 文档中的标签位置
	 */
	private String labelName;

	/**
	 * 文件内容类型，用于指定标签内容的类型
	 */
	private String contentType;

}
