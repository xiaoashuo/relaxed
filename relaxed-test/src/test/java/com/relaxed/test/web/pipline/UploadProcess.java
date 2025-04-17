package com.relaxed.test.web.pipline;

import com.relaxed.common.core.pipeline.BusinessProcess;
import com.relaxed.common.core.pipeline.ProcessContext;
import com.relaxed.common.model.result.R;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Yakir
 * @Topic UploadProcess
 * @Description
 * @date 2022/2/10 10:08
 * @Version 1.0
 */
@Slf4j
public class UploadProcess implements BusinessProcess {

	@Override
	public boolean support(ProcessContext context) {
		ProcessContextModel processContextModel = (ProcessContextModel) context.getProcessModel();
		Integer stage = processContextModel.getStage();
		return stage.equals(1);
	}

	@Override
	public void process(ProcessContext context) {
		log.info("上传处理器开始{}", context);
		ProcessContextModel processContextModel = (ProcessContextModel) context.getProcessModel();
		if (processContextModel.getData() == null) {
			context.setNeedBreak(true).setResponse(R.failed(400, "客户端参数错误"));
			return;
		}
		processContextModel.setStage(2);
		log.info("上传处理器结束{}", context);
	}

}
