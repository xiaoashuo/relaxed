package com.relaxed.poi.funcs;

import com.deepoove.poi.config.Configure;

/**
 * @author Yakir
 * @Topic ConfigureSupplier
 * @Description
 * @date 2024/3/26 13:49
 * @Version 1.0
 */
@FunctionalInterface
public interface ConfigureSupplier {

	Configure apply();

}
