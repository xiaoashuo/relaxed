package com.relaxed.common.risk.engine.rule;

import com.relaxed.common.risk.engine.rules.EvaluateContext;
import com.relaxed.common.risk.engine.rules.EvaluateReport;
import com.relaxed.common.risk.engine.rules.script.groovy.GroovyResult;
import groovy.lang.GroovyObject;

import com.relaxed.common.risk.engine.rules.script.RuleScriptHandler;
import com.relaxed.common.risk.engine.rules.script.groovy.GroovyParam;
import com.relaxed.common.risk.engine.rules.script.groovy.GroovyScriptHandler;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Yakir
 * @Topic ScriptTest
 * @Description
 * @date 2021/9/30 11:15
 * @Version 1.0
 */
public class ScriptTest {

	@Test
	public void testScript() {
		GroovyScriptHandler groovyScriptHandler = new GroovyScriptHandler();

		String ruleScript = "class ActivationCheckScript {" + " public boolean test (def context,def data,def lists) { "
				+ "def annexList=[ 1,2,7,9,12,20,21,24,25,35,36,37,38,42,45,54,69,74,76,77 ]; "
				+ "def annexParams=context.eventJson.annexPackagePath.split (\",\");" + " for (param in annexParams){ "
				+ "if (! annexList.contains (param as Integer)){" + " return true;" + " } }; return false;" + "" + "}}";
		EvaluateContext evaluateContext = new EvaluateContext();
		Map<String, String> maps = new HashMap<>();
		maps.put("annexPackagePath", "1,2,77");
		evaluateContext.setEventJson(maps);
		EvaluateReport evaluateReport = new EvaluateReport();
		GroovyResult groovyResult = groovyScriptHandler.invokeMethod(
				groovyScriptHandler.buildContext(ruleScript, "test", evaluateContext, evaluateReport, null));
		System.out.println(groovyResult);

	}

}
