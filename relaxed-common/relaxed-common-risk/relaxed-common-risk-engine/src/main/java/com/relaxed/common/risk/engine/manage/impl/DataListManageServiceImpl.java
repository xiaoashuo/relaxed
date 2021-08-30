package com.relaxed.common.risk.engine.manage.impl;

import com.relaxed.common.risk.engine.cache.CacheService;
import com.relaxed.common.risk.engine.manage.DataListManageService;
import com.relaxed.common.risk.engine.service.DataListsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

	private final CacheService cacheService;

}
