package com.relaxed.poi.domain;

import com.deepoove.poi.data.NumberingFormat;
import com.deepoove.poi.data.TextRenderData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 列表内容数据类，用于存储 Word 文档中的列表数据。 支持自定义列表样式和内容，支持链式调用。
 *
 * @author Yakir
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class ListContentData extends LabelData {

	/**
	 * 列表项数据集合，每个列表项可以包含文本和样式信息
	 */
	private List<TextRenderData> list;

	/**
	 * 列表样式格式，支持罗马字符、有序列表、无序列表等 默认为点状列表样式
	 */
	private NumberingFormat numberingFormat = NumberingFormat.BULLET;

}
