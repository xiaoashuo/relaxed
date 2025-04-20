package com.relaxed.fastexcel;

import cn.idev.excel.converters.Converter;
import com.relaxed.fastexcel.aop.ResponseExcelReturnValueHandler;
import com.relaxed.fastexcel.config.ExcelConfigProperties;
import com.relaxed.fastexcel.enhance.DefaultWriterBuilderEnhancer;
import com.relaxed.fastexcel.enhance.WriterBuilderEnhancer;
import com.relaxed.fastexcel.handler.ManySheetWriteHandler;
import com.relaxed.fastexcel.handler.SheetWriteHandler;
import com.relaxed.fastexcel.handler.SingleSheetWriteHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Excel处理器配置类 负责配置Excel相关的处理器,包括: 1. Excel构建增强器 2. 单Sheet写入处理器 3. 多Sheet写入处理器 4.
 * Excel返回值处理器
 *
 * @author Hccake
 * @since 1.0.0
 */
@RequiredArgsConstructor
@Configuration
public class ExcelHandlerConfiguration {

	/**
	 * Excel配置属性
	 */
	private final ExcelConfigProperties configProperties;

	/**
	 * 类型转换器提供者
	 */
	private final ObjectProvider<List<Converter<?>>> converterProvider;

	/**
	 * 注册Excel构建增强器 用于增强Excel构建过程,默认实现不进行任何增强
	 * @return DefaultWriterBuilderEnhancer 默认增强器
	 */
	@Bean
	@ConditionalOnMissingBean
	public WriterBuilderEnhancer writerBuilderEnhancer() {
		return new DefaultWriterBuilderEnhancer();
	}

	/**
	 * 注册单Sheet写入处理器 用于处理单个Sheet的Excel写入
	 * @return SingleSheetWriteHandler 单Sheet写入处理器
	 */
	@Bean
	@ConditionalOnMissingBean
	public SingleSheetWriteHandler singleSheetWriteHandler() {
		return new SingleSheetWriteHandler(configProperties, converterProvider, writerBuilderEnhancer());
	}

	/**
	 * 注册多Sheet写入处理器 用于处理多个Sheet的Excel写入
	 * @return ManySheetWriteHandler 多Sheet写入处理器
	 */
	@Bean
	@ConditionalOnMissingBean
	public ManySheetWriteHandler manySheetWriteHandler() {
		return new ManySheetWriteHandler(configProperties, converterProvider, writerBuilderEnhancer());
	}

	/**
	 * 注册Excel返回值处理器 用于处理Controller方法返回Excel的响应
	 * @param sheetWriteHandlerList Sheet写入处理器列表
	 * @return ResponseExcelReturnValueHandler Excel返回值处理器
	 */
	@Bean
	@ConditionalOnMissingBean
	public ResponseExcelReturnValueHandler responseExcelReturnValueHandler(
			List<SheetWriteHandler> sheetWriteHandlerList) {
		return new ResponseExcelReturnValueHandler(sheetWriteHandlerList);
	}

}
