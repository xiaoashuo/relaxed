package com.relaxed.extend.validate.code.processor;

import cn.hutool.core.util.StrUtil;

import com.relaxed.extend.validate.code.ValidateCodeException;
import com.relaxed.extend.validate.code.domain.CodeProperties;
import com.relaxed.extend.validate.code.domain.ValidateCode;
import com.relaxed.extend.validate.code.domain.ValidateCodeType;
import com.relaxed.extend.validate.code.generator.ValidateCodeGenerator;
import com.relaxed.extend.validate.code.handler.ValidateCodeHandler;
import com.relaxed.extend.validate.code.repository.ValidateCodeRepository;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

/**
 * @author Yakir
 * @Topic AbstractValidateCodeProcessor
 * @Description
 * @date 2022/6/12 15:53
 * @Version 1.0
 */
public abstract class AbstractValidateCodeProcessor<C extends ValidateCode> implements ValidateCodeProcessor {

	/** 标记验证码的唯一key */
	protected static final String DEFAULT_CODE_KEY = "code_key";

	private static final AntPathMatcher MATCHER = new AntPathMatcher();

	/** 发送验证码前需要调用的操作 */
	private final List<ValidateCodeHandler> handlerList;

	/** 用作生成验证码 */
	private final ValidateCodeGenerator validateCodeGenerator;

	/** 用作存取验证码 */
	private final ValidateCodeRepository validateCodeRepository;

	/** 用作获取验证码相关系统配置 */
	private final CodeProperties codeProperties;

	public AbstractValidateCodeProcessor(ValidateCodeGenerator validateCodeGenerator,
			ValidateCodeRepository validateCodeRepository, CodeProperties codeProperties) {
		this(validateCodeGenerator, validateCodeRepository, codeProperties, null);
	}

	/** 构造函数 */
	public AbstractValidateCodeProcessor(ValidateCodeGenerator validateCodeGenerator,
			ValidateCodeRepository validateCodeRepository, CodeProperties codeProperties,
			List<ValidateCodeHandler> handlerList) {
		this.validateCodeGenerator = validateCodeGenerator;
		this.validateCodeRepository = validateCodeRepository;
		this.codeProperties = codeProperties;
		this.handlerList = handlerList;
	}

	/** 生成验证码逻辑 */
	@Override
	public void create(HttpServletRequest request, HttpServletResponse response) throws ValidateCodeException {
		// 生成指定验证码
		C validateCode = generate(request);
		ValidateCodeType codeType = getValidateCodeType();
		// 检查是否需要在发送该验证码之前执行一些指定的操作；比如注册的时候验证一下手机号码是否已经被注册；
		if (!CollectionUtils.isEmpty(handlerList)) {
			for (ValidateCodeHandler handler : handlerList) {
				if (handler.support(request, codeType)) {
					handler.beforeSend(request, codeType, validateCode);
				}
			}
		}
		// 用作保存验证码的key，方便后续的验证操作
		String codeKeyValue = request.getSession().getId();
		response.setHeader(this.getCodeKey(), codeKeyValue);
		// 保存验证码数据
		save(request, validateCode, codeKeyValue);
		// 发送验证码
		send(request, response, validateCode);
	}

	/**
	 * 保存验证码
	 * @param request
	 * @param validateCode
	 */
	private void save(HttpServletRequest request, C validateCode, String codeKeyValue) {
		validateCodeRepository.save(request, validateCode, getValidateCodeType(), codeKeyValue);
	}

	/**
	 * 获取ValidateCodeType
	 * @return
	 */
	protected abstract ValidateCodeType getValidateCodeType();

	/**
	 * 验证码发送
	 * @param request
	 * @param validateCode
	 * @throws Exception
	 */
	protected abstract void send(HttpServletRequest request, HttpServletResponse response, C validateCode)
			throws ValidateCodeException;

	/**
	 * 创建验证码
	 * @param request
	 * @return
	 */
	private C generate(HttpServletRequest request) {
		return (C) validateCodeGenerator.createValidateCode(request);
	}

	private String getCodeKeyValue(HttpServletRequest request) throws ServletRequestBindingException {
		// 从请求头或者参数中获取用户输入的验证码
		String codeKeyValue = request.getHeader(getCodeKey());
		codeKeyValue = StrUtil.isBlank(codeKeyValue) ? ServletRequestUtils.getStringParameter(request, getCodeKey())
				: codeKeyValue;
		return codeKeyValue;
	}

	/**
	 * 校验验证码
	 * @param request
	 * @return
	 * @throws ValidateCodeException
	 */
	@Override
	public void validate(HttpServletRequest request) throws ValidateCodeException {
		// 获取验证码类型
		ValidateCodeType codeType = getValidateCodeType();
		C codeInSession;
		String codeKeyValue;
		String codeInRequest;
		try {
			// 取出验证码的key
			codeKeyValue = getCodeKeyValue(request);
			// 使用codeKeyValue取出保存在后台验证码数据
			codeInSession = (C) validateCodeRepository.get(request, codeType, codeKeyValue);
			// 获取请求中用户输入的验证码
			codeInRequest = ServletRequestUtils.getStringParameter(request, codeType.getParamNameOnValidate());
		}
		catch (Exception e) {
			throw new ValidateCodeException("VALIDATE_CODE_OBTAIN_ERROR");
		}
		if (StrUtil.isBlank(codeInRequest)) {
			throw new ValidateCodeException("VALIDATE_CODE_EMPTY_ERROR");
		}
		if (Objects.isNull(codeInSession) || Objects.isNull(codeInSession.getCode())) {
			throw new ValidateCodeException("VALIDATE_CODE_VALIDATE_ERROR");
		}
		if (codeInSession.isExpired()) {
			validateCodeRepository.remove(request, codeType, codeKeyValue);
			throw new ValidateCodeException("VALIDATE_CODE_VALIDATE_ERROR");
		}
		if (!validate(codeInRequest, codeInSession)) {
			throw new ValidateCodeException("VALIDATE_CODE_VALIDATE_ERROR");
		}
		// 验证成功后移除保存的数据
		validateCodeRepository.remove(request, codeType, codeKeyValue);
	}

	/**
	 * 验证
	 * @param code
	 * @return
	 */
	protected abstract boolean validate(String code, C validateCode);

	@Override
	public boolean isGenerateRequest(String targetUrl) {
		return StrUtil.equalsIgnoreCase(targetUrl, this.codeProperties.getGeneratorUrl());
	}

	/**
	 * 获取验证码的key前缀
	 * @author yakir
	 * @date 2022/6/12 19:31
	 * @return java.lang.String
	 */
	public String getCodeKey() {
		return DEFAULT_CODE_KEY;
	}

	@Override
	public boolean isValidateRequest(String targetUrl) {
		String[] filterUrls = this.codeProperties.getFilterUrls();
		for (String url : filterUrls) {
			if (MATCHER.match(url, targetUrl)) {
				return true;
			}
		}
		return false;
	}

}
