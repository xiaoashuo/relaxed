package com.relaxed.common.risk.engine.core.inits;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import com.relaxed.common.risk.engine.cache.CacheKey;
import com.relaxed.common.risk.engine.cache.CacheService;
import com.relaxed.common.risk.engine.config.EngineProperties;
import com.relaxed.common.risk.engine.enums.ModelEnums;
import com.relaxed.common.risk.engine.service.DataListManageService;
import com.relaxed.common.risk.engine.service.ModelManageService;
import com.relaxed.common.risk.model.vo.MobileInfoVO;
import com.relaxed.common.risk.model.vo.ModelVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Yakir
 * @Topic DataInitListener
 * @Description
 * @date 2021/9/6 17:59
 * @Version 1.0
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class DataInitListener implements CommandLineRunner {

	private final ModelManageService modelManageService;

	private final DataListManageService dataListManageService;

	private final CacheService cacheService;

	private void mobileInfoMapLoad() {
		try {
			List<String> lines = FileUtil.readLines(EngineProperties.getMobilePath(), Charset.forName("UTF-8"));
			List<MobileInfoVO> mobileInfoVOList = lines.stream().map(line -> {
				String[] info = line.split(",");
				MobileInfoVO mobile = new MobileInfoVO();
				if (info.length == 5) {
					mobile.setMobile(info[0]);
					mobile.setProvince(info[1]);
					mobile.setCity(info[2]);
					mobile.setSupplier(info[3]);
					mobile.setRegionCode(info[4]);
				}
				return mobile;
			}).collect(Collectors.toList());
			for (MobileInfoVO mobileInfoVO : mobileInfoVOList) {
				cacheService.put(CacheKey.getMobileInfoMapCacheKey(mobileInfoVO.getMobile()), mobileInfoVO);
			}
		}
		catch (Exception e) {
			log.error("mobile info load fail", e);
		}
	}

	private void modelGuidLoad() {
		Map<String, Long> modelGuidMap = modelManageService.listByStatus(ModelEnums.StatusEnum.ENABLE.getStatus());
		for (Map.Entry<String, Long> entry : modelGuidMap.entrySet()) {
			cacheService.put(CacheKey.getModelGuidCacheKey(entry.getKey()), entry.getValue());
		}
	}

	private void dataListLoad() {
		List<ModelVO> modelVOS = dataListManageService.listByStatus(ModelEnums.StatusEnum.ENABLE.getStatus());
		// 获取所有的黑名单列表
		if (CollectionUtil.isNotEmpty(modelVOS)) {
			for (ModelVO modelVO : modelVOS) {
				Long modelId = modelVO.getId();
				Map<String, Object> dataListMap = new HashMap<>(32);
				dataListManageService.buildDataListMap(modelId, dataListMap);
				cacheService.put(CacheKey.getDataListCacheKey(modelId), dataListMap);
			}
		}
	}

	@Override
	public void run(String... args) throws Exception {
		// 1.初始化 model manage
		// 2. 初始化model guid
		log.info("model guid start loaded.");
		modelGuidLoad();
		log.info("model guid end loaded.");
		// 2.初始化 data list
		log.info("data list start loaded.");
		dataListLoad();
		log.info("data list end loaded.");
		// 3.初始化mobile info
		log.info("mobile info start loaded.");
		mobileInfoMapLoad();
		log.info("mobile info end loaded.");
	}

}
