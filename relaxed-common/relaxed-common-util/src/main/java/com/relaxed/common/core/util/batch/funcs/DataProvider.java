package com.relaxed.common.core.util.batch.funcs;

import com.relaxed.common.core.util.batch.core.BatchMeta;

import java.util.List;

/**
 * 批量数据的提供者接口。 负责按批次获取待处理的数据，可以从数据库、文件、远程服务等数据源获取数据。
 *
 * @param <T> 数据类型
 * @author Yakir
 * @since 1.0
 */
public interface DataProvider<T> {

	/**
	 * 获取一批数据
	 * @param batchMeta 批次元数据，包含当前批次的基本信息（如批次号、起始位置、大小等）
	 * @return 返回当前批次的数据列表，如果没有数据则返回空列表
	 */
	List<T> get(BatchMeta batchMeta);

}
