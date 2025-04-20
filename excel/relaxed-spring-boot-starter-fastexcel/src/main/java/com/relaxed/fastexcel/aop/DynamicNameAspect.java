package com.relaxed.fastexcel.aop;

import com.relaxed.fastexcel.annotation.ResponseExcel;
import com.relaxed.fastexcel.processor.NameProcessor;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Excel动态名称切面 用于处理Excel导出时的动态文件名生成 主要功能: 1. 支持自定义Excel文件名 2. 支持动态表达式解析文件名 3. 支持默认时间戳文件名
 * 4. 支持请求上下文存储文件名
 *
 * @author lengleng
 * @since 1.0.0
 */
@Aspect
@RequiredArgsConstructor
public class DynamicNameAspect {

	/**
	 * Excel文件名在请求上下文中的键名
	 */
	public static final String EXCEL_NAME_KEY = "__EXCEL_NAME_KEY__";

	/**
	 * 名称处理器,用于解析动态文件名表达式
	 */
	private final NameProcessor processor;

	/**
	 * 在方法执行前处理Excel文件名 1. 如果配置了固定名称,直接使用 2. 如果名称为空,使用当前时间戳 3. 如果包含动态表达式,通过处理器解析 4.
	 * 将最终文件名存储到请求上下文中
	 * @param point 连接点
	 * @param excel ResponseExcel注解
	 */
	@Before("@annotation(excel)")
	public void before(JoinPoint point, ResponseExcel excel) {
		MethodSignature ms = (MethodSignature) point.getSignature();

		String name = excel.name();
		// 当配置的 excel 名称为空时，取当前时间
		if (!StringUtils.hasText(name)) {
			name = LocalDateTime.now().toString();
		}
		else {
			name = processor.doDetermineName(point.getArgs(), ms.getMethod(), excel.name());
		}

		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		Objects.requireNonNull(requestAttributes).setAttribute(EXCEL_NAME_KEY, name, RequestAttributes.SCOPE_REQUEST);
	}

}
