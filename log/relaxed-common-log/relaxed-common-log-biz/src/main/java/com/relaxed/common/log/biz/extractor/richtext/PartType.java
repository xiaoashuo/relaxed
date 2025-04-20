package com.relaxed.common.log.biz.extractor.richtext;

/**
 * 富文本差异部分类型枚举，用于标识富文本内容中的不同变更类型。 该枚举定义了富文本比较中可能出现的四种变更类型： 1. 新增内容（ADD） 2. 删除内容（DEL） 3.
 * 变更后的新内容（CHANGE_NEW） 4. 变更前的旧内容（CHANGE_OLD）
 *
 * @author Yakir
 * @since 1.0.0
 */
enum PartType {

	/**
	 * 新增的内容部分
	 */
	ADD,

	/**
	 * 删除的内容部分
	 */
	DEL,

	/**
	 * 变更后的新内容部分
	 */
	CHANGE_NEW,

	/**
	 * 变更前的旧内容部分
	 */
	CHANGE_OLD

}
