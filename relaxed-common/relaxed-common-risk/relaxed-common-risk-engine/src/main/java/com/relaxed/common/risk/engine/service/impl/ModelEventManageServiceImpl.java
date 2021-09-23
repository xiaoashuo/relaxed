package com.relaxed.common.risk.engine.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.relaxed.common.risk.engine.mongdb.MongoDbService;
import com.relaxed.common.risk.engine.rules.statistics.domain.AggregateParamBO;
import com.relaxed.common.risk.engine.service.ModelEventManageService;
import com.relaxed.common.risk.engine.service.ModelManageService;
import com.relaxed.common.risk.model.entity.FieldMeta;
import com.relaxed.common.risk.model.entity.Model;
import com.relaxed.common.risk.model.enums.FieldType;
import com.relaxed.common.risk.model.vo.ModelVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
@Slf4j
@RequiredArgsConstructor
public class ModelEventManageServiceImpl implements ModelEventManageService {

	private final MongoDbService mongoDbService;

	@Override
	public boolean save(ModelVO modelVO, String jsonString, String attachJson, boolean isAllowDuplicate) {
		String key = getCollectionName(modelVO.getId());
		// 存储文档
		Document doc = Document.parse(jsonString);
		Document attach = Document.parse(attachJson);
		// 风控执行日期拿出来
		Long executionTime = doc.getLong(modelVO.getReferenceDate());
		if (executionTime != null) {
			attach.put("risk_ref_datetime", new Date(executionTime));
		}
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
	public Long count(AggregateParamBO aggregateParamBO) {
		String collectionName = getCollectionName(aggregateParamBO.getModelId());
		FieldMeta<Object> searchFieldMeta = aggregateParamBO.getSearchFieldMeta();
		FieldMeta<Date> refDateFieldMeta = aggregateParamBO.getRefDateFieldMeta();
		Query query = Query.query(new Criteria(searchFieldMeta.getFieldName()).is(searchFieldMeta.getFieldValue())
				.and(refDateFieldMeta.getFieldName()).gte(aggregateParamBO.getBeginDate().getTime())
				.lte(refDateFieldMeta.getFieldValue().getTime()));
		return mongoDbService.count(collectionName, query);
	}

	@Override
	public Long distinctCount(AggregateParamBO aggregateParamBO) {
		String collectionName = getCollectionName(aggregateParamBO.getModelId());
		FieldMeta<Object> searchFieldMeta = aggregateParamBO.getSearchFieldMeta();
		FieldMeta<Date> refDateFieldMeta = aggregateParamBO.getRefDateFieldMeta();
		Query query = Query.query(new Criteria(searchFieldMeta.getFieldName()).is(searchFieldMeta.getFieldValue()))
				.addCriteria(
						new Criteria(refDateFieldMeta.getFieldName()).gte(aggregateParamBO.getBeginDate().getTime())
								.lte(refDateFieldMeta.getFieldValue().getTime()));
		return mongoDbService.distinctCount(collectionName, query, searchFieldMeta.getFieldName());
	}

	@Override
	public BigDecimal sum(AggregateParamBO aggregateParamBO) {
		String collectionName = getCollectionName(aggregateParamBO.getModelId());
		FieldMeta<Object> searchFieldMeta = aggregateParamBO.getSearchFieldMeta();
		FieldMeta<Date> refDateFieldMeta = aggregateParamBO.getRefDateFieldMeta();
		FieldMeta<Object> functionFieldMeta = aggregateParamBO.getFunctionFieldMeta();

		List<AggregationOperation> operations = new ArrayList<>();
		Criteria criteriaDefinition = Criteria.where(searchFieldMeta.getFieldName())
				.is(searchFieldMeta.getFieldValue());
		criteriaDefinition.and(refDateFieldMeta.getFieldName()).gte(aggregateParamBO.getBeginDate().getTime())
				.lte(refDateFieldMeta.getFieldValue().getTime());
		operations.add(Aggregation.match(criteriaDefinition));
		operations.add(Aggregation.group("_id").sum(functionFieldMeta.getFieldName()).as("sum"));
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
	public BigDecimal average(AggregateParamBO aggregateParamBO) {
		String collectionName = getCollectionName(aggregateParamBO.getModelId());
		FieldMeta<Object> searchFieldMeta = aggregateParamBO.getSearchFieldMeta();
		FieldMeta<Date> refDateFieldMeta = aggregateParamBO.getRefDateFieldMeta();
		FieldMeta<Object> functionFieldMeta = aggregateParamBO.getFunctionFieldMeta();
		List<AggregationOperation> operations = new ArrayList<>();
		Criteria criteriaDefinition = Criteria.where(searchFieldMeta.getFieldName())
				.is(searchFieldMeta.getFieldValue());
		criteriaDefinition.and(refDateFieldMeta.getFieldName()).gte(aggregateParamBO.getBeginDate().getTime())
				.lte(refDateFieldMeta.getFieldValue().getTime());
		operations.add(Aggregation.match(criteriaDefinition));
		operations.add(Aggregation.group("_id").avg(functionFieldMeta.getFieldName()).as("avg"));
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
	public BigDecimal max(AggregateParamBO aggregateParamBO) {
		String collectionName = getCollectionName(aggregateParamBO.getModelId());
		FieldMeta<Object> searchFieldMeta = aggregateParamBO.getSearchFieldMeta();
		FieldMeta<Date> refDateFieldMeta = aggregateParamBO.getRefDateFieldMeta();
		FieldMeta<Object> functionFieldMeta = aggregateParamBO.getFunctionFieldMeta();
		List<AggregationOperation> operations = new ArrayList<>();
		Criteria criteriaDefinition = Criteria.where(searchFieldMeta.getFieldName())
				.is(searchFieldMeta.getFieldValue());
		criteriaDefinition.and(refDateFieldMeta.getFieldName()).gte(aggregateParamBO.getBeginDate().getTime())
				.lte(refDateFieldMeta.getFieldValue().getTime());
		operations.add(Aggregation.match(criteriaDefinition));
		operations.add(Aggregation.group("_id").max(functionFieldMeta.getFieldName()).as("max"));
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
	public BigDecimal min(AggregateParamBO aggregateParamBO) {
		String collectionName = getCollectionName(aggregateParamBO.getModelId());
		FieldMeta<Object> searchFieldMeta = aggregateParamBO.getSearchFieldMeta();
		FieldMeta<Date> refDateFieldMeta = aggregateParamBO.getRefDateFieldMeta();
		FieldMeta<Object> functionFieldMeta = aggregateParamBO.getFunctionFieldMeta();
		List<AggregationOperation> operations = new ArrayList<>();
		Criteria criteriaDefinition = Criteria.where(searchFieldMeta.getFieldName())
				.is(searchFieldMeta.getFieldValue());
		criteriaDefinition.and(refDateFieldMeta.getFieldName()).gte(aggregateParamBO.getBeginDate().getTime())
				.lte(refDateFieldMeta.getFieldValue().getTime());
		operations.add(Aggregation.match(criteriaDefinition));
		operations.add(Aggregation.group("_id").max(functionFieldMeta.getFieldName()).as("min"));
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
	public BigDecimal median(AggregateParamBO aggregateParamBO) {
		String collectionName = getCollectionName(aggregateParamBO.getModelId());
		FieldMeta<Object> searchFieldMeta = aggregateParamBO.getSearchFieldMeta();
		FieldMeta<Date> refDateFieldMeta = aggregateParamBO.getRefDateFieldMeta();
		FieldMeta<Object> functionFieldMeta = aggregateParamBO.getFunctionFieldMeta();
		List<AggregationOperation> operations = new ArrayList<>();
		Criteria criteriaDefinition = Criteria.where(searchFieldMeta.getFieldName())
				.is(searchFieldMeta.getFieldValue());
		criteriaDefinition.and(refDateFieldMeta.getFieldName()).gte(aggregateParamBO.getBeginDate().getTime())
				.lte(refDateFieldMeta.getFieldValue().getTime());
		operations.add(Aggregation.match(criteriaDefinition));
		operations.add(Aggregation.sort(Sort.by(Sort.Direction.ASC, functionFieldMeta.getFieldName())));
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
			median = new BigDecimal(document.get(functionFieldMeta.getFieldName()).toString());

		}
		else {
			// 偶数
			Document document = mappedResults.get(mod);
			median = new BigDecimal(document.get(functionFieldMeta.getFieldName()).toString());
			document = mappedResults.get(mod - 1);
			BigDecimal tmp = new BigDecimal(document.get(functionFieldMeta.getFieldName()).toString());
			median = median.add(tmp).divide(new BigDecimal(2), 2, 4);
		}
		return median;
	}

	@Override
	public BigDecimal deviation(AggregateParamBO aggregateParamBO) {
		BigDecimal avg = average(aggregateParamBO);
		FieldMeta<Object> functionFieldMeta = aggregateParamBO.getFunctionFieldMeta();
		Object functionFieldVal = functionFieldMeta.getFieldValue();
		BigDecimal deviationVal = new BigDecimal(functionFieldVal.toString()).subtract(avg);
		BigDecimal deviationRate = deviationVal.multiply(new BigDecimal(100)).divide(avg, 2, 4);
		return deviationRate;
	}

	@Override
	public BigDecimal sd(AggregateParamBO aggregateParamBO) {
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
