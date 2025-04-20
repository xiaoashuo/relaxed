package com.relaxed.common.log.biz.service;

import com.relaxed.common.log.biz.model.AttributeModel;
import com.relaxed.common.log.biz.model.DiffMeta;

import java.util.List;

/**
 * 数据处理器接口，用于处理对象之间的差异比较 该接口定义了对象差异比较的基本规范，实现类需要提供具体的比较逻辑 支持比较两个对象之间的属性差异，并生成差异列表
 *
 * @author Yakir
 */
public interface IDataHandler {

	/**
	 * 比较两个对象的差异 该方法用于比较源对象和目标对象之间的属性差异 返回的属性变更列表包含了所有发生变化的属性信息
	 * @param diffMeta 差异元数据，包含源对象、目标对象和差异标识
	 * @return 属性变更列表，每个元素描述一个属性的变化情况
	 */
	List<AttributeModel> diffObject(DiffMeta diffMeta);

}
