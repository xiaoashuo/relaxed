package com.relaxed.common.translation.tts;

import com.relaxed.common.translation.core.TranslationParam;
import com.relaxed.common.translation.enums.LangEnum;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class TTSParam implements TranslationParam {

	private LangEnum from;

	private String text;

	/**
	 * 下载路径
	 */
	private String downloadPath = "./tts";

}
