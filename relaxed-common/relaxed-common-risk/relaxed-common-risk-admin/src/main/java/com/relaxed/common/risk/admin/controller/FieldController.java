package com.relaxed.common.risk.admin.controller;

import com.relaxed.common.risk.biz.service.ModelService;
import com.relaxed.common.risk.model.entity.Field;
import com.relaxed.common.risk.model.entity.Model;
import com.relaxed.common.risk.model.enums.FieldType;
import com.relaxed.common.risk.model.qo.FieldQO;
import com.relaxed.common.risk.model.vo.FieldVO;

import com.relaxed.common.risk.biz.service.FieldService;

import com.relaxed.common.model.domain.PageParam;
import com.relaxed.common.model.domain.PageResult;
import com.relaxed.common.model.result.BaseResultCode;
import com.relaxed.common.model.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 控制器
 * </p>
 *
 * @author Yakir
 * @since 2021-09-11T11:23:02.703
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("field")
@Api(tags = "")
public class FieldController {

	private final FieldService fieldService;

	private final ModelService modelService;

	/**
	 * 分页查询
	 * @param pageParam {@link PageParam} 分页参数
	 * @param fieldQO {@link FieldQO} 查询条件
	 * @return @{code R<PageResult<FieldVO>>} 通用返回体
	 */
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@GetMapping("/page")
	public R<PageResult<FieldVO>> page(PageParam pageParam, FieldQO fieldQO) {
		return R.ok(fieldService.selectByPage(pageParam, fieldQO));
	}


	@ApiOperation(value = "字段类型列表", notes = "字段类型列表")
	@GetMapping("/types")
	public  R  listFieldTypes(){
		Map<String,Object> fieldTypes=new HashMap<>(16);
		for (FieldType value : FieldType.values()) {
			fieldTypes.put("name",value.name());
			fieldTypes.put("desc",value.getDesc());
		}
		return R.ok(fieldTypes);
	}

	/**
	 * 新增数据
	 * @param field {@link Field} 数据参数
	 * @return {@code R<?>} 通用返回体
	 */
	@ApiOperation(value = "新增数据", notes = "新增数据")
	@PostMapping
	public R<?> save(@RequestBody Field field) {
		return fieldService.add(field) ? R.ok() : R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "新增数据失败");
	}

	/**
	 * 更新数据
	 * @param field {@link Field} 更新数据
	 * @return {@code R<?>}通用返回体
	 */
	@ApiOperation(value = "更新数据", notes = "更新数据")
	@PutMapping
	public R<?> updateById(@RequestBody Field field) {
		return fieldService.edit(field) ? R.ok() : R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "更新数据失败");
	}

	/**
	 * 根据id删除数据
	 * @param id {@code id} id
	 * @return {@code R<?>} 通用返回体
	 */
	@ApiOperation(value = "根据id删除数据", notes = "根据id删除数据")
	@DeleteMapping("/{id}")
	public R<?> removeById(@PathVariable Long id) {
		Field field = fieldService.getById(id);
		Assert.notNull(field,"field can not exists.");
		Long modelId = field.getModelId();
		Model model = modelService.getById(modelId);
		Assert.notNull(field,"model can not exists.");

		return fieldService.del(model,field) ? R.ok() : R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "根据id删除数据失败");
	}

}
