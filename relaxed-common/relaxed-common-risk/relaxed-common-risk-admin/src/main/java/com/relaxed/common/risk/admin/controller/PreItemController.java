package com.relaxed.common.risk.admin.controller;

import com.relaxed.common.risk.model.entity.PreItem;
import com.relaxed.common.risk.model.qo.PreItemQO;
import com.relaxed.common.risk.model.vo.PreItemVO;

import com.relaxed.common.risk.biz.service.PreItemService;

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
 * @since 2021-09-11T11:23:02.641
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("pre/item")
@Api(tags = "")
public class PreItemController {

	private final PreItemService preItemService;

	/**
	 * 分页查询
	 * @param pageParam {@link PageParam} 分页参数
	 * @param preItemQO {@link PreItemQO} 查询条件
	 * @return @{code R<PageResult<PreItemVO>>} 通用返回体
	 */
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@GetMapping("/page")
	public R<PageResult<PreItemVO>> page(PageParam pageParam, PreItemQO preItemQO) {
		return R.ok(preItemService.selectByPage(pageParam, preItemQO));
	}

	/**
	 * 新增数据
	 * @param preItem {@link PreItem} 数据参数
	 * @return {@code R<?>} 通用返回体
	 */
	@ApiOperation(value = "新增数据", notes = "新增数据")
	@PostMapping
	public R<?> save(@RequestBody PreItem preItem) {
		return preItemService.add(preItem) ? R.ok() : R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "新增数据失败");
	}

	/**
	 * 更新数据
	 * @param preItem {@link PreItem} 更新数据
	 * @return {@code R<?>}通用返回体
	 */
	@ApiOperation(value = "更新数据", notes = "更新数据")
	@PutMapping
	public R<?> updateById(@RequestBody PreItem preItem) {
		return preItemService.edit(preItem) ? R.ok() : R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "更新数据失败");
	}

	/**
	 * 根据id删除数据
	 * @param modelId {@code modelId} modelId
	 * @param id {@code id} id
	 * @return {@code R<?>} 通用返回体
	 */
	@ApiOperation(value = "根据id删除数据", notes = "根据id删除数据")
	@DeleteMapping("/{modelId}/{id}")
	public R<?> removeById(@PathVariable Long modelId,@PathVariable Long id) {
		return preItemService.del(modelId,id) ? R.ok() : R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "根据id删除数据失败");
	}

}
