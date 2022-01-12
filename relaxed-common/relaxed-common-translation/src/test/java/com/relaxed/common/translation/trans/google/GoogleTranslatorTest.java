package com.relaxed.common.translation.trans.google;

import com.relaxed.common.translation.core.TranslationResponse;
import com.relaxed.common.translation.core.Translator;
import com.relaxed.common.translation.enums.LangEnum;

import com.relaxed.common.translation.querier.Querier;
import com.relaxed.common.translation.trans.TransParam;
import com.relaxed.common.translation.trans.TransResponse;
import com.relaxed.common.translation.tts.TTSParam;
import com.relaxed.common.translation.tts.TTSResponse;
import com.relaxed.common.translation.tts.google.GoogleTTSRequest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Yakir
 * @Topic GoogleTranstorTest
 * @Description
 * @date 2022/1/11 17:11
 * @Version 1.0
 */
@SpringBootConfiguration
@SpringBootTest
class GoogleTranslatorTest {

	// @Test
	// public void testYoudao() {
	// Translator translator = new Translator();
	// YoudaoTranslationRequest youdaoTranslationRequest = new YoudaoTranslationRequest();
	// youdaoTranslationRequest.setFrom(LangEnum.EN);
	// youdaoTranslationRequest.setTo(LangEnum.ZH);
	// youdaoTranslationRequest.setText("Happiness is a way station between too much and
	// too little.");
	// YoudaoResponse result = translator.translate(youdaoTranslationRequest);
	// System.out.println(result);
	// //
	// // GoogleParamTTS googleParamTTS = new GoogleParamTTS();
	// // googleParamTTS.setFrom(LangEnum.EN);
	// // googleParamTTS.setText("To be or not to be, that is a question.");
	// //// googleParamTTS.setDownloadPath("D:\\google\\download");
	// // GoogleTTSResponse ttsResponse = transtor.translate(googleParamTTS);
	// // System.out.println(ttsResponse);
	//
	// }

	@Test
	public void testGoogle() {
		// //翻译器
		// Translator translator = new Translator();
		// //1.构造翻译参数
		// TransParam transParam = new TransParam();
		// transParam.setFrom(LangEnum.EN);
		// transParam.setTo(LangEnum.ZH);
		// transParam.setText("Happiness is a way station between too much and too
		// little.");
		// //2.构造翻译请求客户端
		// GoogleRequest googleRequest = new GoogleRequest();
		// googleRequest.setTranslationParam(transParam);
		// //3.翻译器执行翻译请求
		// TranslationResponse<TransResponse> result =
		// translator.translate(googleRequest);
		// System.out.println(result);
		// //1.构造文字转语音
		// TTSParam ttsParam = new TTSParam();
		// ttsParam.setFrom(LangEnum.EN);
		// ttsParam.setText("To be or not to be, that is a question.");
		// //2.构造翻译请求客户端
		// GoogleTTSRequest googleTtsRequest = new GoogleTTSRequest();
		// googleTtsRequest.setTranslationParam(ttsParam);
		// // googleParamTTS.setDownloadPath("D:\\google\\download");
		// //3.翻译器执行翻译
		// TranslationResponse<TTSResponse> ttsResponse =
		// translator.translate(googleTtsRequest);
		// System.out.println(ttsResponse);

		// 1.构建query实列
		TransParam buildTransParam = new TransParam().setFrom(LangEnum.EN).setTo(LangEnum.ZH).setText("hello world.");
		TranslationResponse<TransResponse> buildTransResult = Querier.trans().execute(buildTransParam);
		System.out.println(buildTransResult);

		TTSParam buildTTSParam = new TTSParam().setFrom(LangEnum.ZH).setText("您好啊，我是一个测试项目");
		TranslationResponse<TTSResponse> buildTTSResponse = Querier.tts().execute(buildTTSParam);
		System.out.println(buildTTSResponse);
	}

}