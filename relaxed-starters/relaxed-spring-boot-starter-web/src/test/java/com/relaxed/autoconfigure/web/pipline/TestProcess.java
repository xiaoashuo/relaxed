package com.relaxed.autoconfigure.web.pipline;

import cn.hutool.core.util.IdUtil;
import com.relaxed.common.core.pipeline.*;
import com.relaxed.common.model.result.R;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Yakir
 * @Topic TestProcess
 * @Description
 * @date 2022/2/10 10:11
 * @Version 1.0
 */
@Slf4j
public class TestProcess {

	static ProcessExecutor processExecutor;
	static {
		ProcessTemplate processTemplate = getStampProcessTemplate();
		Map<String, ProcessTemplate> templateConfig = new HashMap<>(4);

		templateConfig.put("ts", processTemplate);
		processExecutor = new ProcessExecutor(templateConfig);
	}

	public static void main(String[] args) {
		ProcessContextModel processContextModel = new ProcessContextModel();
		processContextModel.setStage(1);
		// processContextModel.setData("签署");

		ProcessContext processContext = ProcessContext.builder().processId(IdUtil.simpleUUID())
				.processModel(processContextModel).channel("ts").needBreak(false).response(R.ok("流程开始")).build();

		processExecutor.process(processContext, new PostProcessor() {
			@Override
			public void postProcess(ProcessContext processContext) {
				log.info("流程处理结束{}", processContext);
			}
		});

	}

	/**
	 * 顺序处理 1.上传处理器 2.签署处理器 3.下载处理器
	 * @return
	 */
	private static ProcessTemplate getStampProcessTemplate() {
		ProcessTemplate processTemplate = new ProcessTemplate();

		ArrayList<BusinessProcess> processList = new ArrayList<>();
		processList.add(new UploadProcess());
		processList.add(new SignProcess());
		processList.add(new DownProcess());
		processTemplate.setProcessList(processList);
		return processTemplate;
	}

}
