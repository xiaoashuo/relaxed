package com.relaxed.common.translation.trans.google;

import com.relaxed.common.translation.core.TranslationResponse;
import com.relaxed.common.translation.core.Translator;
import com.relaxed.common.translation.enums.LangEnum;


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

//	@Test
//	public void testYoudao() {
//		Translator translator = new Translator();
//		YoudaoTranslationRequest youdaoTranslationRequest = new YoudaoTranslationRequest();
//		youdaoTranslationRequest.setFrom(LangEnum.EN);
//		youdaoTranslationRequest.setTo(LangEnum.ZH);
//		youdaoTranslationRequest.setText("Happiness is a way station between too much and too little.");
//		YoudaoResponse result = translator.translate(youdaoTranslationRequest);
//		System.out.println(result);
//		//
//		// GoogleParamTTS googleParamTTS = new GoogleParamTTS();
//		// googleParamTTS.setFrom(LangEnum.EN);
//		// googleParamTTS.setText("To be or not to be, that is a question.");
//		//// googleParamTTS.setDownloadPath("D:\\google\\download");
//		// GoogleTTSResponse ttsResponse = transtor.translate(googleParamTTS);
//		// System.out.println(ttsResponse);
//
//	}

	@Test
	public void testGoogle() {
		Translator translator = new Translator();
		 GoogleRequest googleParam = new GoogleRequest();
		 googleParam.setFrom(LangEnum.EN);
		 googleParam.setTo(LangEnum.ZH);
		 googleParam.setText("Happiness is a way station between too much and too little.");


		TranslationResponse<GoogleRequest.Response> result = translator.translate(googleParam);
		System.out.println(result);

		GoogleTTSRequest googleTTSRequest = new GoogleTTSRequest();
		googleTTSRequest.setFrom(LangEnum.EN);
		googleTTSRequest.setText("To be or not to be, that is a question.");
		// googleParamTTS.setDownloadPath("D:\\google\\download");
		TranslationResponse<GoogleTTSRequest.Response> ttsResponse = translator.translate(googleTTSRequest);
		System.out.println(ttsResponse);

	}

}