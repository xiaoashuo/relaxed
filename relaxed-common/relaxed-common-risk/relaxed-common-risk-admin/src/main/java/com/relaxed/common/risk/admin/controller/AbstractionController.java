package com.relaxed.common.risk.admin.controller;

import com.relaxed.common.model.domain.PageParam;
import com.relaxed.common.model.domain.PageResult;
import com.relaxed.common.model.result.BaseResultCode;
import com.relaxed.common.model.result.R;
import com.relaxed.common.risk.biz.service.AbstractionService;
import com.relaxed.common.risk.model.entity.Abstraction;
import com.relaxed.common.risk.model.qo.AbstractionQO;
import com.relaxed.common.risk.model.vo.AbstractionVO;
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
 * @since 2021-09-22T15:24:50.128
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("abstraction")
@Api(tags = "")
public class AbstractionController {

	private final AbstractionService abstractionService;

	/**
	 * 分页查询
	 * @param pageParam {@link PageParam} 分页参数
	 * @param abstractionQO {@link AbstractionQO} 查询条件
	 * @return @{code R<PageResult<AbstractionVO>>} 通用返回体
	 */
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@GetMapping("/page")
	public R<PageResult<AbstractionVO>> page(PageParam pageParam, AbstractionQO abstractionQO) {
		return R.ok(abstractionService.selectByPage(pageParam, abstractionQO));
	}

	/**
	 * 新增数据
	 * @param abstraction {@link Abstraction} 数据参数
	 * @return {@code R<?>} 通用返回体
	 */
	@ApiOperation(value = "新增数据", notes = "新增数据")
	@PostMapping
	public R<?> save(@RequestBody Abstraction abstraction) {
		return abstractionService.add(abstraction) ? R.ok() : R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "新增数据失败");
	}

	/**
	 * 更新数据
	 * @param abstraction {@link Abstraction} 更新数据
	 * @return {@code R<?>}通用返回体
	 */
	@ApiOperation(value = "更新数据", notes = "更新数据")
	@PutMapping
	public R<?> updateById(@RequestBody Abstraction abstraction) {
		return abstractionService.edit(abstraction) ? R.ok() : R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "更新数据失败");
	}

	/**
	 * 根据id删除数据
	 * @param id {@code id} id
	 * @return {@code R<?>} 通用返回体
	 */
	@ApiOperation(value = "根据id删除数据", notes = "根据id删除数据")
	@DeleteMapping("/{id}")
	public R<?> removeById(@PathVariable Long id) {
		return abstractionService.del(id) ? R.ok() : R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "根据id删除数据失败");
	}

}