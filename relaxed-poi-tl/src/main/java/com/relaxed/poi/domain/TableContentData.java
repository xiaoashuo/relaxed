package com.relaxed.poi.domain;

import com.deepoove.poi.data.RowRenderData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author Yakir
 * @Topic TextContentData
 * @Description
 * @date 2024/3/25 13:55
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class TableContentData extends LabelData {

	/**
	 * 表头
	 */
	private RowRenderData header;

	/**
	 * 表内容
	 */
	private List<RowRenderData> contents;

}
