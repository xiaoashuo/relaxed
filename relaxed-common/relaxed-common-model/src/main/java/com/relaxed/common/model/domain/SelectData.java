package com.relaxed.common.model.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 下拉框选择数据封装类
 * <p>
 * 用于封装下拉框组件的数据项，包含显示文本、值、选中状态等属性。 支持分组、禁用状态和扩展数据等功能。
 * </p>
 *
 * @param <T> 扩展数据的类型
 * @author Yakir
 * @since 1.0.0
 */
@Data
@Schema(title = "下拉框数据")
public class SelectData<T> {

	/**
	 * 显示文本
	 * <p>
	 * 在下拉框中显示的文本内容。
	 * </p>
	 */
	@Schema(title = "显示文本", description = "在下拉框中显示的文本内容", required = true)
	private String label;

	/**
	 * 选项值
	 * <p>
	 * 选中该项时获取的值。
	 * </p>
	 */
	@Schema(title = "选项值", description = "选中该项时获取的值", required = true)
	private Object value;

	/**
	 * 选中状态
	 * <p>
	 * 标识该项是否被选中。
	 * </p>
	 */
	@Schema(title = "选中状态", description = "标识该项是否被选中")
	private Boolean selected;

	/**
	 * 禁用状态
	 * <p>
	 * 标识该项是否被禁用。
	 * </p>
	 */
	@Schema(title = "禁用状态", description = "标识该项是否被禁用")
	private Boolean disabled;

	/**
	 * 分组标识
	 * <p>
	 * 用于对下拉选项进行分组。
	 * </p>
	 */
	@Schema(title = "分组标识", description = "用于对下拉选项进行分组")
	private String type;

	/**
	 * 扩展数据
	 * <p>
	 * 可以附加任意类型的扩展数据。
	 * </p>
	 */
	@Schema(title = "扩展数据", description = "可以附加任意类型的扩展数据")
	private T extendObj;

}
