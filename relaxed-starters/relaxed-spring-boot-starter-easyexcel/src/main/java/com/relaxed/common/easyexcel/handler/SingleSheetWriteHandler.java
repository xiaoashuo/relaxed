package com.relaxed.common.easyexcel.handler;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.write.metadata.WriteSheet;

import com.relaxed.common.easyexcel.annotation.ResponseExcel;
import com.relaxed.common.easyexcel.config.ExcelConfigProperties;
import com.relaxed.common.easyexcel.enhance.WriterBuilderEnhancer;
import com.relaxed.common.easyexcel.kit.ExcelException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.ObjectProvider;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author lengleng
 * @date 2020/3/29
 * <p>
 * 处理单sheet 页面
 */
public class SingleSheetWriteHandler extends AbstractSheetWriteHandler {

	public SingleSheetWriteHandler(ExcelConfigProperties configProperties,
			ObjectProvider<List<Converter<?>>> converterProvider, WriterBuilderEnhancer excelWriterBuilderEnhance) {
		super(configProperties, converterProvider, excelWriterBuilderEnhance);
	}

	/**
	 * obj 是List 且list不为空同时list中的元素不是是List 才返回true
	 * @param obj 返回对象
	 * @return
	 */
	@Override
	public boolean support(Object obj) {
		if (obj instanceof List) {
			List objList = (List) obj;
			return !objList.isEmpty() && !(objList.get(0) instanceof List);
		}
		else {
			throw new ExcelException("@ResponseExcel 返回值必须为List类型");
		}
	}

	@Override
	@SneakyThrows
	public void write(Object obj, HttpServletResponse response, ResponseExcel responseExcel) {
		List list = (List) obj;
		ExcelWriter excelWriter = getExcelWriter(response, responseExcel);

		// 有模板则不指定sheet名
		Class<?> dataClass = list.get(0).getClass();
		WriteSheet sheet = this.sheet(responseExcel.sheets()[0], dataClass, responseExcel.template(),
				responseExcel.headGenerator());
		excelWriter.write(list, sheet);
		excelWriter.finish();
	}

}
