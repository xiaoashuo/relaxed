package com.relaxed.extend.mybatis.plus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

import static com.baomidou.mybatisplus.core.toolkit.Constants.COLLECTION;

/**
 * @author Yakir
 * @Topic ExtendService
 * @Description
 * @date 2021/7/12 12:46
 * @Version 1.0
 */
public interface ExtendService<T> extends IService<T> {

	/**
	 * list insert
	 * @param list
	 * @return
	 */
	boolean insertBatch(Collection<T> list);

	/**
	 * list insert
	 * @param list
	 * @param batchSize
	 * @return
	 */
	boolean insertBatch(Collection<T> list, int batchSize);

}
