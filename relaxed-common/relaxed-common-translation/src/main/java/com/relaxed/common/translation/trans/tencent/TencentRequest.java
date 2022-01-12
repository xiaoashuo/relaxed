package com.relaxed.common.translation.trans.tencent;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.relaxed.common.translation.core.AbstractTranslationRequest;
import com.relaxed.common.translation.core.TranslationResponse;
import com.relaxed.common.translation.enums.LangEnum;
import com.relaxed.common.translation.enums.TransEnum;
import com.relaxed.common.translation.tookit.TokenUtils;
import com.relaxed.common.translation.trans.TransParam;
import com.relaxed.common.translation.trans.TransResponse;
import lombok.Data;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Yakir
 * @Topic TencentRequest
 * @Description
 * @date 2022/1/11 16:31
 * @Version 1.0
 */
@Data
public class TencentRequest extends AbstractTranslationRequest<TransParam> {

	private static final String GOOGLE_URL = "https://fanyi.qq.com/api/translate";

	protected Map<String, String> formData = new HashMap<>();

	@Override
	public String getUrl() {
		return GOOGLE_URL;
	}

	@Override
	public TransEnum tsType() {
		return TransEnum.TTL;
	}

	@Override
	public String put(Map<String, String> formData) {
		return URLUtil.buildQuery(formData, CharsetUtil.CHARSET_UTF_8);
	}

	@Override
	protected void initDefaultLangSupport() {
		langMap.put(LangEnum.ZH, "zh");
		langMap.put(LangEnum.EN, "en");
		langMap.put(LangEnum.JP, "jp");
		langMap.put(LangEnum.KOR, "kr");
		langMap.put(LangEnum.FRA, "fr");
		langMap.put(LangEnum.RU, "ru");
		langMap.put(LangEnum.DE, "de");
	}

	@Override
	protected Map<String, String> fillFormData(Map<String, String> formData) {
		String text = translationParam.getText();
		LangEnum from = translationParam.getFrom();
		LangEnum to = translationParam.getTo();
		formData.put("source", langMap.get(from));
		formData.put("target", langMap.get(to));
		formData.put("sourceText", text);
		formData.put("sessionUuid", "translate_uuid" + System.currentTimeMillis());
		return formData;
	}

	@Override
	public <T> TranslationResponse<T> execute(String param) {
		String url = this.getUrl();

		HttpRequest httpRequest = HttpUtil.createPost(url);
		httpRequest.header("Origin", "https://fanyi.qq.com");
		httpRequest.header("Cookie",
				"pgv_pvid=767342458; pac_uid=0_6f0ef13264e20; fy_guid=f723499b-16ad-48da-b144-6637c1be2b4e; ADHOC_MEMBERSHIP_CLIENT_ID1.0=7e20e661-e7ef-8eca-5e6a-3f3dc383ec6a; openCount=1; qtv=cade9ddc0b49b29b; qtk=ByVDELJQjDTjqgQwKHyRQUYr/K0YfdreUg8mq5Xm7c3ge1XzCmiI6m8yS6AsH3LJwjckKt6jqG4rbOjn0xIPbdLayhuBpcY7/pwxoRwtZC0I7VhR83lX+MFOQlNSKoqdq/R0BHf3dnDZommFrH+Ptg==; gr_user_id=b292abdc-a7c9-41e0-b3f2-009795c4de2c; 8507d3409e6fad23_gr_session_id=09c9a463-06f7-4f73-8918-eaf15b2ddf42; 8507d3409e6fad23_gr_session_id_09c9a463-06f7-4f73-8918-eaf15b2ddf42=true");
		httpRequest.body(param);
		String result = httpRequest.execute().body();
		String realResult = parseResult(result);
		return TranslationResponse.ok(new TransResponse(realResult));
	}

	@SneakyThrows
	private static String parseResult(String text) {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readTree(text).path("translate").findPath("targetText").toString();
	}

}
