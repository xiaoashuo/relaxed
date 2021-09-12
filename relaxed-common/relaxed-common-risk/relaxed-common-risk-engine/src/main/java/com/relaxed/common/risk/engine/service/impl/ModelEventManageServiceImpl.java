package com.relaxed.common.risk.engine.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.relaxed.common.risk.engine.enums.FieldType;
import com.relaxed.common.risk.engine.mongdb.MongoDbService;
import com.relaxed.common.risk.engine.rules.statistics.domain.AggregateParamBO;
import com.relaxed.common.risk.engine.service.ModelEventManageService;
import com.relaxed.common.risk.engine.service.ModelManageService;
import com.relaxed.common.risk.model.vo.ModelVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
		String key = getCollectionName(modelId);
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
				return true;
			}
		}

		mongoDbService.insert(key, doc);

		return true;
	}

	@Override
	public Long count(Long modelId, String searchFieldName, Object searchFieldVal, String refDateFieldName,
			Date beginDate, Date refDateFieldVal) {
		String collectionName = getCollectionName(modelId);
		Query query = Query.query(new Criteria(searchFieldName).is(searchFieldVal).and(refDateFieldName)
				.gte(beginDate.getTime()).lte(refDateFieldVal.getTime()));
		return mongoDbService.count(collectionName, query);
	}

	@Override
	public Long distinctCount(Long modelId, String searchFieldName, Object searchFieldVal, String refDateFieldName,
			Date beginDate, Date refDateFieldVal, String functionFieldName) {
		String collectionName = getCollectionName(modelId);
		Query query = Query.query(new Criteria(searchFieldName).is(searchFieldVal))
				.addCriteria(new Criteria(refDateFieldName).gte(beginDate.getTime()).lte(refDateFieldVal.getTime()));
		return mongoDbService.distinctCount(collectionName, query, searchFieldName);
	}

	@Override
	public BigDecimal sum(Long modelId, String searchFieldName, Object searchFieldVal, String refDateFieldName,
			Date beginDate, Date refDateFieldVal, String functionFieldName) {
		String collectionName = getCollectionName(modelId);
		List<AggregationOperation> operations = new ArrayList<>();
		Criteria criteriaDefinition = Criteria.where(searchFieldName).is(searchFieldVal);
		criteriaDefinition.and(refDateFieldName).gte(beginDate.getTime()).lte(refDateFieldVal.getTime());
		operations.add(Aggregation.match(criteriaDefinition));
		operations.add(Aggregation.group("_id").sum(functionFieldName).as("sum"));
		Aggregation aggregation = Aggregation.newAggregation(operations);
		AggregationResults<Document> aggregationResults = mongoDbService.aggregate(collectionName, aggregation);
		Optional<Document> first = aggregationResults.getMappedResults().stream().findFirst();
		if (first.isPresent()) {
			Document document = first.get();
			Object sum = document.get("sum");
			return new BigDecimal(sum.toString());
		}

		return BigDecimal.ZERO;
	}

	@Override
	public BigDecimal average(Long modelId, String searchFieldName, Object searchFieldVal, String refDateFieldName,
			Date beginDate, Date refDateFieldVal, String functionFieldName) {
		String collectionName = getCollectionName(modelId);
		List<AggregationOperation> operations = new ArrayList<>();
		Criteria criteriaDefinition = Criteria.where(searchFieldName).is(searchFieldVal);
		criteriaDefinition.and(refDateFieldName).gte(beginDate.getTime()).lte(refDateFieldVal.getTime());
		operations.add(Aggregation.match(criteriaDefinition));
		operations.add(Aggregation.group("_id").avg(functionFieldName).as("avg"));
		Aggregation aggregation = Aggregation.newAggregation(operations);
		AggregationResults<Document> aggregationResults = mongoDbService.aggregate(collectionName, aggregation);
		Optional<Document> first = aggregationResults.getMappedResults().stream().findFirst();
		if (first.isPresent()) {
			Document document = first.get();
			Object sum = document.get("avg");
			return new BigDecimal(sum.toString());
		}

		return BigDecimal.ZERO;

	}

	@Override
	public BigDecimal max(Long modelId, String searchFieldName, Object searchFieldVal, String refDateFieldName,
			Date beginDate, Date refDateFieldVal, String functionFieldName) {
		String collectionName = getCollectionName(modelId);
		List<AggregationOperation> operations = new ArrayList<>();
		Criteria criteriaDefinition = Criteria.where(searchFieldName).is(searchFieldVal);
		criteriaDefinition.and(refDateFieldName).gte(beginDate.getTime()).lte(refDateFieldVal.getTime());
		operations.add(Aggregation.match(criteriaDefinition));
		operations.add(Aggregation.group("_id").max(functionFieldName).as("max"));
		Aggregation aggregation = Aggregation.newAggregation(operations);
		AggregationResults<Document> aggregationResults = mongoDbService.aggregate(collectionName, aggregation);
		Optional<Document> first = aggregationResults.getMappedResults().stream().findFirst();
		if (first.isPresent()) {
			Document document = first.get();
			Object sum = document.get("max");
			return new BigDecimal(sum.toString());
		}
		return BigDecimal.ZERO;
	}

	@Override
	public BigDecimal min(Long modelId, String searchFieldName, Object searchFieldVal, String refDateFieldName,
			Date beginDate, Date refDateFieldVal, String functionFieldName) {
		String collectionName = getCollectionName(modelId);
		List<AggregationOperation> operations = new ArrayList<>();
		Criteria criteriaDefinition = Criteria.where(searchFieldName).is(searchFieldVal);
		criteriaDefinition.and(refDateFieldName).gte(beginDate.getTime()).lte(refDateFieldVal.getTime());
		operations.add(Aggregation.match(criteriaDefinition));
		operations.add(Aggregation.group("_id").max(functionFieldName).as("min"));
		Aggregation aggregation = Aggregation.newAggregation(operations);
		AggregationResults<Document> aggregationResults = mongoDbService.aggregate(collectionName, aggregation);
		Optional<Document> first = aggregationResults.getMappedResults().stream().findFirst();
		if (first.isPresent()) {
			Document document = first.get();
			Object sum = document.get("min");
			return new BigDecimal(sum.toString());
		}
		return BigDecimal.ZERO;
	}

	@Override
	public BigDecimal median(Long modelId, String searchFieldName, Object searchFieldVal, String refDateFieldName,
			Date beginDate, Date refDateFieldVal, String functionFieldName) {
		String collectionName = getCollectionName(modelId);
		List<AggregationOperation> operations = new ArrayList<>();
		Criteria criteriaDefinition = Criteria.where(searchFieldName).is(searchFieldVal);
		criteriaDefinition.and(refDateFieldName).gte(beginDate.getTime()).lte(refDateFieldVal.getTime());
		operations.add(Aggregation.match(criteriaDefinition));
		operations.add(Aggregation.sort(Sort.by(Sort.Direction.ASC, functionFieldName)));
		Aggregation aggregation = Aggregation.newAggregation(operations);
		AggregationResults<Document> aggregationResults = mongoDbService.aggregate(collectionName, aggregation);
		List<Document> mappedResults = aggregationResults.getMappedResults();
		if (CollectionUtil.isEmpty(mappedResults)) {
			return BigDecimal.ZERO;
		}
		BigDecimal median;
		int mod = mappedResults.size() % 2;
		if (mod == 1) {
			// 奇数
			Document document = mappedResults.get(mod);
			median = new BigDecimal(document.get(functionFieldName).toString());

		}
		else {
			// 偶数
			Document document = mappedResults.get(mod);
			median = new BigDecimal(document.get(functionFieldName).toString());
			document = mappedResults.get(mod - 1);
			BigDecimal tmp = new BigDecimal(document.get(functionFieldName).toString());
			median = median.add(tmp).divide(new BigDecimal(2), 2, 4);
		}
		return median;
	}

	@Override
	public BigDecimal deviation(AggregateParamBO aggregateParamBO) {
		BigDecimal avg = average(aggregateParamBO.getModelId(), aggregateParamBO.getSearchFieldName(),
				aggregateParamBO.getSearchFieldVal(), aggregateParamBO.getRefDateFieldName(),
				aggregateParamBO.getBeginDate(), aggregateParamBO.getRefDateFieldVal(),
				aggregateParamBO.getFunctionFieldName());
		Object functionFieldVal = aggregateParamBO.getFunctionFieldVal();
		BigDecimal deviationVal = new BigDecimal(functionFieldVal.toString()).subtract(avg);
		BigDecimal deviationRate = deviationVal.multiply(new BigDecimal(100)).divide(avg, 2, 4);
		return deviationRate;
	}

	@Override
	public BigDecimal sd(Long modelId, String searchFieldName, Object searchFieldVal, String refDateFieldName,
			Date beginDate, Date refDateFieldVal, String functionFieldName, FieldType functionFieldType) {
		return null;
	}

	@Override
	public BigDecimal variance(AggregateParamBO aggregateParamBO) {
		return null;
	}

	private String getCollectionName(Long modelId) {
		return "entity_" + modelId;
	}

}
