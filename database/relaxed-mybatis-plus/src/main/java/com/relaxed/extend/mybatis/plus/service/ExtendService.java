package com.relaxed.extend.mybatis.plus.service;

import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Collection;

/**
 * MyBatis-Plus 扩展服务接口
 * <p>
 * 该接口扩展了 MyBatis-Plus 的 IService 接口，提供了批量插入数据的功能。 支持全字段插入和部分字段插入两种方式，可以根据实际需求选择使用。
 */
public interface ExtendService<T> extends IService<T> {

	/**
	 * 批量插入数据
	 * <p>
	 * 将集合中的所有数据一次性插入到数据库中。 使用默认的批量大小进行分批处理。
	 * @param list 要插入的数据集合
	 * @return 是否插入成功
	 */
	boolean insertBatch(Collection<T> list);

	/**
	 * 批量插入部分字段数据
	 * <p>
	 * 将集合中的所有数据一次性插入到数据库中，只插入指定的字段。 使用默认的批量大小进行分批处理。
	 * @param list 要插入的数据集合
	 * @return 是否插入成功
	 */
	boolean insertBatchSomeColumn(Collection<T> list);

	/**
	 * 批量插入部分字段数据
	 * <p>
	 * 将集合中的所有数据一次性插入到数据库中，只插入指定的字段。 可以指定每批处理的数据量。
	 * @param list 要插入的数据集合
	 * @param batchSize 每批处理的数据量
	 * @return 是否插入成功
	 */
	boolean insertBatchSomeColumn(Collection<T> list, int batchSize);

	/**
	 * 批量插入数据
	 * <p>
	 * 将集合中的所有数据一次性插入到数据库中。 可以指定每批处理的数据量。
	 * @param list 要插入的数据集合
	 * @param batchSize 每批处理的数据量
	 * @return 是否插入成功
	 */
	boolean insertBatch(Collection<T> list, int batchSize);

}