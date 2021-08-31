package com.relaxed.common.risk.engine.manage.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.relaxed.common.risk.engine.cache.CacheKey;
import com.relaxed.common.risk.engine.cache.CacheService;
import com.relaxed.common.risk.engine.enums.DataListEnum;
import com.relaxed.common.risk.engine.enums.ModelEnums;
import com.relaxed.common.risk.engine.manage.DataListManageService;
import com.relaxed.common.risk.engine.model.vo.DataListRecordsVO;
import com.relaxed.common.risk.engine.model.vo.DataListsVO;
import com.relaxed.common.risk.engine.model.vo.ModelVO;
import com.relaxed.common.risk.engine.service.DataListRecordsService;
import com.relaxed.common.risk.engine.service.DataListsService;
import com.relaxed.common.risk.engine.service.ModelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Yakir
 * @Topic DataListManageServiceImpl
 * @Description
 * @date 2021/8/30 11:19
 * @Version 1.0
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class DataListManageServiceImpl implements DataListManageService {

	private final DataListsService dataListsService;

	private final DataListRecordsService dataListRecordsService;

	private final CacheService cacheService;

	private final ModelService modelService;

	@PostConstruct
	public void init() {
		// 获取所有model
		List<ModelVO> modelVOS = modelService.listByStatus(ModelEnums.StatusEnum.ENABLE.getStatus());
		// 获取所有的黑名单列表
		if (CollectionUtil.isEmpty(modelVOS)) {
			return;
		}
		for (ModelVO modelVO : modelVOS) {
			Long modelId = modelVO.getId();
			Map<String, Object> dataListMap = new HashMap<>(32);
			buildDataListMap(modelId, dataListMap);
			cacheService.put(getDataListCacheKey(modelId), dataListMap);
		}
		log.info("data list has loaded.");

	}

	private void buildDataListMap(Long modelId, Map<String, Object> dataListMap) {
		List<DataListsVO> dateListsVOS = dataListsService.list(modelId, DataListEnum.StatusEnum.ENABLE.getStatus());
		if (CollectionUtil.isNotEmpty(dateListsVOS)) {
			buildList2Map(dataListMap, dateListsVOS);
		}
	}

	private String getDataListCacheKey(Long modelId) {
		return CacheKey.getDataListCacheKey(modelId);
	}

	@Override
	public Map<String, Object> getDataListMap(Long modelId) {
		Map<String, Object> listRecordMap = cacheService.get(getDataListCacheKey(modelId));
		if (CollectionUtil.isNotEmpty(listRecordMap)) {
			return listRecordMap;
		}
		Map<String, Object> dataListMap = new HashMap<>(32);
		buildDataListMap(modelId, dataListMap);
		cacheService.put(getDataListCacheKey(modelId), dataListMap);
		return dataListMap;
	}

	/**
	 * 构建数据列表map phone-black map {k 13933333 v "" ,kv}
	 * @author yakir
	 * @date 2021/8/31 16:41
	 * @param dataListMap [ {key dlName item field 139666 value ""}]
	 * @param dateListsVOS
	 */
	private void buildList2Map(Map<String, Object> dataListMap, List<DataListsVO> dateListsVOS) {

		for (DataListsVO dataListVO : dateListsVOS) {
			Map<String, String> dataListRecords = new HashMap<>();
			// record list
			List<DataListRecordsVO> recordVOList = dataListRecordsService.listDataRecord(dataListVO.getId());
			if (recordVOList != null) {
				for (DataListRecordsVO record : recordVOList) {
					dataListRecords.put(record.getDataRecord(), "");
				}
			}
			dataListMap.put(dataListVO.getName(), dataListRecords);
		}
	}

}
