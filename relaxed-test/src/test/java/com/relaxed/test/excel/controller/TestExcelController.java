package com.relaxed.test.excel.controller;

import com.apifan.common.random.source.DateTimeSource;
import com.apifan.common.random.source.NumberSource;
import com.apifan.common.random.source.PersonInfoSource;
import com.relaxed.common.model.result.R;
import com.relaxed.fastexcel.annotation.RequestExcel;
import com.relaxed.fastexcel.annotation.ResponseExcel;
import com.relaxed.fastexcel.annotation.Sheet;
import com.relaxed.fastexcel.vo.ErrorMessage;
import com.relaxed.test.excel.domain.ExcelModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yakir
 * @Topic TestExcelController
 * @Description
 * @date 2024/12/28 15:02
 * @Version 1.0
 */
@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/excel")
public class TestExcelController {

	/**
	 * 导出单sheet
	 * @return
	 */
	@ResponseExcel(name = "单sheet模板")
	// @ResponseExcel( name="'客户记录#'.replace('#','') +
	// T(java.time.LocalDateTime).now().format(T(java.time.format.DateTimeFormatter).ofPattern('yyyyMMddHHmmss'))",sheets
	// = @Sheet(sheetName = "客户记录"))
	@GetMapping("/sheet")
	public List<ExcelModel> sheet() {
		return mockExcelModelList();
	}

	/**
	 * excel导出多sheet
	 * @return
	 */
	@ResponseExcel(name = "多sheet模板1", sheets = { @Sheet(sheetName = "sheet1"), @Sheet(sheetName = "sheet2") })
	@GetMapping("/sheet/many")
	public List<List<ExcelModel>> sheetMany() {
		List<List<ExcelModel>> datas = new ArrayList<>();
		datas.add(mockExcelModelList());
		datas.add(mockExcelModelList());
		return datas;
	}

	/**
	 * excel上传
	 * @param dataList
	 * @param bindingResult
	 * @return
	 */
	@PostMapping("/upload")
	public R upload(@RequestExcel List<ExcelModel> dataList, BindingResult bindingResult) {
		// JSR 303 校验通用校验获取失败的数据
		List<ErrorMessage> errorMessageList = (List<ErrorMessage>) bindingResult.getTarget();
		return R.ok(dataList);
	}

	/**
	 * 解析前多少行
	 * @param dataList
	 * @param bindingResult
	 * @return
	 */
	@PostMapping("/upload/preHunds")
	public R uploadPreHunds(@RequestExcel(fileName = "dataList", numRows = 50) List<ExcelModel> dataList,
			BindingResult bindingResult) {
		// JSR 303 校验通用校验获取失败的数据
		List<ErrorMessage> errorMessageList = (List<ErrorMessage>) bindingResult.getTarget();
		return R.ok(dataList);
	}

	private static List<ExcelModel> mockExcelModelList() {
		List<ExcelModel> excelModels = new ArrayList<>();
		for (int i = 0; i < 200; i++) {
			ExcelModel excelModel = new ExcelModel();
			excelModel.setId(NumberSource.getInstance().randomInt(1, 101));
			excelModel.setUsername(PersonInfoSource.getInstance().randomChineseName());
			excelModel.setAge(NumberSource.getInstance().randomInt(1, 101));
			excelModel.setCreateTime(DateTimeSource.getInstance().randomPastTime(7));
			excelModel.setUpdateTime(DateTimeSource.getInstance().randomFutureTime(7));

			excelModels.add(excelModel);
		}
		return excelModels;
	}

}
