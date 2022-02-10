package com.relaxed.autoconfigure.web.pipline;

import com.relaxed.common.core.pipeline.BusinessProcess;
import com.relaxed.common.core.pipeline.ProcessContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Yakir
 * @Topic UploadProcess
 * @Description
 * @date 2022/2/10 10:08
 * @Version 1.0
 */
@Slf4j
public class DownProcess implements BusinessProcess {

	@Override
	public boolean support(ProcessContext context) {
		ProcessContextModel processContextModel = (ProcessContextModel) context.getProcessModel();
		Integer stage = processContextModel.getStage();
		return stage.equals(3);
	}

	@Override
	public void process(ProcessContext context) {
		log.info("下载处理器开始{}", context);
		ProcessContextModel processContextModel = (ProcessContextModel) context.getProcessModel();
		processContextModel.setStage(4);
		processContextModel.setData("下载");
		log.info("下载处理器结束{}", context);
	}

}
