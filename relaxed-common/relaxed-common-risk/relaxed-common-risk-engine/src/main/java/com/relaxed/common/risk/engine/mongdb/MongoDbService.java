package com.relaxed.common.risk.engine.mongdb;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Query;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Yakir
 * @Topic MongoDbService
 * @Description
 * @date 2021/8/29 14:43
 * @Version 1.0
 */
public interface MongoDbService {

	/**
	 * 获取mongo db 执行模板
	 * @author yakir
	 * @date 2021/8/29 15:04
	 * @return org.springframework.data.mongodb.core.MongoTemplate
	 */
	MongoTemplate getMongoTemplate();

	/**
	 * 查询集合总数
	 * @author yakir
	 * @date 2021/8/29 15:15
	 * @param collectionName
	 * @return long
	 */
	long count(String collectionName);

	/**
	 * 查询数据
	 * @author yakir
	 * @date 2021/8/29 16:34
	 * @param collectionName
	 * @param query 条件
	 * @return long
	 */
	long count(String collectionName, Query query);

	/**
	 * 去重
	 * @author yakir
	 * @date 2021/8/30 17:27
	 * @param collectionName
	 * @param query
	 * @param fieldName
	 * @return long
	 */
	long distinctCount(String collectionName, Query query, String fieldName);

	/**
	 * 插入文档到指定集合
	 * @author yakir
	 * @date 2021/8/29 16:27
	 * @param collectionName 类比sql表
	 * @param obj
	 * @return T
	 */
	<T> T insert(String collectionName, T obj);

	/**
	 * 聚合查询
	 * @author yakir
	 * @date 2021/8/30 18:15
	 * @param collectionName
	 * @param aggregation
	 * @return org.springframework.data.mongodb.core.aggregation.AggregationResults<org.bson.Document>
	 */
	AggregationResults<Document> aggregate(String collectionName, Aggregation aggregation);

}
