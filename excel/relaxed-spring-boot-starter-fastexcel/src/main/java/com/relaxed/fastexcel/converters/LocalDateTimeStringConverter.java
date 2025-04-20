package com.relaxed.fastexcel.converters;

import cn.idev.excel.converters.Converter;
import cn.idev.excel.enums.CellDataTypeEnum;
import cn.idev.excel.metadata.GlobalConfiguration;
import cn.idev.excel.metadata.data.ReadCellData;
import cn.idev.excel.metadata.data.WriteCellData;
import cn.idev.excel.metadata.property.ExcelContentProperty;
import cn.idev.excel.util.DateUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * LocalDateTime与String类型转换器 用于Excel导入导出时LocalDateTime类型的转换处理 主要功能: 1.
 * 支持LocalDateTime与String的双向转换 2. 支持自定义日期时间格式 3. 支持多种日期时间格式自动识别 4. 支持全局配置和属性级配置
 *
 * @author L.cm
 * @since 1.0.0
 */
public enum LocalDateTimeStringConverter implements Converter<LocalDateTime> {

	/**
	 * 转换器单例实例
	 */
	INSTANCE;

	/**
	 * 日期格式中的分隔符
	 */
	private static final String MINUS = "-";

	/**
	 * 获取支持的Java类型
	 * @return LocalDateTime.class
	 */
	@Override
	public Class supportJavaTypeKey() {
		return LocalDateTime.class;
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
	 * 将Excel单元格数据转换为Java LocalDateTime类型 1. 如果未指定格式,自动识别日期时间格式 2. 如果指定了格式,使用指定的格式解析
	 * @param cellData Excel单元格数据
	 * @param contentProperty 内容属性(包含日期时间格式配置)
	 * @param globalConfiguration 全局配置
	 * @return 转换后的LocalDateTime对象
	 */
	@Override
	public LocalDateTime convertToJavaData(ReadCellData cellData, ExcelContentProperty contentProperty,
			GlobalConfiguration globalConfiguration) {
		String stringValue = cellData.getStringValue();
		String pattern;
		if (contentProperty == null || contentProperty.getDateTimeFormatProperty() == null) {
			pattern = switchDateFormat(stringValue);
		}
		else {
			pattern = contentProperty.getDateTimeFormatProperty().getFormat();
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		return LocalDateTime.parse(cellData.getStringValue(), formatter);
	}

	/**
	 * 将Java LocalDateTime类型转换为Excel单元格数据 1. 如果未指定格式,使用默认的19位日期时间格式(yyyy-MM-dd HH:mm:ss)
	 * 2. 如果指定了格式,使用指定的格式输出
	 * @param value LocalDateTime值
	 * @param contentProperty 内容属性(包含日期时间格式配置)
	 * @param globalConfiguration 全局配置
	 * @return 转换后的Excel单元格数据
	 */
	@Override
	public WriteCellData<String> convertToExcelData(LocalDateTime value, ExcelContentProperty contentProperty,
			GlobalConfiguration globalConfiguration) {
		String pattern;
		if (contentProperty == null || contentProperty.getDateTimeFormatProperty() == null) {
			pattern = DateUtils.DATE_FORMAT_19;
		}
		else {
			pattern = contentProperty.getDateTimeFormatProperty().getFormat();
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		return new WriteCellData<>(value.format(formatter));
	}

	/**
	 * 根据日期时间字符串长度自动识别格式 支持以下格式: 1. 19位: yyyy-MM-dd HH:mm:ss 或 yyyy/MM/dd HH:mm:ss 2. 17位:
	 * yyyyMMdd HH:mm:ss 3. 14位: yyyyMMddHHmmss 4. 10位: yyyy-MM-dd
	 * @param dateString 日期时间字符串
	 * @return 匹配的日期时间格式
	 * @throws IllegalArgumentException 当无法识别日期格式时抛出
	 */
	private static String switchDateFormat(String dateString) {
		int length = dateString.length();
		switch (length) {
		case 19:
			if (dateString.contains(MINUS)) {
				return DateUtils.DATE_FORMAT_19;
			}
			else {
				return DateUtils.DATE_FORMAT_19_FORWARD_SLASH;
			}
		case 17:
			return DateUtils.DATE_FORMAT_17;
		case 14:
			return DateUtils.DATE_FORMAT_14;
		case 10:
			return DateUtils.DATE_FORMAT_10;
		default:
			throw new IllegalArgumentException("can not find date format for：" + dateString);
		}
	}

}
