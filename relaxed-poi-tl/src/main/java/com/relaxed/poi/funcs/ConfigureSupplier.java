package com.relaxed.poi.funcs;

import com.deepoove.poi.config.Configure;

/**
 * A functional interface that supplies a {@link Configure} instance for POI-TL template
 * rendering. This interface is used to provide custom configuration for Word document
 * generation.
 *
 * @author Yakir
 * @since 1.0
 */
@FunctionalInterface
public interface ConfigureSupplier {

	/**
	 * Applies this supplier to get a {@link Configure} instance.
	 * @return a {@link Configure} instance for POI-TL template rendering
	 */
	Configure apply();

}
