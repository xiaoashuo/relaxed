package com.relaxed.common.log.biz.service;

import com.relaxed.common.log.biz.model.AttributeModel;
import com.relaxed.common.log.biz.model.DiffMeta;

import java.util.List;

/**
 * @author Yakir
 * @Topic RecordHandler
 * @Description
 * @date 2021/12/14 14:08
 * @Version 1.0
 */
public interface IDataHandler {

	/**
	 * 差异化对象
	 * @author yakir
	 * @date 2021/12/14 14:25
	 * @param diffMeta
	 */
	List<AttributeModel> diffObject(DiffMeta diffMeta);

}
