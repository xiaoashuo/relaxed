package com.relaxed.fastexcel.aop;

import cn.idev.excel.EasyExcel;
import cn.idev.excel.FastExcel;
import com.relaxed.fastexcel.annotation.RequestExcel;
import com.relaxed.fastexcel.converters.LocalDateStringConverter;
import com.relaxed.fastexcel.converters.LocalDateTimeStringConverter;
import com.relaxed.fastexcel.handler.ListAnalysisEventListener;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.core.Conventions;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.ResolvableType;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Excel上传请求参数解析器 用于解析Controller方法中带有@RequestExcel注解的参数 主要功能: 1. 支持解析上传的Excel文件流 2.
 * 支持自定义读取监听器 3. 支持动态解析Sheet名称 4. 支持数据校验和错误处理
 *
 * @author lengleng
 * @author L.cm
 * @since 1.0.0
 */
@Slf4j
public class RequestExcelArgumentResolver implements HandlerMethodArgumentResolver {

	/**
	 * 参数名称发现器,用于获取方法参数名称
	 */
	private final ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

	/**
	 * SPEL表达式解析器,用于解析动态Sheet名称
	 */
	private final ExpressionParser expressionParser = new SpelExpressionParser();

	/**
	 * 判断是否支持解析该参数 当参数带有@RequestExcel注解时返回true
	 * @param parameter 方法参数
	 * @return 是否支持解析
	 */
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(RequestExcel.class);
	}

	/**
	 * 解析Excel上传请求参数 1. 获取上传的Excel文件流 2. 创建读取监听器 3. 解析Sheet名称 4. 读取Excel数据 5. 处理数据校验结果
	 * @param parameter 方法参数
	 * @param modelAndViewContainer 模型和视图容器
	 * @param webRequest Web请求对象
	 * @param webDataBinderFactory 数据绑定工厂
	 * @return 解析后的Excel数据列表
	 * @throws IllegalArgumentException 当参数类型不是List时抛出
	 */
	@Override
	@SneakyThrows
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer modelAndViewContainer,
			NativeWebRequest webRequest, WebDataBinderFactory webDataBinderFactory) {
		Class<?> parameterType = parameter.getParameterType();
		if (!parameterType.isAssignableFrom(List.class)) {
			throw new IllegalArgumentException(
					"Excel upload request resolver error, @RequestExcel parameter is not List " + parameterType);
		}

		// 处理自定义 readListener
		RequestExcel requestExcel = parameter.getParameterAnnotation(RequestExcel.class);
		assert requestExcel != null;
		Class<? extends ListAnalysisEventListener<?>> readListenerClass = requestExcel.readListener();
		ListAnalysisEventListener<?> readListener = BeanUtils.instantiateClass(readListenerClass);

		// 获取请求文件流
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		assert request != null;
		String fieldName = requestExcel.fileName();
		if (!StringUtils.hasText(fieldName)) {
			fieldName = Conventions.getVariableNameForParameter(parameter);
		}
		InputStream inputStream;
		if (request instanceof MultipartRequest) {
			MultipartFile file = ((MultipartRequest) request).getFile(fieldName);
			assert file != null;
			inputStream = file.getInputStream();
		}
		else {
			inputStream = request.getInputStream();
		}
		// 解析工作表名称
		String sheetName = resolverSheetName(request, parameter.getMethod(), requestExcel.sheetName());
		// 获取目标类型
		Class<?> excelModelClass = ResolvableType.forMethodParameter(parameter).getGeneric(0).resolve();

		// 这里需要指定读用哪个 class 去读，然后读取第一个 sheet 文件流会自动关闭
		FastExcel.read(inputStream, excelModelClass, readListener).registerConverter(LocalDateStringConverter.INSTANCE)
				.registerConverter(LocalDateTimeStringConverter.INSTANCE)
				.numRows(requestExcel.numRows() > 0 ? requestExcel.numRows() : null)
				.ignoreEmptyRow(requestExcel.ignoreEmptyRow()).sheet(sheetName)
				.headRowNumber(requestExcel.headRowNumber()).doRead();

		// 校验失败的数据处理 交给 BindResult
		WebDataBinder dataBinder = webDataBinderFactory.createBinder(webRequest, readListener.getErrors(), fieldName);
		ModelMap model = modelAndViewContainer.getModel();
		model.put(BindingResult.MODEL_KEY_PREFIX + fieldName, dataBinder.getBindingResult());

		return readListener.getList();
	}

	/**
	 * 解析Sheet名称 支持以下格式: 1. 固定名称 - 直接返回 2. 动态名称 - 使用SPEL表达式解析 3. 空名称 - 返回null
	 * @param request HttpServletRequest对象
	 * @param method 调用的方法对象
	 * @param sheetName 需要解析的Sheet名称
	 * @return 解析后的Sheet名称
	 */
	public String resolverSheetName(HttpServletRequest request, Method method, String sheetName) {
		if (!StringUtils.hasText(sheetName)) {
			return null;
		}

		if (!sheetName.contains("#")) {
			return sheetName;
		}
		String[] parameterNames = this.parameterNameDiscoverer.getParameterNames(method);
		StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
		if (parameterNames != null) {
			for (String name : parameterNames) {
				evaluationContext.setVariable(name, request.getParameter(name));
			}
		}
		Expression expression = this.expressionParser.parseExpression(sheetName);
		String value = expression.getValue(evaluationContext, String.class);
		return value == null || value.isEmpty() ? null : value;
	}

}
