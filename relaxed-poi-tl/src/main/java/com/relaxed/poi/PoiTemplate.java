package com.relaxed.poi;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.template.ElementTemplate;
import com.deepoove.poi.template.MetaTemplate;
import com.deepoove.poi.util.PoitlIOUtils;
import com.relaxed.poi.domain.ElementMeta;
import com.relaxed.poi.domain.LabelData;
import com.relaxed.poi.exception.WordException;
import com.relaxed.poi.funcs.RenderFunction;
import com.relaxed.poi.funcs.WordContentRender;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Main template class for POI-TL that handles Word document rendering and template
 * processing. This class provides methods for rendering Word documents with various data
 * sources and configurations.
 *
 * @author Yakir
 * @since 1.0
 */
public class PoiTemplate {

	/**
	 * Singleton instance of PoiTemplate.
	 */
	public static final PoiTemplate INSTANCE = new PoiTemplate();

	private PoiTemplate() {
	}

	/**
	 * Renders a Word document from source file to destination file using label data.
	 * @param source the source Word template file
	 * @param dest the destination file to write the rendered document
	 * @param contents the list of label data to render
	 */
	public void renderWord(File source, File dest, List<LabelData> contents) {
		renderWord(source, dest, contents, PoiGlobalConfig.defaultConfigure());
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
		try {
			renderWord(Files.newInputStream(source.toPath()), Files.newOutputStream(dest.toPath()), contents,
					configure);
		}
		catch (Exception e) {
			throw new WordException(e);
		}
	}

	/**
	 * Renders a Word document from source file to destination file using data map.
	 * @param source the source Word template file
	 * @param dest the destination file to write the rendered document
	 * @param data the data map containing template variables
	 */
	public void renderWord(File source, File dest, Map<String, Object> data) {
		renderWord(source, dest, data, PoiGlobalConfig.defaultConfigure());
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
		try {
			renderWord(Files.newInputStream(source.toPath()), Files.newOutputStream(dest.toPath()), data, configure);
		}
		catch (Exception e) {
			throw new WordException(e);
		}
	}

	/**
	 * Renders a Word document from input stream to output stream using label data.
	 * @param inputStream the input stream containing the Word template
	 * @param outputStream the output stream to write the rendered document
	 * @param contents the list of label data to render
	 */
	public void renderWord(InputStream inputStream, OutputStream outputStream, List<LabelData> contents) {
		renderWord(inputStream, outputStream, contents, PoiGlobalConfig.defaultConfigure());
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
		HashMap<String, Object> dataMap = new HashMap<String, Object>(contents.size()) {
		};
		contents.forEach(content -> {
			WordContentRender backData = PoiGlobalConfig.contentRenderHolder().getRender(content.getContentType());
			dataMap.put(content.getLabelName(), backData.render(configure, content));
		});
		renderWord(inputStream, outputStream, dataMap, configure);
	}

	/**
	 * Renders a Word document from input stream to output stream using data map.
	 * @param inputStream the input stream containing the Word template
	 * @param outputStream the output stream to write the rendered document
	 * @param dataMap the data map containing template variables
	 */
	public void renderWord(InputStream inputStream, OutputStream outputStream, Map<String, Object> dataMap) {
		renderWord(inputStream, outputStream, dataMap, PoiGlobalConfig.defaultConfigure());
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
		XWPFTemplate xwpfTemplate = getXwpfTemplate(inputStream, configure);
		XWPFTemplate template = xwpfTemplate.render(dataMap);
		try {
			template.write(outputStream);
			outputStream.flush();
		}
		catch (Exception e) {
			throw new WordException(e);
		}
		finally {
			PoitlIOUtils.closeQuietlyMulti(xwpfTemplate, outputStream);
		}
	}

	/**
	 * Extracts template elements from the input stream using default configuration.
	 * @param inputStream the input stream containing the Word template
	 * @return list of element metadata
	 */
	public List<ElementMeta> templateElement(InputStream inputStream) {
		return templateElement(inputStream, PoiGlobalConfig.defaultConfigure());
	}

	/**
	 * Extracts template elements from the input stream using custom configuration.
	 * @param inputStream the input stream containing the Word template
	 * @param configure the custom configuration to use
	 * @return list of element metadata
	 */
	public List<ElementMeta> templateElement(InputStream inputStream, Configure configure) {
		RenderFunction<List<ElementMeta>> renderFunction = xwpfTemplate -> {
			List<MetaTemplate> elementTemplates = xwpfTemplate.getElementTemplates();
			return convertToElementMetas(elementTemplates);
		};
		return render(inputStream, configure, renderFunction);
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
		XWPFTemplate xwpfTemplate = getXwpfTemplate(inputStream, configure);
		try {
			return function.render(xwpfTemplate);
		}
		finally {
			PoitlIOUtils.closeQuietlyMulti(xwpfTemplate);
		}
	}

	/**
	 * Creates a new XWPFTemplate instance from the input stream and configuration.
	 * @param inputStream the input stream containing the Word template
	 * @param configure the configuration to use
	 * @return the compiled XWPFTemplate instance
	 */
	private XWPFTemplate getXwpfTemplate(InputStream inputStream, Configure configure) {
		return XWPFTemplate.compile(inputStream, configure);
	}

	/**
	 * Converts element templates to element metadata, removing duplicates.
	 * @param elementTemplates the list of element templates to convert
	 * @return list of unique element metadata
	 */
	protected List<ElementMeta> convertToElementMetas(List<MetaTemplate> elementTemplates) {
		List<ElementMeta> elementMetas = new ArrayList<>();
		Set<String> elementNames = new HashSet<>();
		Integer i = 0;
		for (MetaTemplate metaTemplate : elementTemplates) {
			if (metaTemplate instanceof ElementTemplate) {
				ElementTemplate elementTemplate = (ElementTemplate) metaTemplate;
				String tagName = elementTemplate.getTagName();
				String source = elementTemplate.getSource();
				if (elementNames.contains(tagName)) {
					continue;
				}
				elementNames.add(tagName);
				elementMetas.add(new ElementMeta(i++, tagName, source));
			}
		}
		return elementMetas;
	}

}
