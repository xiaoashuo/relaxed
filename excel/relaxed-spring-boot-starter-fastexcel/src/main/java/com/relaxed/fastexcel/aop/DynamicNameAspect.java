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
 * @author lengleng
 * @date 2020/3/29
 */
@Aspect
@RequiredArgsConstructor
public class DynamicNameAspect {

	public static final String EXCEL_NAME_KEY = "__EXCEL_NAME_KEY__";

	private final NameProcessor processor;

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
