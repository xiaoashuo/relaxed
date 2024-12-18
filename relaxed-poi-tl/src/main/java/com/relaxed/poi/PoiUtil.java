package com.relaxed.poi;

import com.deepoove.poi.config.Configure;

import com.relaxed.poi.domain.ElementMeta;
import com.relaxed.poi.domain.LabelData;
import com.relaxed.poi.funcs.RenderFunction;
import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * @author Yakir
 * @Topic PoiUtil
 * @Description
 * @date 2024/3/28 9:49
 * @Version 1.0
 */
@UtilityClass
public class PoiUtil {

	private final Logger log = LoggerFactory.getLogger(PoiTemplate.class);

	private static final PoiTemplate INSTANCE = PoiTemplate.INSTANCE;

	public void renderWord(File source, File dest, List<LabelData> contents) {
		INSTANCE.renderWord(source, dest, contents);
	}

	public void renderWord(File source, File dest, List<LabelData> contents, Configure configure) {
		INSTANCE.renderWord(source, dest, contents, configure);
	}

	public void renderWord(InputStream inputStream, OutputStream outputStream, List<LabelData> contents) {
		INSTANCE.renderWord(inputStream, outputStream, contents);
	}

	public void renderWord(File source, File dest, Map<String, Object> data) {
		INSTANCE.renderWord(source, dest, data);
	}

	public void renderWord(File source, File dest, Map<String, Object> data, Configure configure) {
		INSTANCE.renderWord(source, dest, data, configure);
	}

	/**
	 * 渲染word
	 * @param inputStream
	 * @param outputStream
	 * @param contents
	 * @param configure
	 */
	public void renderWord(InputStream inputStream, OutputStream outputStream, List<LabelData> contents,
			Configure configure) {
		INSTANCE.renderWord(inputStream, outputStream, contents, configure);
	}

	public void renderWord(InputStream inputStream, OutputStream outputStream, Map<String, Object> dataMap) {
		INSTANCE.renderWord(inputStream, outputStream, dataMap);
	}

	public void renderWord(InputStream inputStream, OutputStream outputStream, Map<String, Object> dataMap,
			Configure configure) {
		INSTANCE.renderWord(inputStream, outputStream, dataMap, configure);
	}

	public List<ElementMeta> templateElement(InputStream inputStream) {
		return INSTANCE.templateElement(inputStream);
	}

	public List<ElementMeta> templateElement(InputStream inputStream, Configure configure) {
		return INSTANCE.templateElement(inputStream, configure);
	}

	public <R> R render(InputStream inputStream, Configure configure, RenderFunction<R> function) {
		return INSTANCE.render(inputStream, configure, function);
	}

}
