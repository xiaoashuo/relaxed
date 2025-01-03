package com.relaxed.common.core.util.batch.funcs;

import com.relaxed.common.core.util.batch.core.BatchMeta;

import java.util.List;

/**
 * @author Yakir
 * @Topic DataProvider
 * @Description 数据提供者
 * @date 2023/9/22 15:13
 * @Version 1.0
 */
public interface DataProvider<T> {

	/**
	 * 获取数据
	 * @param batchMeta 数据提供者元数据
	 * @return
	 */
	List<T> get(BatchMeta batchMeta);

}
