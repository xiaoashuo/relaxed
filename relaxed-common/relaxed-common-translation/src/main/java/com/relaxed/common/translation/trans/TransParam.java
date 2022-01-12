package com.relaxed.common.translation.trans;

import com.relaxed.common.translation.core.TranslationParam;
import com.relaxed.common.translation.enums.LangEnum;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Yakir
 * @Topic GoogleParam
 * @Description
 * @date 2022/1/12 14:55
 * @Version 1.0
 */
@Accessors(chain = true)
@Data
public class TransParam implements TranslationParam {

	private LangEnum from;

	private LangEnum to;

	private String text;

}
