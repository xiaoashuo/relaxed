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
 * 分页参数
 *
 * @author Yakir
 */
@Data
@Schema(title = "分页查询参数")
public class PageParam {

	@Schema(title = "当前页码", description = "从 1 开始", defaultValue = "1", example = "1")
	@Min(value = 1, message = "当前页不能小于 1")
	private long current = 1;

	@Schema(title = "每页显示条数", description = "最大值为系统设置，默认 100", defaultValue = "10")
	@Min(value = 1, message = "每页显示条数不能小于1")
	private long size = 10;

	/**
	 * 排序规则
	 * @param key 字段名称: field value 是否升序: true 升序 |false 降序
	 */
	@Schema(title = "排序规则")
	private Map<String, Boolean> sort = new HashMap<>();

}
