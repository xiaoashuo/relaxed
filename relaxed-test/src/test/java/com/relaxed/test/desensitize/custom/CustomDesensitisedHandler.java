package com.relaxed.test.desensitize.custom;

import com.relaxed.common.desensitize.handler.DesensitizationHandler;

public class CustomDesensitisedHandler implements DesensitizationHandler {

	public String handle(String text) {
		return "customer rule" + text;
	}

}
