package com.relaxed.test.excel.domain;

import cn.idev.excel.annotation.ExcelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Yakir
 * @Topic ExcelModel
 * @Description
 * @date 2022/1/19 16:54
 * @Version 1.0
 */
@Data
public class ExcelModel {

	/**
	 * id
	 */
	@ExcelProperty(value = "标识id")
	private Integer id;

	/**
	 * 用户名
	 */
	@ExcelProperty(value = "用户名")
	private String username;

	/**
	 * 年龄
	 */
	@ExcelProperty(value = "年龄")
	private Integer age;

	/**
	 * 性别
	 */
	@ExcelProperty(value = "创建时间")
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@ExcelProperty(value = "更新时间")
	private LocalDateTime updateTime;

}
