package com.relaxed.poi.domain;

import com.deepoove.poi.data.RowRenderData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 表格内容数据类，用于存储 Word 文档中的表格数据。 包含表头和表内容数据，支持链式调用。
 *
 * @author Yakir
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class TableContentData extends LabelData {

	/**
	 * 表格的表头数据
	 */
	private RowRenderData header;

	/**
	 * 表格的内容数据列表
	 */
	private List<RowRenderData> contents;

}
