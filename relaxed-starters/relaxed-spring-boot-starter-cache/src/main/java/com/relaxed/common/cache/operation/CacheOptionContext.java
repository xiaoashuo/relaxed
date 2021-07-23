package com.relaxed.common.cache.operation;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * @author Yakir
 * @Topic CacheOptionContext
 * @Description
 * @date 2021/7/23 18:16
 * @Version 1.0
 */
@Data
@RequiredArgsConstructor
public class CacheOptionContext {

    private final Object[] args;

    private final Object target;

}
