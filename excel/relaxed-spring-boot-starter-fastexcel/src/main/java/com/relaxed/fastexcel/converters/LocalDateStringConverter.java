package com.relaxed.fastexcel.converters;

import cn.idev.excel.converters.Converter;
import cn.idev.excel.enums.CellDataTypeEnum;
import cn.idev.excel.metadata.GlobalConfiguration;
import cn.idev.excel.metadata.data.ReadCellData;
import cn.idev.excel.metadata.data.WriteCellData;
import cn.idev.excel.metadata.property.ExcelContentProperty;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * LocalDate与String类型转换器 用于Excel导入导出时LocalDate类型的转换处理 主要功能: 1. 支持LocalDate与String的双向转换 2.
 * 支持自定义日期格式 3. 支持默认ISO日期格式 4. 支持全局配置和属性级配置
 *
 * @author L.cm
 * @since 1.0.0
 */
public enum LocalDateStringConverter implements Converter<LocalDate> {

	/**
	 * 转换器单例实例
	 */
	INSTANCE;

	/**
	 * 获取支持的Java类型
	 * @return LocalDate.class
	 */
	@Override
	public Class supportJavaTypeKey() {
		return LocalDate.class;
	}

	/**
	 * 获取支持的Excel单元格类型
	 * @return CellDataTypeEnum.STRING
	 */
	@Override
	public CellDataTypeEnum supportExcelTypeKey() {
		return CellDataTypeEnum.STRING;
	}

	/**
	 * 将Excel单元格数据转换为Java LocalDate类型 1. 如果未指定日期格式,使用默认的ISO格式解析 2. 如果指定了日期格式,使用指定的格式解析
	 * @param cellData Excel单元格数据
	 * @param contentProperty 内容属性(包含日期格式配置)
	 * @param globalConfiguration 全局配置
	 * @return 转换后的LocalDate对象
	 * @throws ParseException 日期解析异常
	 */
	@Override
	public LocalDate convertToJavaData(ReadCellData cellData, ExcelContentProperty contentProperty,
			GlobalConfiguration globalConfiguration) throws ParseException {
		if (contentProperty == null || contentProperty.getDateTimeFormatProperty() == null) {
			return LocalDate.parse(cellData.getStringValue());
		}
		else {
			DateTimeFormatter formatter = DateTimeFormatter
					.ofPattern(contentProperty.getDateTimeFormatProperty().getFormat());
			return LocalDate.parse(cellData.getStringValue(), formatter);
		}
	}

	/**
	 * 将Java LocalDate类型转换为Excel单元格数据 1. 如果未指定日期格式,使用ISO格式输出 2. 如果指定了日期格式,使用指定的格式输出
	 * @param value LocalDate值
	 * @param contentProperty 内容属性(包含日期格式配置)
	 * @param globalConfiguration 全局配置
	 * @return 转换后的Excel单元格数据
	 */
	@Override
	public WriteCellData<String> convertToExcelData(LocalDate value, ExcelContentProperty contentProperty,
			GlobalConfiguration globalConfiguration) {
		DateTimeFormatter formatter;
		if (contentProperty == null || contentProperty.getDateTimeFormatProperty() == null) {
			formatter = DateTimeFormatter.ISO_LOCAL_DATE;
		}
		else {
			formatter = DateTimeFormatter.ofPattern(contentProperty.getDateTimeFormatProperty().getFormat());
		}
		return new WriteCellData<>(value.format(formatter));
	}

}
