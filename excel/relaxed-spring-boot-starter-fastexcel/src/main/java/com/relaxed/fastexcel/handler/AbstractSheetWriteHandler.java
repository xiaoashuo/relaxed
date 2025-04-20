package com.relaxed.fastexcel.handler;

import cn.idev.excel.EasyExcel;
import cn.idev.excel.ExcelWriter;
import cn.idev.excel.converters.Converter;
import cn.idev.excel.write.builder.ExcelWriterBuilder;
import cn.idev.excel.write.builder.ExcelWriterSheetBuilder;
import cn.idev.excel.write.handler.WriteHandler;
import cn.idev.excel.write.metadata.WriteSheet;
import com.relaxed.fastexcel.annotation.ResponseExcel;
import com.relaxed.fastexcel.annotation.Sheet;
import com.relaxed.fastexcel.aop.DynamicNameAspect;
import com.relaxed.fastexcel.config.ExcelConfigProperties;
import com.relaxed.fastexcel.converters.LocalDateStringConverter;
import com.relaxed.fastexcel.converters.LocalDateTimeStringConverter;
import com.relaxed.fastexcel.enhance.WriterBuilderEnhancer;
import com.relaxed.fastexcel.head.HeadGenerator;
import com.relaxed.fastexcel.head.HeadMeta;
import com.relaxed.fastexcel.kit.ExcelException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Excel工作表写入处理器抽象基类 提供Excel导出的基础功能和通用实现 主要功能: 1. 支持Excel文件导出 2. 支持模板导出 3. 支持自定义表头 4.
 * 支持数据类型转换 5. 支持密码保护 6. 支持列包含/排除 7. 支持自定义写入处理器
 *
 * @author lengleng
 * @author L.cm
 * @since 1.0.0
 */
@RequiredArgsConstructor
public abstract class AbstractSheetWriteHandler implements SheetWriteHandler, ApplicationContextAware {

	/**
	 * Excel配置属性 用于获取Excel相关的配置信息
	 */
	private final ExcelConfigProperties configProperties;

	/**
	 * 类型转换器提供者 用于获取自定义的类型转换器
	 */
	private final ObjectProvider<List<Converter<?>>> converterProvider;

	/**
	 * Excel写入构建器增强器 用于增强Excel写入构建器的功能
	 */
	private final WriterBuilderEnhancer excelWriterBuilderEnhance;

	/**
	 * Spring应用上下文 用于获取Spring容器中的Bean
	 */
	private ApplicationContext applicationContext;

	/**
	 * 校验Excel导出配置 校验内容: 1. 文件名不能为空 2. 工作表配置不能为空
	 * @param responseExcel Excel响应注解
	 * @throws ExcelException 当配置不合法时抛出
	 */
	@Override
	public void check(ResponseExcel responseExcel) {
		if (!StringUtils.hasText(responseExcel.name())) {
			throw new ExcelException("@ResponseExcel name 配置不合法");
		}

		if (responseExcel.sheets().length == 0) {
			throw new ExcelException("@ResponseExcel sheet 配置不合法");
		}
	}

	/**
	 * 执行Excel导出操作 处理流程: 1. 校验配置 2. 获取文件名 3. 设置响应头 4. 执行写入操作
	 * @param o 待导出的数据对象
	 * @param response HTTP响应对象
	 * @param responseExcel Excel响应注解
	 */
	@Override
	@SneakyThrows
	public void export(Object o, HttpServletResponse response, ResponseExcel responseExcel) {
		check(responseExcel);
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		String name = (String) Objects.requireNonNull(requestAttributes).getAttribute(DynamicNameAspect.EXCEL_NAME_KEY,
				RequestAttributes.SCOPE_REQUEST);
		if (name == null) {
			name = UUID.randomUUID().toString();
		}
		String fileName = String.format("%s%s", URLEncoder.encode(name, "UTF-8"), responseExcel.suffix().getValue())
				.replaceAll("\\+", "%20");
		// 根据实际的文件类型找到对应的 contentType
		String contentType = MediaTypeFactory.getMediaType(fileName).map(MediaType::toString)
				.orElse("application/vnd.ms-excel");
		response.setContentType(contentType);
		response.setCharacterEncoding("utf-8");
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName);
		write(o, response, responseExcel);
	}

	/**
	 * 获取Excel写入器 处理流程: 1. 创建写入构建器 2. 注册默认转换器 3. 设置密码保护 4. 设置列包含/排除 5. 注册自定义写入处理器 6.
	 * 注册自定义转换器 7. 设置模板 8. 增强构建器
	 * @param response HTTP响应对象
	 * @param responseExcel Excel响应注解
	 * @return Excel写入器
	 */
	@SneakyThrows
	public ExcelWriter getExcelWriter(HttpServletResponse response, ResponseExcel responseExcel) {
		ExcelWriterBuilder writerBuilder = EasyExcel.write(response.getOutputStream())
				.registerConverter(LocalDateStringConverter.INSTANCE)
				.registerConverter(LocalDateTimeStringConverter.INSTANCE).autoCloseStream(true)
				.excelType(responseExcel.suffix()).inMemory(responseExcel.inMemory());

		if (StringUtils.hasText(responseExcel.password())) {
			writerBuilder.password(responseExcel.password());
		}

		if (responseExcel.include().length != 0) {
			writerBuilder.includeColumnFiledNames(Arrays.asList(responseExcel.include()));
		}

		if (responseExcel.exclude().length != 0) {
			writerBuilder.excludeColumnFiledNames(Arrays.asList(responseExcel.exclude()));
		}

		if (responseExcel.writeHandler().length != 0) {
			for (Class<? extends WriteHandler> clazz : responseExcel.writeHandler()) {
				writerBuilder.registerWriteHandler(BeanUtils.instantiateClass(clazz));
			}
		}

		// 自定义注入的转换器
		registerCustomConverter(writerBuilder);

		if (responseExcel.converter().length != 0) {
			for (Class<? extends Converter> clazz : responseExcel.converter()) {
				writerBuilder.registerConverter(BeanUtils.instantiateClass(clazz));
			}
		}

		String templatePath = configProperties.getTemplatePath();
		if (StringUtils.hasText(responseExcel.template())) {
			ClassPathResource classPathResource = new ClassPathResource(
					templatePath + File.separator + responseExcel.template());
			InputStream inputStream = classPathResource.getInputStream();
			writerBuilder.withTemplate(inputStream);
		}

		writerBuilder = excelWriterBuilderEnhance.enhanceExcel(writerBuilder, response, responseExcel, templatePath);

		return writerBuilder.build();
	}

	/**
	 * 注册自定义转换器 子类可以重写此方法以添加自定义转换器
	 * @param builder Excel写入构建器
	 */
	public void registerCustomConverter(ExcelWriterBuilder builder) {
		converterProvider.ifAvailable(converters -> converters.forEach(builder::registerConverter));
	}

	/**
	 * 创建工作表配置 处理流程: 1. 设置工作表编号和名称 2. 处理模板写入 3. 处理表头生成 4. 处理列包含/排除 5. 增强工作表配置
	 * @param sheet 工作表注解
	 * @param dataClass 数据类型
	 * @param template 模板路径
	 * @param bookHeadEnhancerClass 表头生成器类型
	 * @return 工作表配置
	 */
	public WriteSheet sheet(Sheet sheet, Class<?> dataClass, String template,
			Class<? extends HeadGenerator> bookHeadEnhancerClass) {

		// Sheet 编号和名称
		Integer sheetNo = sheet.sheetNo() >= 0 ? sheet.sheetNo() : null;
		String sheetName = sheet.sheetName();

		// 是否模板写入
		ExcelWriterSheetBuilder writerSheetBuilder = StringUtils.hasText(template) ? EasyExcel.writerSheet(sheetNo)
				: EasyExcel.writerSheet(sheetNo, sheetName);

		// 头信息增强 1. 优先使用 sheet 指定的头信息增强 2. 其次使用 @ResponseExcel 中定义的全局头信息增强
		Class<? extends HeadGenerator> headGenerateClass = null;
		if (isNotInterface(sheet.headGenerateClass())) {
			headGenerateClass = sheet.headGenerateClass();
		}
		else if (isNotInterface(bookHeadEnhancerClass)) {
			headGenerateClass = bookHeadEnhancerClass;
		}
		// 定义头信息增强则使用其生成头信息，否则使用 dataClass 来自动获取
		if (headGenerateClass != null) {
			fillCustomHeadInfo(dataClass, bookHeadEnhancerClass, writerSheetBuilder);
		}
		else if (dataClass != null) {
			writerSheetBuilder.head(dataClass);
			if (sheet.excludes().length > 0) {
				writerSheetBuilder.excludeColumnFiledNames(Arrays.asList(sheet.excludes()));
			}
			if (sheet.includes().length > 0) {
				writerSheetBuilder.includeColumnFiledNames(Arrays.asList(sheet.includes()));
			}
		}

		// sheetBuilder 增强
		writerSheetBuilder = excelWriterBuilderEnhance.enhanceSheet(writerSheetBuilder, sheetNo, sheetName, dataClass,
				template, headGenerateClass);

		return writerSheetBuilder.build();
	}

	/**
	 * 填充自定义表头信息 使用指定的表头生成器生成表头信息
	 * @param dataClass 数据类型
	 * @param headEnhancerClass 表头生成器类型
	 * @param writerSheetBuilder 工作表构建器
	 */
	private void fillCustomHeadInfo(Class<?> dataClass, Class<? extends HeadGenerator> headEnhancerClass,
			ExcelWriterSheetBuilder writerSheetBuilder) {
		HeadGenerator headGenerator = this.applicationContext.getBean(headEnhancerClass);
		Assert.notNull(headGenerator, "The header generated bean does not exist.");
		HeadMeta head = headGenerator.head(dataClass);
		writerSheetBuilder.head(head.getHead());
		writerSheetBuilder.excludeColumnFiledNames(head.getIgnoreHeadFields());
	}

	/**
	 * 判断是否为非接口类型 用于判断表头生成器是否已指定
	 * @param headGeneratorClass 表头生成器类型
	 * @return true表示已指定,false表示未指定(默认值)
	 */
	private boolean isNotInterface(Class<? extends HeadGenerator> headGeneratorClass) {
		return !Modifier.isInterface(headGeneratorClass.getModifiers());
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
