package com.relaxed.common.translation.trans;

import com.relaxed.common.translation.core.TranslationParam;
import com.relaxed.common.translation.enums.LangEnum;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * GoogleParam
 *
 * @author Yakir
 */
@Accessors(chain = true)
@Data
public class TransParam implements TranslationParam {

	private LangEnum from;

	private LangEnum to;

	private String text;

}
