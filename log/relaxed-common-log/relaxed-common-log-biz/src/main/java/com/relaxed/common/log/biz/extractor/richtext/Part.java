package com.relaxed.common.log.biz.extractor.richtext;

/**
 * 富文本内容部分类，用于表示富文本中的一个内容片段。 每个部分包含类型和内容，用于记录富文本的变更情况。
 *
 * @author Yakir
 * @since 1.0.0
 */
class Part {

	/**
	 * 内容部分类型，表示该部分的变更类型
	 */
	private PartType partType;

	/**
	 * 内容部分的具体内容
	 */
	private String partContent;

	/**
	 * 构造方法，创建一个指定类型的内容部分
	 * @param partType 内容部分类型
	 */
	Part(PartType partType) {
		this.partType = partType;
	}

	/**
	 * 构造方法，创建一个指定类型和内容的内容部分
	 * @param partType 内容部分类型
	 * @param partContent 内容部分的具体内容
	 */
	Part(PartType partType, String partContent) {
		this.partType = partType;
		this.partContent = partContent;
	}

	/**
	 * 获取内容部分类型
	 * @return 内容部分类型
	 */
	PartType getPartType() {
		return partType;
	}

	/**
	 * 设置内容部分类型
	 * @param partType 内容部分类型
	 */
	void setPartType(PartType partType) {
		this.partType = partType;
	}

	/**
	 * 获取内容部分的具体内容
	 * @return 内容部分的具体内容
	 */
	String getPartContent() {
		return partContent;
	}

	/**
	 * 设置内容部分的具体内容
	 * @param partContent 内容部分的具体内容
	 */
	void setPartContent(String partContent) {
		this.partContent = partContent;
	}

}
