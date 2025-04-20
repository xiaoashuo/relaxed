package com.relaxed.common.translation.tts.google;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpUtil;
import com.relaxed.common.translation.core.AbstractTranslationRequest;

import com.relaxed.common.translation.core.TranslationResponse;

import com.relaxed.common.translation.enums.TransEnum;
import com.relaxed.common.translation.tookit.TokenUtils;
import com.relaxed.common.translation.tts.TTSParam;
import com.relaxed.common.translation.tts.TTSResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.File;
import java.util.Map;

@Data
public class GoogleTTSRequest extends AbstractTranslationRequest<TTSParam> {

	private static final String GOOGLE_URL = "https://translate.googleapis.com/translate_tts";

	@Override
	public String getUrl() {
		return GOOGLE_URL;
	}

	@Override
	public TransEnum tsType() {
		return TransEnum.TTS;
	}

	@Override
	public String put(Map<String, String> formData) {
		return URLUtil.buildQuery(formData, CharsetUtil.CHARSET_UTF_8);
	}

	@Override
	protected Map<String, String> fillFormData(Map<String, String> formData) {
		formData.put("ie", "UTF-8");
		formData.put("q", translationParam.getText());
		formData.put("tl", langMap.get(translationParam.getFrom()));
		formData.put("total", String.valueOf(1));
		formData.put("idx", String.valueOf(0));
		formData.put("textlen", String.valueOf(11));
		formData.put("tk", TokenUtils.token("tk/Google.js", translationParam.getText()));
		formData.put("client", "t");
		return formData;
	}

	@Override
	public <T> TranslationResponse<T> execute(String params) {
		String url = this.getUrl();
		String urlString = url + "?" + params;
		// 将 TTS 结果保存为 mp3 音频文件，以待转换文本的 md5 码作为部分文件名
		StringBuilder saveFile = new StringBuilder();
		String filename = SecureUtil.md5(urlString) + ".mp3";
		saveFile.append(translationParam.getDownloadPath()).append(File.separator).append(filename);
		String destPath = saveFile.toString();
		HttpUtil.downloadFile(urlString, destPath);
		return TranslationResponse.ok(new TTSResponse(filename, destPath));
	}

}
