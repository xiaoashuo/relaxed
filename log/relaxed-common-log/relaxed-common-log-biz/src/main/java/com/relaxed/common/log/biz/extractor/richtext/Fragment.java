package com.relaxed.common.log.biz.extractor.richtext;

import java.util.List;

/**
 * 富文本差异片段类，用于表示富文本比较中的一个片段。 每个片段包含行号和该行的内容部分列表，用于记录富文本的变更情况。
 *
 * @author Yakir
 * @since 1.0.0
 */
class Fragment {

	/**
	 * 行号，表示该片段在原始文本中的行位置
	 */
	private Integer lineNumber;

	/**
	 * 内容部分列表，包含该行的所有变更部分
	 */
	private List<Part> partList;

	/**
	 * 构造方法，创建一个指定行号的片段
	 * @param lineNumber 行号
	 */
	Fragment(Integer lineNumber) {
		this.lineNumber = lineNumber;
	}

	/**
	 * 获取行号
	 * @return 行号
	 */
	Integer getLineNumber() {
		return lineNumber;
	}

	/**
	 * 设置行号
	 * @param lineNumber 行号
	 */
	void setLineNumber(Integer lineNumber) {
		this.lineNumber = lineNumber;
	}

	/**
	 * 获取内容部分列表
	 * @return 内容部分列表
	 */
	List<Part> getPartList() {
		return partList;
	}

	/**
	 * 设置内容部分列表
	 * @param partList 内容部分列表
	 */
	void setPartList(List<Part> partList) {
		this.partList = partList;
	}

}
