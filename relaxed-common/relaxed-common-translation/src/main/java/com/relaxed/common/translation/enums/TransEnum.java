package com.relaxed.common.translation.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TransEnum {

	TTL("TTL", "文字翻译"), TTS("TTS", "文字翻译语音"),;

	private final String val;

	private final String desc;

}
