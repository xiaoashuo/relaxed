package com.relaxed.common.translation.tookit;

import cn.hutool.core.io.resource.ResourceUtil;
import lombok.experimental.UtilityClass;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.BufferedReader;

/**
 * @author Yakir
 * @Topic TokenUtils
 * @Description
 * @date 2022/1/12 10:35
 * @Version 1.0
 */
@UtilityClass
public class TokenUtils {

	/**
	 * 获取token
	 * @author yakir
	 * @date 2022/1/12 10:36
	 * @param classFilePath js文件路径相对于classpath
	 * @param args 参数
	 * @return java.lang.String
	 */
	public String token(String classFilePath, Object... args) {
		String tk = "";
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("js");
		try {
			BufferedReader reader = ResourceUtil.getUtf8Reader(classFilePath);
			engine.eval(reader);

			if (engine instanceof Invocable) {
				Invocable invoke = (Invocable) engine;
				tk = String.valueOf(invoke.invokeFunction("token", args));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return tk;
	}

}
