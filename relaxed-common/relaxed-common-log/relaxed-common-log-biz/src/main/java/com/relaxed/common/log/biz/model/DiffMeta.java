package com.relaxed.common.log.biz.model;

import lombok.Getter;

import java.util.List;

/**
 * @author Yakir
 * @Topic DiffMeta
 * @Description
 * @date 2023/12/21 10:41
 * @Version 1.0
 */
@Getter
public class DiffMeta {

	/**
	 * 差异化key 标识比对对象 标识
	 */
	private String diffKey;

	/**
	 * 源对象
	 */
	private Object source;

	/**
	 * 目标对象
	 */
	private Object target;

	/**
	 * 差异列表
	 */
	private List<AttributeModel> diffList;

	public DiffMeta(String diffKey, Object source, Object target) {
		this.diffKey = diffKey;
		this.source = source;
		this.target = target;
	}

}
