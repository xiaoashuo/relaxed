package com.relaxed.common.core.util.batch.core;

import lombok.Builder;
import lombok.Getter;

/**
 * BatchProgress
 *
 * @author Yakir
 */
@Builder
@Getter
public class BatchProgress {

	private final int total;

	private final int current;

	private final int groupNo;

	private final int groupTotal;

}
