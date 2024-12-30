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
 * 上传excel 解析注解
 *
 * @author lengleng
 * @author L.cm
 * @date 2021/4/16
 */
@Slf4j
public class RequestExcelArgumentResolver implements HandlerMethodArgumentResolver {

	private final ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

	private final ExpressionParser expressionParser = new SpelExpressionParser();

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(RequestExcel.class);
	}

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
	 * 解析Sheet名称
	 * @param request HttpServletRequest对象，用于获取请求参数
	 * @param method 调用的方法对象，用于获取方法参数名称
	 * @param sheetName 需要解析的Sheet名称
	 * @return 解析后的Sheet名称，如果为空则返回null
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
