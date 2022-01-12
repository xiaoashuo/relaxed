package com.relaxed.common.translation.querier;

import com.relaxed.common.translation.core.*;
import com.relaxed.common.translation.enums.LangEnum;
import com.relaxed.common.translation.trans.google.GoogleRequest;
import com.relaxed.common.translation.tts.google.GoogleTTSRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yakir
 * @Topic Querier
 * @Description 聚合多个翻译器
 * @date 2022/1/11 16:17
 * @Version 1.0
 */
public class Querier<T extends AbstractTranslationRequest> {

	private List<T> collection = new ArrayList<>();

	private Translator translator = new Translator();

	private Querier() {

	}

	/**
	 * 构建文字翻译服务
	 * @author yakir
	 * @date 2022/1/12 15:55
	 * @return com.relaxed.common.translation.querier.Querier<T>
	 */
	public static <T extends AbstractTranslationRequest> Querier<T> trans() {
		Querier querier = new Querier();
		querier.attach(new GoogleRequest());
		return querier;
	}

	/**
	 * 构建文字转语音查询器
	 * @author yakir
	 * @date 2022/1/12 15:55
	 * @return com.relaxed.common.translation.querier.Querier<T>
	 */
	public static <T extends AbstractTranslationRequest> Querier<T> tts() {
		Querier querier = new Querier();
		querier.attach(new GoogleTTSRequest());
		return querier;
	}

	/**
	 * 执行翻译命令
	 * @author yakir
	 * @date 2022/1/12 15:55
	 * @param param
	 * @return com.relaxed.common.translation.core.TranslationResponse<R>
	 */
	public <P extends TranslationParam, R> TranslationResponse<R> execute(P param) {
		for (T command : collection) {
			command.setTranslationParam(param);
			TranslationResponse<R> translate = translator.translate(command);
			if (translate.success()) {
				return translate;
			}
		}
		return null;
	}

	/**
	 * 添加请求客户端
	 * @author yakir
	 * @date 2022/1/12 15:56
	 * @param command
	 */
	public void attach(T command) {
		collection.add(command);
	}

	public void detach(T command) {
		collection.remove(command);
	}

}
