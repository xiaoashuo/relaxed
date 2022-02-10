package com.relaxed.autoconfigure.web.pipline;

import com.relaxed.common.core.pipeline.ProcessModel;
import lombok.Data;

/**
 * @author Yakir
 * @Topic UploadModel
 * @Description
 * @date 2022/2/10 10:09
 * @Version 1.0
 */
@Data
public class ProcessContextModel implements ProcessModel {

	private Integer stage;

	private String data;

}
