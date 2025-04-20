package com.relaxed.poi.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 元素元数据类，用于存储 Word 文档中元素的元信息。 包含元素的索引、标签名称和源文本信息。
 *
 * @author Yakir
 * @since 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ElementMeta {

	/**
	 * 元素在文档中的索引位置
	 */
	private Integer index;

	/**
	 * 元素的标签名称，不包含前缀和后缀
	 */
	private String tagName;

	/**
	 * 元素的完整源文本，包含前缀和后缀
	 */
	private String source;

}
