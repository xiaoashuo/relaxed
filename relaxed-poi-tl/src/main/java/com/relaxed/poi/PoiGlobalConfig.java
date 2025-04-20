package com.relaxed.poi;

import com.deepoove.poi.config.Configure;
import com.relaxed.poi.funcs.ConfigureSupplier;
import com.relaxed.poi.funcs.ContentRenderHolder;

/**
 * Global configuration class for POI-TL (Word template engine). This class provides
 * static methods to manage content rendering and configuration settings.
 *
 * @author Yakir
 * @since 1.0
 */
public class PoiGlobalConfig {

	/**
	 * Content render holder instance for managing content rendering.
	 */
	private static ContentRenderHolder CONTENT_RENDER_HOLDER;

	/**
	 * Default configuration supplier that creates default POI-TL configuration.
	 */
	private static ConfigureSupplier CONFIGURE_SUPPLIER = Configure::createDefault;

	/**
	 * Gets the content render holder instance. If not initialized, creates a new
	 * instance.
	 * @return the content render holder instance
	 */
	public static ContentRenderHolder contentRenderHolder() {
		if (CONTENT_RENDER_HOLDER == null) {
			CONTENT_RENDER_HOLDER = new ContentRenderHolder();
			CONTENT_RENDER_HOLDER.init();
		}
		return CONTENT_RENDER_HOLDER;
	}

	/**
	 * Sets the content render holder if it hasn't been set before. The first setting will
	 * be used, subsequent calls will be ignored.
	 * @param contentRenderHolder the content render holder to set
	 * @return the current content render holder instance
	 */
	public static ContentRenderHolder setContentRenderHolder(ContentRenderHolder contentRenderHolder) {
		if (CONTENT_RENDER_HOLDER == null) {
			CONTENT_RENDER_HOLDER = contentRenderHolder;
		}
		return CONTENT_RENDER_HOLDER;
	}

	/**
	 * Gets the default POI-TL configuration with standard placeholder format ({{}}).
	 * @return the default configuration instance
	 */
	public static Configure defaultConfigure() {
		return CONFIGURE_SUPPLIER.apply();
	}

	/**
	 * Sets the configuration supplier function.
	 * @param supplierFunction the supplier function to set
	 */
	public static void setConfigureSupplier(ConfigureSupplier supplierFunction) {
		CONFIGURE_SUPPLIER = supplierFunction;
	}

}
