package com.relaxed.extend.validate.code;

import com.relaxed.extend.validate.code.processor.ValidateCodeProcessorHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Yakir
 * @Topic ValidateCodeFilter
 * @Description
 * @date 2022/6/12 15:49
 * @Version 1.0
 */
@Slf4j
@RequiredArgsConstructor
public class ValidateCodeFilter extends OncePerRequestFilter {

	private final ValidateCodeProcessorHolder V_CODE_HOLDER;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		log.info("开始走入");
		// 判断当前这个请求是否需要验证，并且验证请求中携带的验证码
		V_CODE_HOLDER.validateCode(request, response);
		// 效验是否为生成请求,是则生成验证码
		V_CODE_HOLDER.generatorCode(request, response);
		filterChain.doFilter(request, response);
	}

}
