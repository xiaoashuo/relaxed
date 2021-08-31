package com.relaxed.common.risk.engine.manage;

import com.relaxed.common.risk.engine.enums.FieldType;
import com.relaxed.common.risk.engine.rules.statistics.domain.AggregateParam;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Yakir
 * @Topic ModelMongoManageService
 * @Description
 * @date 2021/8/29 16:14
 * @Version 1.0
 */
public interface ModelEventManageService {

	/**
	 * 存储事件模型
	 * @author yakir
	 * @date 2021/8/29 16:17
	 * @param modelId 模型id
	 * @param jsonString 参数信息
	 * @param attachJson 预处理参数信息
	 * @param isAllowDuplicate 是否覆盖
	 * @return boolean
	 */
	boolean save(Long modelId, String jsonString, String attachJson, boolean isAllowDuplicate);

	/**
	 * 统计数目 未去重
	 * @author yakir
	 * @date 2021/8/30 16:33
	 * @param modelId
	 * @param searchFieldName
	 * @param searchFieldVal
	 * @param refDateFieldName
	 * @param beginDate
	 * @param refDateFieldVal
	 * @return java.lang.Long
	 */
	Long count(Long modelId, String searchFieldName, Object searchFieldVal, String refDateFieldName, Date beginDate,
			Date refDateFieldVal);

	/**
	 * 去重数目统计
	 * @author yakir
	 * @date 2021/8/30 17:00
	 * @param modelId
	 * @param searchFieldName
	 * @param searchFieldVal
	 * @param refDateFieldName
	 * @param beginDate
	 * @param refDateFieldVal
	 * @param functionFieldName
	 * @return java.lang.Long
	 */
	Long distinctCount(Long modelId, String searchFieldName, Object searchFieldVal, String refDateFieldName,
			Date beginDate, Date refDateFieldVal, String functionFieldName);

	/**
	 * 聚合总数
	 * @author yakir
	 * @date 2021/8/30 17:32
	 * @param modelId
	 * @param searchFieldName
	 * @param searchFieldVal
	 * @param refDateFieldName
	 * @param beginDate
	 * @param refDateFieldVal
	 * @param functionFieldName
	 * @return java.lang.BigDecimal
	 */
	BigDecimal sum(Long modelId, String searchFieldName, Object searchFieldVal, String refDateFieldName, Date beginDate,
			Date refDateFieldVal, String functionFieldName);

	/**
	 * 平均值
	 * @author yakir
	 * @date 2021/8/30 18:09
	 * @param modelId
	 * @param searchFieldName
	 * @param searchFieldVal
	 * @param refDateFieldName
	 * @param beginDate
	 * @param refDateFieldVal
	 * @param functionFieldName
	 * @return java.math.BigDecimal
	 */
	BigDecimal average(Long modelId, String searchFieldName, Object searchFieldVal, String refDateFieldName,
			Date beginDate, Date refDateFieldVal, String functionFieldName);

	/**
	 * 计算最大值
	 * @author yakir
	 * @date 2021/8/30 18:14
	 * @param modelId
	 * @param searchFieldName
	 * @param searchFieldVal
	 * @param refDateFieldName
	 * @param beginDate
	 * @param refDateFieldVal
	 * @param functionFieldName
	 * @return java.math.BigDecimal
	 */
	BigDecimal max(Long modelId, String searchFieldName, Object searchFieldVal, String refDateFieldName, Date beginDate,
			Date refDateFieldVal, String functionFieldName);

	/**
	 * 求最小值
	 * @author yakir
	 * @date 2021/8/30 18:17
	 * @param modelId
	 * @param searchFieldName
	 * @param searchFieldVal
	 * @param refDateFieldName
	 * @param beginDate
	 * @param refDateFieldVal
	 * @param functionFieldName
	 * @return java.math.BigDecimal
	 */
	BigDecimal min(Long modelId, String searchFieldName, Object searchFieldVal, String refDateFieldName, Date beginDate,
			Date refDateFieldVal, String functionFieldName);

	/**
	 * 标准差
	 * @author yakir
	 * @date 2021/8/30 18:19
	 * @param modelId
	 * @param searchFieldName
	 * @param searchFieldVal
	 * @param refDateFieldName
	 * @param beginDate
	 * @param refDateFieldVal
	 * @param functionFieldName
	 * @param functionFieldType
	 * @return java.math.BigDecimal
	 */
	BigDecimal sd(Long modelId, String searchFieldName, Object searchFieldVal, String refDateFieldName, Date beginDate,
			Date refDateFieldVal, String functionFieldName, FieldType functionFieldType);

	/**
	 * 中位数
	 * @author yakir
	 * @date 2021/8/30 18:24
	 * @param modelId
	 * @param searchFieldName
	 * @param searchFieldVal
	 * @param refDateFieldName
	 * @param beginDate
	 * @param refDateFieldVal
	 * @param functionFieldName
	 * @return java.math.BigDecimal
	 */
	BigDecimal median(Long modelId, String searchFieldName, Object searchFieldVal, String refDateFieldName,
			Date beginDate, Date refDateFieldVal, String functionFieldName);

	/**
	 * 偏离率计算
	 * @author yakir
	 * @date 2021/8/31 9:16
	 * @param aggregateParam
	 * @return java.math.BigDecimal
	 */
	BigDecimal deviation(AggregateParam aggregateParam);

	/**
	 * 计算方差
	 * @author yakir
	 * @date 2021/8/31 9:19
	 * @param aggregateParam
	 * @return java.math.BigDecimal
	 */
	BigDecimal variance(AggregateParam aggregateParam);

}
