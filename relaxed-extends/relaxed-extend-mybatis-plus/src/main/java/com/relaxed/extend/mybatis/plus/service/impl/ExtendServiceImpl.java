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
 * @author Yakir
 * @Topic ExtendServiceImpl
 * @Description
 * @date 2021/7/12 12:46
 * @Version 1.0
 */
public class ExtendServiceImpl<M extends ExtendMapper<T>, T> extends ServiceImpl<M, T> implements ExtendService<T> {

	/**
	 * 默认一次批量插入的数量
	 */
	int DEFAULT_INSERT_BATCH_SIZE = 5000;

	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean insertBatch(Collection<T> list) {
		return insertBatch(list, DEFAULT_INSERT_BATCH_SIZE);
	}

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
