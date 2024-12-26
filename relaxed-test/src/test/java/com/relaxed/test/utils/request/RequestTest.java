package com.relaxed.test.utils.request;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Editor;
import cn.hutool.http.Method;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Yakir
 * @Topic RequestTest
 * @Description
 * @date 2024/12/26 16:20
 * @Version 1.0
 */
@Slf4j
public class RequestTest {

	public static void main(String[] args) {
		UserReq userReq = new UserReq();
		userReq.setUsername("张三");
		userReq.setExtParam(new HashMap<>());

		UserRet userRet = syncCustInfo(userReq);
		log.info("响应结果:{}", userRet);
	}

	public static UserRet syncCustInfo(UserReq userReq) {
		String requestUrl = "https://www.baidu.com";

		Set<String> propertiesSet = new HashSet<>();
		propertiesSet.add("extParam");
		Editor<String> keyEditor = property -> !propertiesSet.contains(property) ? property : null;
		Map<String, Object> paramMap = BeanUtil.beanToMap(userReq, new LinkedHashMap<>(16, 1), false, keyEditor);

		Map<String, Object> extParam = userReq.getExtParam();
		paramMap.putAll(extParam);
		String jsonStr = JSONUtil.toJsonStr(paramMap);
		RequestFactory.PostCheck postCheck = (factory, result) -> {
			if (!JSONUtil.isTypeJSON(result)) {
				return true;
			}
			return false;
		};
		UserRet userRet = RequestFactory.create(requestUrl, Method.POST).desc("同步客户信息").body(jsonStr).config()
				.readTimeout(60000).end().wrapExecute(result -> {
					Assert.hasText(result, "访问同步客户信息接口返回数据为空");
					return JSONUtil.toBean(result, UserRet.class);
				}, postCheck);
		return userRet;
	}

}
