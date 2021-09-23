package com.relaxed.common.risk.admin.controller;

import com.relaxed.common.risk.model.entity.DataListRecords;
import com.relaxed.common.risk.model.qo.DataListRecordsQO;

import com.relaxed.common.risk.biz.service.DataListRecordsService;

import com.relaxed.common.model.domain.PageParam;
import com.relaxed.common.model.domain.PageResult;
import com.relaxed.common.model.result.BaseResultCode;
import com.relaxed.common.model.result.R;
import com.relaxed.common.risk.model.vo.DataListRecordsVO;
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
 * @since 2021-09-11T11:23:02.762
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("data/list/records")
@Api(tags = "")
public class DataListRecordsController {

	private final DataListRecordsService dataListRecordsService;

	/**
	 * 分页查询
	 * @param pageParam {@link PageParam} 分页参数
	 * @param dataListRecordsQO {@link DataListRecordsQO} 查询条件
	 * @return @{code R<PageResult<DataListRecordsVO>>} 通用返回体
	 */
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@GetMapping("/page")
	public R<PageResult<DataListRecordsVO>> page(PageParam pageParam, DataListRecordsQO dataListRecordsQO) {
		return R.ok(dataListRecordsService.selectByPage(pageParam, dataListRecordsQO));
	}

	/**
	 * 新增数据
	 * @param dataListRecords {@link DataListRecords} 数据参数
	 * @return {@code R<?>} 通用返回体
	 */
	@ApiOperation(value = "新增数据", notes = "新增数据")
	@PostMapping
	public R<?> save(@RequestBody DataListRecords dataListRecords) {
		return dataListRecordsService.add(dataListRecords) ? R.ok()
				: R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "新增数据失败");
	}

	/**
	 * 更新数据
	 * @param dataListRecords {@link DataListRecords} 更新数据
	 * @return {@code R<?>}通用返回体
	 */
	@ApiOperation(value = "更新数据", notes = "更新数据")
	@PutMapping
	public R<?> updateById(@RequestBody DataListRecords dataListRecords) {
		return dataListRecordsService.edit(dataListRecords) ? R.ok()
				: R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "更新数据失败");
	}

	/**
	 * 根据id删除数据
	 * @param id {@code id} id
	 * @return {@code R<?>} 通用返回体
	 */
	@ApiOperation(value = "根据id删除数据", notes = "根据id删除数据")
	@DeleteMapping("/{id}")
	public R<?> removeById(@PathVariable Long id) {
		return dataListRecordsService.del(id) ? R.ok() : R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "根据id删除数据失败");
	}

}
