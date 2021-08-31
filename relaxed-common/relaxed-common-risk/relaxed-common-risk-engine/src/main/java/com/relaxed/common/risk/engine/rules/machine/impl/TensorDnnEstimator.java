package com.relaxed.common.risk.engine.rules.machine.impl;

import cn.hutool.core.util.StrUtil;
import com.relaxed.common.risk.engine.model.vo.ModelConfParamVO;
import com.relaxed.common.risk.engine.model.vo.ModelConfVO;
import com.relaxed.common.risk.engine.rules.EvaluateContext;
import com.relaxed.common.risk.engine.rules.EvaluateReport;
import com.relaxed.common.risk.engine.rules.machine.Estimator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.tensorflow.SavedModelBundle;
import org.tensorflow.Session;
import org.tensorflow.Tensor;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Yakir
 * @Topic TensorDnnEstimator
 * @Description
 * @date 2021/8/31 10:11
 * @Version 1.0
 */
@RequiredArgsConstructor
@Slf4j
public class TensorDnnEstimator implements Estimator {

	/**
	 * 基于TensorFlow实现的神经网络模型
	 */
	private static final String TYPE_TENSOR_DNN = "TENSOR_DNN";

	/**
	 * 工作目录
	 */
	private final String WORK_DIR;

	private Map<Long, SavedModelBundle> modelBundleMap = new HashMap<>();

	@Override
	public float predict(ModelConfVO modelConfVO, EvaluateContext evaluateContext, EvaluateReport evaluateReport) {
		SavedModelBundle modelBundle = loadAndCacheModel(modelConfVO);
		if (modelBundle == null) {
			log.warn("模型文件不存在或加载失败，ModelId：{}", modelConfVO.getModelId());
			return 0;
		}
		Session tfSession = modelBundle.session();
		try {
			List<ModelConfParamVO> params = modelConfVO.getParams();
			Session.Runner runner = tfSession.runner();
			for (ModelConfParamVO moldParam : params) {
				runner.feed(moldParam.getFeed(), convert2Tensor(moldParam, evaluateReport.getEvaluateDataMap()));
			}
			Tensor<?> output = runner.fetch(modelConfVO.getOperation()).run().get(0);
			float[][] results = new float[1][1];
			output.copyTo(results);
			return results[0][0];
		}
		catch (Exception e) {
			log.error("模型调用失败，ModelId:{}", modelConfVO.getModelId(), e);
		}
		return 0;

	}

	@Override
	public String getType() {
		return TYPE_TENSOR_DNN;
	}

	private Tensor<?> convert2Tensor(ModelConfParamVO moldParam, Map<String, Map<String, ?>> data) {
		String expressions = moldParam.getExpressions();
		if (StrUtil.isEmpty(expressions)) {
			return Tensor.create(new float[1][1]);
		}
		String[] expList = expressions.split(",");
		float[][] vec = new float[1][expList.length];
		int a = 0;
		for (String s : expList) {
			float xn = 0;
			String[] ss = s.split("\\.");// fields.deviceId，abstractions.log_uid_ip_1_day_qty
			Map<String, ?> stringMap = data.get(ss[0]);
			if (stringMap != null) {
				xn = Float.parseFloat(String.valueOf(stringMap.get(ss[1])));
			}
			vec[0][a++] = xn;
		}
		return Tensor.create(vec);
	}

	private synchronized SavedModelBundle loadAndCacheModel(ModelConfVO mold) {
		SavedModelBundle modelBundle = modelBundleMap.get(mold.getId());
		if (modelBundle == null) {
			String path = WORK_DIR + "\\" + mold.getPath();
			String decomposePath = path.substring(0, path.lastIndexOf("."));
			File file = new File(decomposePath);
			if (file.exists() && file.isDirectory()) {
				// 模型加载，比较耗时
				try {
					modelBundle = SavedModelBundle.load(mold.getPath(), mold.getTag());
					modelBundleMap.put(mold.getId(), modelBundle);
				}
				catch (Exception e) {
					log.error("模型加载失败，MoldId：{}", mold.getId());
				}
			}
		}
		return modelBundle;
	}

}
