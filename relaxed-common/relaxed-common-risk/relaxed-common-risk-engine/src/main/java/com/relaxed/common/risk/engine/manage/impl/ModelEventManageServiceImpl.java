package com.relaxed.common.risk.engine.manage.impl;

import com.relaxed.common.risk.engine.manage.ModelManageService;
import com.relaxed.common.risk.engine.manage.ModelEventManageService;
import com.relaxed.common.risk.engine.model.vo.ModelVO;
import com.relaxed.common.risk.engine.mongdb.MongoDbService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Yakir
 * @Topic ModelMongoManageServiceImpl
 * @Description
 * @date 2021/8/29 16:14
 * @Version 1.0
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ModelEventManageServiceImpl implements ModelEventManageService {

	private final ModelManageService modelManageService;

	private final MongoDbService mongoDbService;

	@Override
	public boolean save(Long modelId, String jsonString, String attachJson, boolean isAllowDuplicate) {
		String key = "entity_" + modelId;
		// 存储文档
		Document doc = Document.parse(jsonString);
		Document attach = Document.parse(attachJson);
		ModelVO modelVO = modelManageService.getById(modelId);
		// 风控执行日期拿出来
		attach.put("risk_ref_datetime", new Date(doc.getLong(modelVO.getReferenceDate())));
		doc.putAll(attach);
		if (!isAllowDuplicate) {
			// 设置查询条件 根据主键查询
			String entryName = modelVO.getEntryName();
			Query query = Query.query(new Criteria(entryName).is(doc.get(entryName)));
			long count = mongoDbService.count(key, query);
			if (count > 0) {
				log.info("{} record has already exists!", entryName);
			}
			return true;
		}

		mongoDbService.insert(key, doc);

		return true;
	}

}
