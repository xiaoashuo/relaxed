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
 * Utility class for POI-TL that provides convenient static methods for Word document
 * rendering. This class serves as a facade for the PoiTemplate class, offering simplified
 * access to its functionality.
 *
 * @author Yakir
 * @since 1.0
 */
@UtilityClass
public class PoiUtil {

	private final Logger log = LoggerFactory.getLogger(PoiTemplate.class);

	private static final PoiTemplate INSTANCE = PoiTemplate.INSTANCE;

	/**
	 * Renders a Word document from source file to destination file using label data.
	 * @param source the source Word template file
	 * @param dest the destination file to write the rendered document
	 * @param contents the list of label data to render
	 */
	public void renderWord(File source, File dest, List<LabelData> contents) {
		INSTANCE.renderWord(source, dest, contents);
	}

	/**
	 * Renders a Word document from source file to destination file using label data and
	 * custom configuration.
	 * @param source the source Word template file
	 * @param dest the destination file to write the rendered document
	 * @param contents the list of label data to render
	 * @param configure the custom configuration to use
	 */
	public void renderWord(File source, File dest, List<LabelData> contents, Configure configure) {
		INSTANCE.renderWord(source, dest, contents, configure);
	}

	/**
	 * Renders a Word document from input stream to output stream using label data.
	 * @param inputStream the input stream containing the Word template
	 * @param outputStream the output stream to write the rendered document
	 * @param contents the list of label data to render
	 */
	public void renderWord(InputStream inputStream, OutputStream outputStream, List<LabelData> contents) {
		INSTANCE.renderWord(inputStream, outputStream, contents);
	}

	/**
	 * Renders a Word document from source file to destination file using data map.
	 * @param source the source Word template file
	 * @param dest the destination file to write the rendered document
	 * @param data the data map containing template variables
	 */
	public void renderWord(File source, File dest, Map<String, Object> data) {
		INSTANCE.renderWord(source, dest, data);
	}

	/**
	 * Renders a Word document from source file to destination file using data map and
	 * custom configuration.
	 * @param source the source Word template file
	 * @param dest the destination file to write the rendered document
	 * @param data the data map containing template variables
	 * @param configure the custom configuration to use
	 */
	public void renderWord(File source, File dest, Map<String, Object> data, Configure configure) {
		INSTANCE.renderWord(source, dest, data, configure);
	}

	/**
	 * Renders a Word document from input stream to output stream using label data and
	 * custom configuration.
	 * @param inputStream the input stream containing the Word template
	 * @param outputStream the output stream to write the rendered document
	 * @param contents the list of label data to render
	 * @param configure the custom configuration to use
	 */
	public void renderWord(InputStream inputStream, OutputStream outputStream, List<LabelData> contents,
			Configure configure) {
		INSTANCE.renderWord(inputStream, outputStream, contents, configure);
	}

	/**
	 * Renders a Word document from input stream to output stream using data map.
	 * @param inputStream the input stream containing the Word template
	 * @param outputStream the output stream to write the rendered document
	 * @param dataMap the data map containing template variables
	 */
	public void renderWord(InputStream inputStream, OutputStream outputStream, Map<String, Object> dataMap) {
		INSTANCE.renderWord(inputStream, outputStream, dataMap);
	}

	/**
	 * Renders a Word document from input stream to output stream using data map and
	 * custom configuration.
	 * @param inputStream the input stream containing the Word template
	 * @param outputStream the output stream to write the rendered document
	 * @param dataMap the data map containing template variables
	 * @param configure the custom configuration to use
	 */
	public void renderWord(InputStream inputStream, OutputStream outputStream, Map<String, Object> dataMap,
			Configure configure) {
		INSTANCE.renderWord(inputStream, outputStream, dataMap, configure);
	}

	/**
	 * Extracts template elements from the input stream using default configuration.
	 * @param inputStream the input stream containing the Word template
	 * @return list of element metadata
	 */
	public List<ElementMeta> templateElement(InputStream inputStream) {
		return INSTANCE.templateElement(inputStream);
	}

	/**
	 * Extracts template elements from the input stream using custom configuration.
	 * @param inputStream the input stream containing the Word template
	 * @param configure the custom configuration to use
	 * @return list of element metadata
	 */
	public List<ElementMeta> templateElement(InputStream inputStream, Configure configure) {
		return INSTANCE.templateElement(inputStream, configure);
	}

	/**
	 * Renders the template using the provided render function.
	 * @param <R> the type of the render result
	 * @param inputStream the input stream containing the Word template
	 * @param configure the configuration to use
	 * @param function the render function to apply
	 * @return the result of the render function
	 */
	public <R> R render(InputStream inputStream, Configure configure, RenderFunction<R> function) {
		return INSTANCE.render(inputStream, configure, function);
	}

}
