package com.relaxed.common.translation.trans.google;

import com.relaxed.common.translation.core.TranslationParam;
import com.relaxed.common.translation.enums.LangEnum;
import lombok.Data;

/**
 * @author Yakir
 * @Topic GoogleParam
 * @Description
 * @date 2022/1/12 14:55
 * @Version 1.0
 */
@Data
public class GoogleParam implements TranslationParam {
    private LangEnum from;

    private LangEnum to;

    private String text;
}
