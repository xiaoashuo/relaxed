package com.relaxed.common.risk.admin.controller;

import com.relaxed.common.risk.model.entity.Rule;
import com.relaxed.common.risk.model.qo.RuleQO;
import com.relaxed.common.risk.model.vo.RuleVO;

import com.relaxed.common.risk.biz.service.RuleService;

import com.relaxed.common.model.domain.PageParam;
import com.relaxed.common.model.domain.PageResult;
import com.relaxed.common.model.result.BaseResultCode;
import com.relaxed.common.model.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 控制器
 * </p>
 *
 * @author Yakir
 * @since 2021-09-11T11:23:02.509
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("rule")
@Api(tags = "")
public class RuleController {

	private final RuleService ruleService;

	/**
	 * 分页查询
	 * @param pageParam {@link PageParam} 分页参数
	 * @param ruleQO {@link RuleQO} 查询条件
	 * @return @{code R<PageResult<RuleVO>>} 通用返回体
	 */
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@GetMapping("/page")
	public R<PageResult<RuleVO>> page(PageParam pageParam, RuleQO ruleQO) {
		return R.ok(ruleService.selectByPage(pageParam, ruleQO));
	}

	/**
	 * 新增数据
	 * @param rule {@link Rule} 数据参数
	 * @return {@code R<?>} 通用返回体
	 */
	@ApiOperation(value = "新增数据", notes = "新增数据")
	@PostMapping
	public R<?> save(@RequestBody Rule rule) {
		return ruleService.save(rule) ? R.ok() : R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "新增数据失败");
	}

	/**
	 * 更新数据
	 * @param rule {@link Rule} 更新数据
	 * @return {@code R<?>}通用返回体
	 */
	@ApiOperation(value = "更新数据", notes = "更新数据")
	@PutMapping
	public R<?> updateById(@RequestBody Rule rule) {
		return ruleService.updateById(rule) ? R.ok() : R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "更新数据失败");
	}

	/**
	 * 根据id删除数据
	 * @param id {@code id} id
	 * @return {@code R<?>} 通用返回体
	 */
	@ApiOperation(value = "根据id删除数据", notes = "根据id删除数据")
	@DeleteMapping("/{id}")
	public R<?> removeById(@PathVariable Long id) {
		return ruleService.removeById(id) ? R.ok() : R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "根据id删除数据失败");
	}

}
