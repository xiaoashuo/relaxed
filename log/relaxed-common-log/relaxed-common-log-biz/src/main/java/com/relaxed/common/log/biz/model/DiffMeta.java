package com.relaxed.common.log.biz.model;

import lombok.Getter;

import java.util.List;

/**
 * 差异元数据类，用于存储对象之间的差异比较信息 该类封装了源对象和目标对象的比较结果，以及差异列表
 *
 * @author Yakir
 */
@Getter
public class DiffMeta {

	/**
	 * 差异标识键，用于唯一标识比较的对象 通常用于在日志中追踪特定的差异比较
	 */
	private String diffKey;

	/**
	 * 源对象，表示比较的原始对象 通常为修改前的对象状态
	 */
	private Object source;

	/**
	 * 目标对象，表示比较的目标对象 通常为修改后的对象状态
	 */
	private Object target;

	/**
	 * 差异列表，存储源对象和目标对象之间的具体差异 每个差异项包含属性名称、旧值和新值等信息
	 */
	private List<AttributeModel> diffList;

	/**
	 * 构造函数，初始化差异元数据的基本信息
	 * @param diffKey 差异标识键
	 * @param source 源对象
	 * @param target 目标对象
	 */
	public DiffMeta(String diffKey, Object source, Object target) {
		this.diffKey = diffKey;
		this.source = source;
		this.target = target;
	}

}
