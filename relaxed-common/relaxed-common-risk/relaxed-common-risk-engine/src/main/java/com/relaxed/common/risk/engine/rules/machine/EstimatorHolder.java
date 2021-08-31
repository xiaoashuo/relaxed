package com.relaxed.common.risk.engine.rules.machine;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Yakir
 * @Topic EstimatorHolder
 * @Description
 * @date 2021/8/31 9:47
 * @Version 1.0
 */
public class EstimatorHolder {

	/**
	 * 评估器持有者
	 */
	private Map<String, Estimator> ESTIMATOR_HOLDER = new HashMap<>();

	public void put(Estimator estimator) {
		ESTIMATOR_HOLDER.put(estimator.getType(), estimator);
	}

	public Estimator get(String type) {
		return Objects.requireNonNull(ESTIMATOR_HOLDER.get(type));
	}

}
