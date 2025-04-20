package com.relaxed.common.model.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分页查询参数封装类
 * <p>
 * 用于封装分页查询的基本参数，包括页码、每页大小和排序规则。 支持自定义排序规则，可以指定多个字段的排序方式。
 * </p>
 *
 * @author Yakir
 * @since 1.0.0
 */
@Data
@Schema(title = "分页查询参数")
public class PageParam {

	/**
	 * 当前页码
	 * <p>
	 * 从1开始计数，表示要查询的页码。
	 * </p>
	 */
	@Schema(title = "当前页码", description = "从 1 开始", defaultValue = "1", example = "1")
	@Min(value = 1, message = "当前页不能小于 1")
	private long current = 1;

	/**
	 * 每页显示条数
	 * <p>
	 * 表示每页要显示的数据条数，有系统设置的最大值限制。
	 * </p>
	 */
	@Schema(title = "每页显示条数", description = "最大值为系统设置，默认 100", defaultValue = "10")
	@Min(value = 1, message = "每页显示条数不能小于1")
	private long size = 10;

	/**
	 * 排序规则
	 * <p>
	 * 用于指定查询结果的排序方式，key为字段名称，value为是否升序。 true表示升序，false表示降序。
	 * </p>
	 */
	@Schema(title = "排序规则", description = "key为字段名称，value为是否升序（true升序，false降序）")
	private Map<String, Boolean> sort = new HashMap<>();

}
