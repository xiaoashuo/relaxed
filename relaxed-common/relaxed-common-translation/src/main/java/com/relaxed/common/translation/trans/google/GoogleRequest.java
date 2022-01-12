package com.relaxed.common.translation.trans.google;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import com.relaxed.common.translation.core.AbstractTranslationRequest;

import com.relaxed.common.translation.core.TranslationResponse;

import com.relaxed.common.translation.enums.TransEnum;

import com.relaxed.common.translation.tookit.TokenUtils;
import com.relaxed.common.translation.trans.TransParam;
import com.relaxed.common.translation.trans.TransResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Yakir
 * @Topic GoogleParam
 * @Description
 * @date 2022/1/11 16:31
 * @Version 1.0
 */
@Data
public class GoogleRequest extends AbstractTranslationRequest<TransParam> {

	private static final String GOOGLE_URL = "https://translate.googleapis.com/translate_a/single";

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
	protected Map<String, String> fillFormData(Map<String, String> formData) {
		String text = translationParam.getText();
		formData.put("client", "t");
		formData.put("sl", langMap.get(translationParam.getFrom()));
		formData.put("tl", langMap.get(translationParam.getTo()));
		formData.put("hl", "zh-CN");
		formData.put("dt", "at");
		formData.put("dt", "bd");
		formData.put("dt", "ex");
		formData.put("dt", "ld");
		formData.put("dt", "md");
		formData.put("dt", "qca");
		formData.put("dt", "rw");
		formData.put("dt", "rm");
		formData.put("dt", "ss");
		formData.put("dt", "t");
		formData.put("ie", "UTF-8");
		formData.put("oe", "UTF-8");
		formData.put("source", "btn");
		formData.put("ssel", "0");
		formData.put("tsel", "0");
		formData.put("kc", "0");
		formData.put("tk", TokenUtils.token("tk/Google.js", text));
		formData.put("q", text);
		return formData;
	}

	@Override
	public <T> TranslationResponse<T> execute(String param) {
		String url = this.getUrl();
		String urlString = url + "?" + param;
		String result = HttpUtil.get(urlString);
		String realResult = parseResult(result);
		return TranslationResponse.ok(new TransResponse(realResult));
	}

	private static String parseResult(String inputJson) {
		JSONArray jsonArray2 = (JSONArray) new JSONArray(inputJson).get(0);
		StringBuilder result = new StringBuilder();
		for (Object o : jsonArray2) {
			result.append(((JSONArray) o).get(0).toString());
		}
		return result.toString();
	}

}
