package com.relaxed.autoconfigure.web.servlet;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.relaxed.common.core.exception.SqlCheckedException;
import com.relaxed.common.model.domain.PageParam;
import com.relaxed.common.model.domain.PageParamRequest;
import com.relaxed.common.model.result.BaseResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.PropertyValue;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.ValidationAnnotationUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.util.*;

/**
 * 分页参数解析器 用于解析Controller方法中的分页参数，支持分页查询和排序 提供SQL注入防护和参数校验功能
 *
 * @author Yakir
 * @since 1.0
 */
@Slf4j
public class PageParamArgumentResolver implements HandlerMethodArgumentResolver {

	/**
	 * SQL关键字集合，用于防止SQL注入
	 */
	private static final Set<String> SQL_KEYWORDS = CollectionUtil.newHashSet("master", "truncate", "insert", "select",
			"delete", "update", "declare", "alter", "drop", "sleep");

	/**
	 * 升序标识
	 */
	private static final String ASC = "asc";

	/**
	 * 分页大小限制
	 */
	private final int pageSizeLimit;

	/**
	 * 默认构造函数，不限制分页大小
	 */
	public PageParamArgumentResolver() {
		this(0);
	}

	/**
	 * 带分页大小限制的构造函数
	 * @param pageSizeLimit 分页大小限制
	 */
	public PageParamArgumentResolver(int pageSizeLimit) {
		this.pageSizeLimit = pageSizeLimit;
	}

	/**
	 * 判断是否支持当前参数类型 检查参数类型是否为PageParam或其子类
	 * @param parameter 方法参数
	 * @return 如果支持返回true，否则返回false
	 */
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return PageParam.class.isAssignableFrom(parameter.getParameterType());
	}

	/**
	 * 解析分页参数 从请求中提取分页参数，包括当前页、每页大小和排序信息 支持参数校验和SQL注入防护
	 * @param parameter 方法参数
	 * @param mavContainer 模型和视图容器
	 * @param webRequest Web请求
	 * @param binderFactory 数据绑定工厂
	 * @return 解析后的分页参数对象
	 * @throws Exception 如果解析过程中发生异常
	 */
	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

		PageParam pageParam;
		try {
			pageParam = (PageParam) parameter.getParameterType().newInstance();
		}
		catch (InstantiationException | IllegalAccessException e) {
			pageParam = new PageParam();
		}

		BeanWrapper pageParamBeanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(pageParam);
		pageParamBeanWrapper.setExtractOldValueForEditor(true);
		pageParamBeanWrapper.setAutoGrowNestedPaths(true);
		pageParamBeanWrapper.setAutoGrowCollectionLimit(256);
		WebDataBinder binder = null;
		if (binderFactory != null) {
			binder = binderFactory.createBinder(webRequest, pageParam, "pageParam");
			validateIfApplicable(binder, parameter);
			pageParamBeanWrapper.setConversionService(binder.getConversionService());
		}
		MutablePropertyValues pvs = new ServletRequestParameterPropertyValues(request);
		List<PropertyValue> pageParamPropertyValues = new ArrayList<>();
		List<PropertyValue> propertyValues = (pvs instanceof MutablePropertyValues ? pvs.getPropertyValueList()
				: Arrays.asList(pvs.getPropertyValues()));
		for (PropertyValue pv : propertyValues) {
			String name = pv.getName();
			if (name.equals("current") || name.equals("size")) {
				pageParamPropertyValues.add(pv);
			}
			else if (name.startsWith("sort")) {
				String value = (String) pv.getValue();
				// 若排序字段 或规则 有一个为空 则不参与排序
				if (StrUtil.isEmpty(name) || StrUtil.isEmpty(value)) {
					continue;
				}
				pageParamPropertyValues.add(new PropertyValue(name, ASC.equalsIgnoreCase(value)));
			}
		}
		for (PropertyValue pageParamPropertyValue : pageParamPropertyValues) {
			pageParamBeanWrapper.setPropertyValue(pageParamPropertyValue);
		}

		pageParam.setSort(convertToTargetFieldMap(pageParam));
		if (binder != null) {
			paramValidate(parameter, mavContainer, webRequest, binder, pageParam);
		}
		return pageParam;
	}

	/**
	 * 将排序字段转换为目标字段映射 将驼峰命名的字段转换为下划线命名，并进行SQL注入检查
	 * @param pageParam 分页参数对象
	 * @return 排序字段映射
	 */
	private Map<String, Boolean> convertToTargetFieldMap(PageParam pageParam) {
		Map<String, Boolean> sortFieldMap = pageParam.getSort();
		Map<String, Boolean> targetFieldMap = new HashMap<>();
		Set<Map.Entry<String, Boolean>> sourceSortFieldEntry = sortFieldMap.entrySet();
		for (Map.Entry<String, Boolean> entry : sourceSortFieldEntry) {
			String field = entry.getKey();
			Boolean value = entry.getValue();
			// 验证排序字段 是否符合规范
			if (validFieldName(field)) {
				targetFieldMap.put(StrUtil.toUnderlineCase(field), value);
			}
		}

		return targetFieldMap;
	}

	/**
	 * 验证字段名是否合法 检查字段名是否符合命名规范，且不是SQL关键字
	 * @param filedName 字段名
	 * @return 如果合法返回true，否则返回false
	 */
	public boolean validFieldName(String filedName) {
		boolean isValid = StrUtil.isNotBlank(filedName) && filedName.matches(PageParamRequest.SORT_FILED_REGEX)
				&& !SQL_KEYWORDS.contains(filedName);
		if (!isValid) {
			log.warn("异常的分页查询排序字段：{}", filedName);
		}
		return isValid;
	}

	/**
	 * 参数校验 检查分页大小是否超过限制
	 * @param parameter 方法参数
	 * @param mavContainer 模型和视图容器
	 * @param webRequest Web请求
	 * @param binder 数据绑定器
	 * @param pageParam 分页参数对象
	 * @throws Exception 如果校验失败
	 */
	protected void paramValidate(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinder binder, PageParam pageParam) throws Exception {
		// 数据校验处理

		BindingResult bindingResult = binder.getBindingResult();

		long size = pageParam.getSize();
		if (size > pageSizeLimit) {
			bindingResult.addError(new ObjectError("size", "分页条数不能大于" + pageSizeLimit));
		}

		if (bindingResult.hasErrors() && isBindExceptionRequired(binder, parameter)) {
			throw new MethodArgumentNotValidException(parameter, bindingResult);
		}
		if (mavContainer != null) {
			mavContainer.addAttribute(BindingResult.MODEL_KEY_PREFIX + "pageParam", bindingResult);
		}

	}

	/**
	 * 检查是否需要应用验证
	 * @param binder 数据绑定器
	 * @param parameter 方法参数
	 */
	protected void validateIfApplicable(WebDataBinder binder, MethodParameter parameter) {
		Annotation[] annotations = parameter.getParameterAnnotations();
		for (Annotation ann : annotations) {
			Object[] validationHints = ValidationAnnotationUtils.determineValidationHints(ann);
			if (validationHints != null) {
				binder.validate(validationHints);
				break;
			}
		}
	}

	/**
	 * 检查是否需要绑定异常
	 * @param binder 数据绑定器
	 * @param parameter 方法参数
	 * @return 如果需要绑定异常返回true，否则返回false
	 */
	protected boolean isBindExceptionRequired(WebDataBinder binder, MethodParameter parameter) {
		int i = parameter.getParameterIndex();
		Class<?>[] paramTypes = parameter.getExecutable().getParameterTypes();
		boolean hasBindingResult = (paramTypes.length > (i + 1) && Errors.class.isAssignableFrom(paramTypes[i + 1]));
		return !hasBindingResult;
	}

}
