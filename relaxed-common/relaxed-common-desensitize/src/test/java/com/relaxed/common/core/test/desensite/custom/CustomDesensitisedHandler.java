package com.relaxed.common.core.test.desensite.custom;

import com.relaxed.common.desensitize.handler.DesensitizationHandler;

public class CustomDesensitisedHandler implements DesensitizationHandler {

	public String handle(String text) {
		return "customer rule" + text;
	}

}
