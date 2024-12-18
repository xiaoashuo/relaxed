package com.relaxed.poi;

import com.deepoove.poi.config.Configure;
import com.relaxed.poi.funcs.ConfigureSupplier;
import com.relaxed.poi.funcs.ContentRenderHolder;


/**
 * @author Yakir
 * @Topic PolGlobalConfig
 * @Description
 * @date 2024/3/26 10:43
 * @Version 1.0
 */
public class PoiGlobalConfig {

	/**
	 * 内容渲染持有者
	 */
	private static ContentRenderHolder CONTENT_RENDER_HOLDER;

	private static ConfigureSupplier CONFIGURE_SUPPLIER = Configure::createDefault;

	public static ContentRenderHolder contentRenderHolder() {
		if (CONTENT_RENDER_HOLDER == null) {
			CONTENT_RENDER_HOLDER = new ContentRenderHolder();
			CONTENT_RENDER_HOLDER.init();
		}
		return CONTENT_RENDER_HOLDER;
	}

	/**
	 * 重复设置 以首次为准 设置内容渲染持有者
	 * @param contentRenderHolder
	 * @return
	 */
	public static ContentRenderHolder setContentRenderHolder(ContentRenderHolder contentRenderHolder) {
		if (CONTENT_RENDER_HOLDER == null) {
			CONTENT_RENDER_HOLDER = contentRenderHolder;
		}
		return CONTENT_RENDER_HOLDER;
	}

	/**
	 * 默认配置 占位符 {{}}
	 */
	public static Configure defaultConfigure() {
		return CONFIGURE_SUPPLIER.apply();
	}

	/**
	 * 设置配置提供者
	 * @param supplierFunction
	 */
	public static void setConfigureSupplier(ConfigureSupplier supplierFunction) {
		CONFIGURE_SUPPLIER = supplierFunction;
	}

}
