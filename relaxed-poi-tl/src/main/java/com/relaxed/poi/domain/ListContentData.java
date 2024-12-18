package com.relaxed.poi.domain;

import com.deepoove.poi.data.NumberingFormat;
import com.deepoove.poi.data.TextRenderData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author Yakir
 * @Topic ListContentData
 * @Description
 * @date 2024/3/25 17:53
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class ListContentData extends LabelData {

	/**
	 * 列表数据集
	 */
	private List<TextRenderData> list;

	/**
	 * 列表样式,支持罗马字符、有序无序等,默认为点
	 */
	private NumberingFormat numberingFormat = NumberingFormat.BULLET;

}
