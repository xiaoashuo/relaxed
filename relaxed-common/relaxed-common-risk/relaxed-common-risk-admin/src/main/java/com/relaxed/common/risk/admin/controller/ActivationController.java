package com.relaxed.common.risk.admin.controller;

import com.relaxed.common.model.domain.PageParam;
import com.relaxed.common.model.domain.PageResult;
import com.relaxed.common.model.result.BaseResultCode;
import com.relaxed.common.model.result.R;
import com.relaxed.common.risk.biz.service.ActivationService;
import com.relaxed.common.risk.model.entity.Activation;
import com.relaxed.common.risk.model.qo.ActivationQO;
import com.relaxed.common.risk.model.vo.ActivationVO;
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
 * @since 2021-09-22T16:03:38.120
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("activation")
@Api(tags = "决策控制器")
public class ActivationController {

	private final ActivationService activationService;

	/**
	 * 分页查询
	 * @param pageParam {@link PageParam} 分页参数
	 * @param activationQO {@link ActivationQO} 查询条件
	 * @return @{code R<PageResult<ActivationVO>>} 通用返回体
	 */
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@GetMapping("/page")
	public R<PageResult<ActivationVO>> page(PageParam pageParam, ActivationQO activationQO) {
		return R.ok(activationService.selectByPage(pageParam, activationQO));
	}

	/**
	 * 新增数据
	 * @param activation {@link Activation} 数据参数
	 * @return {@code R<?>} 通用返回体
	 */
	@ApiOperation(value = "新增数据", notes = "新增数据")
	@PostMapping
	public R<?> save(@RequestBody Activation activation) {
		return activationService.add(activation) ? R.ok() : R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "新增数据失败");
	}

	/**
	 * 更新数据
	 * @param activation {@link Activation} 更新数据
	 * @return {@code R<?>}通用返回体
	 */
	@ApiOperation(value = "更新数据", notes = "更新数据")
	@PutMapping
	public R<?> updateById(@RequestBody Activation activation) {
		return activationService.edit(activation) ? R.ok() : R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "更新数据失败");
	}

	/**
	 * 根据id删除数据
	 * @param id {@code id} id
	 * @return {@code R<?>} 通用返回体
	 */
	@ApiOperation(value = "根据id删除数据", notes = "根据id删除数据")
	@DeleteMapping("/{id}")
	public R<?> removeById(@PathVariable Long id) {
		return activationService.del(id) ? R.ok() : R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "根据id删除数据失败");
	}

}