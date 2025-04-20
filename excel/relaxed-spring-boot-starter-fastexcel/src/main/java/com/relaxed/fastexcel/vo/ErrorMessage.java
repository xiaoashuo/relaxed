package com.relaxed.fastexcel.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * Excel数据校验错误信息类 用于存储Excel数据校验过程中的错误信息 主要功能: 1. 记录错误行号 2. 记录错误信息 3. 支持多错误信息 4. 支持错误信息去重
 *
 * @author lengleng
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMessage {

	/**
	 * 错误行号 从1开始计数
	 */
	private Long lineNum;

	/**
	 * 错误信息集合 使用Set确保错误信息不重复
	 */
	private Set<String> errors = new HashSet<>();

	/**
	 * 构造方法 使用错误信息集合创建对象
	 * @param errors 错误信息集合
	 */
	public ErrorMessage(Set<String> errors) {
		this.errors = errors;
	}

	/**
	 * 构造方法 使用单个错误信息创建对象
	 * @param error 错误信息
	 */
	public ErrorMessage(String error) {
		HashSet<String> objects = new HashSet<>();
		objects.add(error);
		this.errors = objects;
	}

}
