package com.relaxed.fastexcel.handler;

import cn.idev.excel.ExcelWriter;
import cn.idev.excel.converters.Converter;
import cn.idev.excel.write.metadata.WriteSheet;
import com.relaxed.fastexcel.annotation.ResponseExcel;
import com.relaxed.fastexcel.config.ExcelConfigProperties;
import com.relaxed.fastexcel.enhance.WriterBuilderEnhancer;
import com.relaxed.fastexcel.kit.ExcelException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.ObjectProvider;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 单工作表Excel写入处理器 用于处理单个工作表的Excel导出 主要功能: 1. 支持单个List数据的导出 2. 支持模板导出 3. 支持自定义表头 4.
 * 支持数据类型转换
 *
 * @author lengleng
 * @since 1.0.0
 */
public class SingleSheetWriteHandler extends AbstractSheetWriteHandler {

	/**
	 * 构造函数
	 * @param configProperties Excel配置属性
	 * @param converterProvider 类型转换器提供者
	 * @param excelWriterBuilderEnhance Excel写入构建器增强器
	 */
	public SingleSheetWriteHandler(ExcelConfigProperties configProperties,
			ObjectProvider<List<Converter<?>>> converterProvider, WriterBuilderEnhancer excelWriterBuilderEnhance) {
		super(configProperties, converterProvider, excelWriterBuilderEnhance);
	}

	/**
	 * 判断是否支持处理指定对象 支持的条件: 1. 对象必须是List类型 2. List不能为空 3. List中的元素不能是List类型(与多工作表处理器区分)
	 * @param obj 待处理的对象
	 * @return true表示支持,false表示不支持
	 * @throws ExcelException 当对象不是List类型时抛出
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

	/**
	 * 执行Excel写入操作 处理流程: 1. 获取Excel写入器 2. 获取数据类型 3. 创建工作表配置 4. 写入数据 5. 完成写入
	 * @param obj 待写入的数据对象(List类型)
	 * @param response HTTP响应对象
	 * @param responseExcel Excel响应注解
	 */
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
