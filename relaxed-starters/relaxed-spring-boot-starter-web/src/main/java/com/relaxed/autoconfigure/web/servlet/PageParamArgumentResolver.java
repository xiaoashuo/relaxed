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
 * 分页参数解析器
 *
 * @author Yakir
 * @since 2021/3/19
 */
@Slf4j
public class PageParamArgumentResolver implements HandlerMethodArgumentResolver {

	private static final Set<String> SQL_KEYWORDS = CollectionUtil.newHashSet("master", "truncate", "insert", "select",
			"delete", "update", "declare", "alter", "drop", "sleep");

	private static final String ASC = "asc";

	private final int pageSizeLimit;

	public PageParamArgumentResolver() {
		this(0);
	}

	public PageParamArgumentResolver(int pageSizeLimit) {
		this.pageSizeLimit = pageSizeLimit;
	}

	/**
	 * 判断Controller是否包含page 参数
	 * @param parameter 参数
	 * @return 是否过滤
	 */
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return PageParam.class.isAssignableFrom(parameter.getParameterType());
	}

	/**
	 * @param parameter 入参集合
	 * @param mavContainer model 和 view
	 * @param webRequest web相关
	 * @param binderFactory 入参解析
	 * @return 检查后新的page对象
	 * <p>
	 * page 只支持查询 GET .如需解析POST获取请求报文体处理
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
	 * 将前台侧排序字段 转为后台测
	 * @param pageParam 前台侧业务参数
	 * @return Map<String,Boolean> 参与排序字段
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
	 * 判断排序字段名是否非法 字段名只允许数字字母下划线，且不能是 sql 关键字
	 * @param filedName 字段名
	 * @return 是否非法
	 */
	public boolean validFieldName(String filedName) {
		boolean isValid = StrUtil.isNotBlank(filedName) && filedName.matches(PageParamRequest.SORT_FILED_REGEX)
				&& !SQL_KEYWORDS.contains(filedName);
		if (!isValid) {
			log.warn("异常的分页查询排序字段：{}", filedName);
		}
		return isValid;
	}

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

	protected boolean isBindExceptionRequired(WebDataBinder binder, MethodParameter parameter) {
		int i = parameter.getParameterIndex();
		Class<?>[] paramTypes = parameter.getExecutable().getParameterTypes();
		boolean hasBindingResult = (paramTypes.length > (i + 1) && Errors.class.isAssignableFrom(paramTypes[i + 1]));
		return !hasBindingResult;
	}

}
