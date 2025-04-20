package com.relaxed.common.translation.querier;

import com.relaxed.common.translation.core.*;
import com.relaxed.common.translation.enums.LangEnum;
import com.relaxed.common.translation.trans.google.GoogleRequest;
import com.relaxed.common.translation.trans.tencent.TencentRequest;
import com.relaxed.common.translation.tts.google.GoogleTTSRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * 聚合多个翻译器
 *
 * @author Yakir
 */
public class Querier<T extends AbstractTranslationRequest> {

	private List<T> collection = new ArrayList<>();

	private Translator translator = new Translator();

	private Querier() {

	}

	/**
	 * 构建文字翻译服务
	 * @param <T> 翻译对象执行器
	 * @return {@link com.relaxed.common.translation.querier.Querier}
	 */
	public static <T extends AbstractTranslationRequest> Querier<T> trans() {
		Querier querier = new Querier();
		querier.attach(new GoogleRequest());
		querier.attach(new TencentRequest());
		return querier;
	}

	/**
	 * 构建文字转语音查询器
	 * @param <T> 翻译器对象
	 * @return com.relaxed.common.translation.querier.Querier
	 */
	public static <T extends AbstractTranslationRequest> Querier<T> tts() {
		Querier querier = new Querier();
		querier.attach(new GoogleTTSRequest());
		return querier;
	}

	/**
	 * 执行翻译命令
	 * @param <P> 泛型参数
	 * @param param 实体对象
	 * @param <R> 响应
	 * @return {@link com.relaxed.common.translation.core.TranslationResponse}
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
	 * @param command 客户端端对象
	 */
	public void attach(T command) {
		collection.add(command);
	}

	public void detach(T command) {
		collection.remove(command);
	}

}
