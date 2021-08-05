package com.relaxed.common.conf.web;

import cn.hutool.core.util.StrUtil;
import com.relaxed.common.core.exception.SqlCheckedException;
import com.relaxed.common.model.result.BaseResultCode;

import com.relaxed.common.model.domain.PageParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页参数解析器
 *
 * @author Yakir
 * @since 2021/3/19
 */
@Slf4j
public class PageParamArgumentResolver implements HandlerMethodArgumentResolver {

	private static final String[] KEYWORDS = { "master", "truncate", "insert", "select", "delete", "update", "declare",
			"alter", "drop", "sleep" };

	private static final String FILED_NAME_REGEX = "[A-Za-z0-9_]+";

	private static final String ASC = "asc";

	/**
	 * 判断控制器支持的参数类型
	 * @param methodParameter
	 * @return 是否处理
	 */
	@Override
	public boolean supportsParameter(MethodParameter methodParameter) {
		return methodParameter.getParameterType().equals(PageParam.class);
	}

	@Override
	public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer,
			NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
		HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
		String current = request.getParameter("current");
		String size = request.getParameter("size");
		String sortFields = request.getParameter("sortFields");
		String sortOrders = request.getParameter("sortOrders");
		PageParam pageParam = new PageParam();
		if (StrUtil.isNotBlank(current)) {
			pageParam.setCurrent(Long.parseLong(current));
		}
		if (StrUtil.isNotBlank(size)) {
			pageParam.setSize(Long.parseLong(size));
		}

		List<PageParam.Sort> sorts = getOrderItems(sortFields, sortOrders);
		pageParam.setSorts(sorts);
		return pageParam;
	}

	/**
	 * 封装排序规则
	 * @param sortFields 排序字段，使用英文逗号分割
	 * @param sortOrders 排序规则，使用英文逗号分割，与排序字段一一对应
	 * @return List<PageParam.OrderItem>
	 */
	protected List<PageParam.Sort> getOrderItems(String sortFields, String sortOrders) {
		List<PageParam.Sort> sorts = new ArrayList<>();

		// 字段和规则都不能为空
		if (StrUtil.isBlank(sortFields) || StrUtil.isBlank(sortOrders)) {
			return sorts;
		}

		// 字段和规则不一一对应则不处理
		String[] fieldArr = sortFields.split(StrUtil.COMMA);
		String[] orderArr = sortOrders.split(StrUtil.COMMA);
		if (fieldArr.length != orderArr.length) {
			return sorts;
		}

		String field;
		String order;
		for (int i = 0; i < fieldArr.length; i++) {
			field = fieldArr[i];
			order = orderArr[i];
			if (validFieldName(field)) {
				PageParam.Sort sort = new PageParam.Sort();
				// 驼峰转下划线
				sort.setAsc(ASC.equalsIgnoreCase(order));
				// 正序/倒序
				sort.setField(StrUtil.toUnderlineCase(field));
				sorts.add(sort);
			}
		}

		return sorts;
	}

	/**
	 * 判断排序字段名是否非法 字段名只允许数字字母下划线
	 * @param filedName 字段名
	 * @return 是否非法
	 */
	public static boolean validFieldName(String filedName) {
		return StrUtil.isNotBlank(filedName) && filedName.matches(FILED_NAME_REGEX);
	}

	/**
	 * SQL注入过滤
	 * @param str 待验证的字符串
	 */
	public static void sqlInject(String str) {
		if (StrUtil.isEmpty(str)) {
			return;
		}
		// 转换成小写
		String inStr = str.toLowerCase();

		// 判断是否包含非法字符
		for (String keyword : KEYWORDS) {
			if (inStr.contains(keyword)) {
				log.error("查询包含非法字符 {}", keyword);
				throw new SqlCheckedException(BaseResultCode.MALICIOUS_REQUEST.getCode(), "恶意请求参数：" + keyword);
			}
		}
	}

}
