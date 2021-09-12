package com.relaxed.common.risk.engine.core.plugins.handler;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.*;
import com.relaxed.common.risk.engine.core.plugins.PluginEnum;
import com.relaxed.common.risk.engine.core.plugins.PluginMeta;
import com.relaxed.common.risk.engine.core.plugins.PluginService;
import com.relaxed.common.risk.model.vo.PreItemVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @author Yakir
 * @Topic HttpUtilPlugin
 * @Description
 * @date 2021/8/29 14:29
 * @Version 1.0
 */
@Slf4j
public class HttpUtilPlugin implements PluginService {

	@Override
	public PluginMeta pluginMeta() {
		return PluginEnum.HTTP_UTIL_PLUGIN;
	}

	@Override
	public String pluginName() {
		return "HttpUtil";
	}

	@Override
	public Object handle(PreItemVO preItemVO, Map<String, Object> jsonInfo, String[] sourceFields) {
		String url = preItemVO.getArgs();
		String reqType = preItemVO.getReqType();
		HttpRequest request = HttpUtil.createRequest(Method.valueOf(reqType), url);
		String params = StrUtil.format(preItemVO.getConfigJson(), sourceFields);
		try {
			String response = request.body(params).execute().body();
			log.info("http plugin:{}\n{}\n {}", url, sourceFields, response);
		}
		catch (Exception e) {
			log.error("http plugins request error url:{},params:{}", url, params, e);
		}
		return null;
	}

}
