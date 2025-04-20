package com.relaxed.extend.mybatis.plus.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.relaxed.extend.mybatis.plus.mapper.ExtendMapper;
import com.relaxed.extend.mybatis.plus.service.ExtendService;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * MyBatis-Plus 扩展服务实现类
 * <p>
 * 实现了 ExtendService 接口，提供了批量插入数据的具体实现。 支持全字段插入和部分字段插入两种方式，并提供了事务支持。
 */
public class ExtendServiceImpl<M extends ExtendMapper<T>, T> extends ServiceImpl<M, T> implements ExtendService<T> {

	/**
	 * 默认的批量插入大小
	 * <p>
	 * 当不指定批量大小时，使用该值作为默认值。 该值设置为 5000，可以根据实际需求调整。
	 */
	int DEFAULT_INSERT_BATCH_SIZE = 5000;

	/**
	 * 批量插入数据
	 * <p>
	 * 使用默认的批量大小进行分批处理。 使用事务注解确保数据一致性。
	 * @param list 要插入的数据集合
	 * @return 是否插入成功
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean insertBatch(Collection<T> list) {
		return insertBatch(list, DEFAULT_INSERT_BATCH_SIZE);
	}

	/**
	 * 批量插入部分字段数据
	 * <p>
	 * 使用默认的批量大小进行分批处理。
	 * @param list 要插入的数据集合
	 * @return 是否插入成功
	 */
	@Override
	public boolean insertBatchSomeColumn(Collection<T> list) {
		return insertBatchSomeColumn(list, DEFAULT_INSERT_BATCH_SIZE);
	}

	/**
	 * 批量插入部分字段数据
	 * <p>
	 * 将数据分批处理，每批处理指定数量的数据。 使用事务注解确保数据一致性。
	 * @param list 要插入的数据集合
	 * @param batchSize 每批处理的数据量
	 * @return 是否插入成功
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean insertBatchSomeColumn(Collection<T> list, int batchSize) {
		Assert.isFalse(batchSize < 1, "batchSize must not be less than one");
		if (CollectionUtils.isEmpty(list)) {
			return false;
		}
		int size = list.size();
		// step flag
		int i = 1;
		// stage list not all list
		List<T> stageList = new ArrayList<>();
		for (T element : list) {
			stageList.add(element);
			if ((i % batchSize == 0) || i == size) {
				this.getBaseMapper().insertBatchSomeColumn(stageList);
				stageList = new ArrayList<>();
			}
			i++;
		}
		return true;
	}

	/**
	 * 批量插入数据
	 * <p>
	 * 将数据分批处理，每批处理指定数量的数据。 使用事务注解确保数据一致性。
	 * @param list 要插入的数据集合
	 * @param batchSize 每批处理的数据量
	 * @return 是否插入成功
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean insertBatch(Collection<T> list, int batchSize) {
		Assert.isFalse(batchSize < 1, "batchSize must not be less than one");
		if (CollectionUtils.isEmpty(list)) {
			return false;
		}
		int size = list.size();
		// step flag
		int i = 1;
		// stage list not all list
		List<T> stageList = new ArrayList<>();
		for (T element : list) {
			stageList.add(element);
			if ((i % batchSize == 0) || i == size) {
				this.getBaseMapper().insertBatch(stageList);
				stageList = new ArrayList<>();
			}
			i++;
		}
		return true;
	}

}
