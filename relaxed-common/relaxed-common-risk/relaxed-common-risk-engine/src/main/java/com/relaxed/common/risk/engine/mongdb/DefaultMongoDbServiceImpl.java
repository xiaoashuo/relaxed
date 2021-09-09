package com.relaxed.common.risk.engine.mongdb;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonValue;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

/**
 * @author Yakir
 * @Topic DefaultMongoDbServiceImpl
 * @Description
 * @date 2021/8/29 14:43
 * @Version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultMongoDbServiceImpl implements MongoDbService {

	private final MongoTemplate mongoTemplate;

	@Override
	public MongoTemplate getMongoTemplate() {
		return mongoTemplate;
	}

	@Override
	public long count(String collectionName) {
		return getMongoTemplate().count(new Query(), collectionName);
	}

	@Override
	public long count(String collectionName, Query query) {
		return getMongoTemplate().count(query, collectionName);
	}

	@Override
	public long distinctCount(String collectionName, Query query, String fieldName) {
		List<Object> distinct = mongoTemplate.findDistinct(query, fieldName, collectionName, Object.class);
		return distinct.size();
	}

	@Override
	public AggregationResults<Document> aggregate(String collectionName, Aggregation aggregation) {
		return mongoTemplate.aggregate(aggregation, collectionName, Document.class);
	}

	@Override
	public <T> T insert(String collectionName, T obj) {
		return getMongoTemplate().insert(obj, collectionName);
	}

}
