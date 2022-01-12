package com.relaxed.common.translation.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Yakir
 * @Topic TransEnum
 * @Description
 * @date 2022/1/11 16:20
 * @Version 1.0
 */
@RequiredArgsConstructor
@Getter
public enum TransEnum {

	TTL("TTL", "文字翻译"), TTS("TTS", "文字翻译语音"),;

	private final String val;

	private final String desc;

}
