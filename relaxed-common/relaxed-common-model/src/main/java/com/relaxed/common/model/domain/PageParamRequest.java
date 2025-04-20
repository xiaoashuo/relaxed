package com.relaxed.common.model.domain;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springdoc.api.annotations.ParameterObject;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * 分页查询请求参数封装类
 * <p>
 * 用于接收前端分页查询请求的参数，支持多种排序方式。 该类会被转换为 {@link PageParam} 对象进行后续处理。
 * </p>
 *
 * @see PageParam
 * @author hccake
 * @since 1.0.0
 */
@Data
@Valid
@ParameterObject
@Schema(title = "分页查询入参")
public class PageParamRequest {

	/**
	 * 排序字段正则表达式
	 * <p>
	 * 用于验证排序字段的格式，支持表名.字段名的形式。
	 * </p>
	 */
	public static final String SORT_FILED_REGEX = "(([A-Za-z0-9_]{1,10}.)?[A-Za-z0-9_]{1,64})";

	/**
	 * 排序方式正则表达式
	 * <p>
	 * 用于验证排序方式，支持 asc 和 desc。
	 * </p>
	 */
	public static final String SORT_FILED_ORDER = "(desc|asc)";

	/**
	 * 完整排序规则正则表达式
	 * <p>
	 * 用于验证完整的排序规则格式。
	 * </p>
	 */
	public static final String SORT_REGEX = "^" + SORT_FILED_REGEX + "(," + SORT_FILED_ORDER + ")*$";

	/**
	 * 当前页码
	 * <p>
	 * 从1开始计数，表示要查询的页码。
	 * </p>
	 */
	@Parameter(description = "当前页码, 从 1 开始", schema = @Schema(minimum = "1", defaultValue = "1", example = "1"))
	@Min(value = 1, message = "当前页不能小于 1")
	private long current = 1;

	/**
	 * 每页显示条数
	 * <p>
	 * 表示每页要显示的数据条数，有系统设置的最大值限制。
	 * </p>
	 */
	@Parameter(description = "每页显示条数, 最大值为 100",
			schema = @Schema(title = "每页显示条数", description = "最大值为系统设置，默认 100", minimum = "1", defaultValue = "10",
					example = "10"))
	@Min(value = 1, message = "每页显示条数不能小于1")
	private long size = 10;

	/**
	 * 排序字段
	 * <p>
	 * 已弃用，推荐使用 {@link #sort} 参数。 多个排序字段以逗号分隔。
	 * </p>
	 * @deprecated 推荐使用 sort 参数，以便更直观的展示排序规则
	 * @see #sort
	 */
	@Deprecated
	@Parameter(description = "（已弃用）排序字段，多个排序字段以,分割",
			schema = @Schema(title = "排序字段", pattern = "^(" + SORT_FILED_REGEX + ")(,(" + SORT_FILED_REGEX + "))*$"))
	@Pattern(regexp = "^" + SORT_FILED_REGEX + "(," + SORT_FILED_REGEX + ")*$", message = "排序字段格式非法")
	private String sortFields;

	/**
	 * 排序方式
	 * <p>
	 * 已弃用，推荐使用 {@link #sort} 参数。 与排序字段一一对应，以逗号分隔。
	 * </p>
	 * @deprecated 推荐使用 sort 参数，以便更直观的展示排序规则
	 * @see #sort
	 */
	@Deprecated
	@Parameter(description = "（已弃用）排序方式，与排序字段一一对应，以,分割",
			schema = @Schema(title = "排序方式", pattern = "^(" + SORT_FILED_ORDER + ")(,(" + SORT_FILED_ORDER + "))*$"))
	@Pattern(regexp = "^" + SORT_FILED_ORDER + "(," + SORT_FILED_ORDER + ")*$", message = "排序字段格式非法")
	private String sortOrders;

	/**
	 * 排序规则
	 * <p>
	 * 格式为：property(,asc|desc)，默认为升序。 支持传入多个排序字段。 当此属性有值时，忽略 sortFields 和 sortOrders。
	 * </p>
	 */
	@Parameter(description = "排序规则，格式为：property(,asc|desc)。" + "默认为升序。" + "支持传入多个排序字段。", name = "sort",
			array = @ArraySchema(schema = @Schema(type = "string", pattern = SORT_REGEX, example = "id,desc")))
	@Pattern(regexp = SORT_REGEX, message = "排序字段格式非法")
	private List<String> sort;

}
