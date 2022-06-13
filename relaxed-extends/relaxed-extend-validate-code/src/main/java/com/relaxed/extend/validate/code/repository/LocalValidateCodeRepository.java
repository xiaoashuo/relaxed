package com.relaxed.extend.validate.code.repository;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.date.DateUnit;
import com.relaxed.extend.validate.code.domain.ValidateCode;
import com.relaxed.extend.validate.code.domain.ValidateCodeType;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Yakir
 * @Topic LocalValidateCodeRespository
 * @Description
 * @date 2022/6/12 16:45
 * @Version 1.0
 */
public class LocalValidateCodeRepository implements ValidateCodeRepository {

	private final TimedCache<String, Object> timedCache = CacheUtil.newTimedCache(4);

	@Override
	public void save(HttpServletRequest request, ValidateCode code, ValidateCodeType validateCodeType,
			String codeKeyValue) {
		timedCache.put(buildKey(validateCodeType, codeKeyValue), code, DateUnit.MINUTE.getMillis() * 30);
	}

	@Override
	public ValidateCode get(HttpServletRequest request, ValidateCodeType validateCodeType, String codeKeyValue) {
		Object value = timedCache.get(buildKey(validateCodeType, codeKeyValue));
		return (ValidateCode) value;
	}

	@Override
	public void remove(HttpServletRequest request, ValidateCodeType codeType, String codeKeyValue) {
		timedCache.remove(buildKey(codeType, codeKeyValue));
	}

	/**
	 * @param type
	 * @param key
	 * @return
	 */
	private String buildKey(ValidateCodeType type, String key) {
		return "code:" + type.toString().toLowerCase() + ":" + key;
	}

}
