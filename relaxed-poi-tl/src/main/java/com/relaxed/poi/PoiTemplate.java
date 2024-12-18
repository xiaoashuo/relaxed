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
 * @author Yakir
 * @Topic PoiUtil
 * @Description
 * @date 2024/3/25 13:58
 * @Version 1.0
 */

public class PoiTemplate {

	public static final PoiTemplate INSTANCE = new PoiTemplate();

	private PoiTemplate() {

	}

	public void renderWord(File source, File dest, List<LabelData> contents) {
		renderWord(source, dest, contents, PoiGlobalConfig.defaultConfigure());
	}

	public void renderWord(File source, File dest, List<LabelData> contents, Configure configure) {
		try {
			renderWord(Files.newInputStream(source.toPath()), Files.newOutputStream(dest.toPath()), contents,
					configure);
		}
		catch (Exception e) {
			throw new WordException(e);
		}

	}

	public void renderWord(File source, File dest, Map<String, Object> data) {
		renderWord(source, dest, data, PoiGlobalConfig.defaultConfigure());
	}

	public void renderWord(File source, File dest, Map<String, Object> data, Configure configure) {
		try {
			renderWord(Files.newInputStream(source.toPath()), Files.newOutputStream(dest.toPath()), data, configure);
		}
		catch (Exception e) {
			throw new WordException(e);
		}
	}

	public void renderWord(InputStream inputStream, OutputStream outputStream, List<LabelData> contents) {
		renderWord(inputStream, outputStream, contents, PoiGlobalConfig.defaultConfigure());
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
		HashMap<String, Object> dataMap = new HashMap<String, Object>(contents.size()) {
		};
		contents.forEach(content -> {
			WordContentRender backData = PoiGlobalConfig.contentRenderHolder().getRender(content.getContentType());
			dataMap.put(content.getLabelName(), backData.render(configure, content));
		});
		renderWord(inputStream, outputStream, dataMap, configure);
	}

	public void renderWord(InputStream inputStream, OutputStream outputStream, Map<String, Object> dataMap) {
		renderWord(inputStream, outputStream, dataMap, PoiGlobalConfig.defaultConfigure());
	}

	public void renderWord(InputStream inputStream, OutputStream outputStream, Map<String, Object> dataMap,
			Configure configure) {
		XWPFTemplate xwpfTemplate = getXwpfTemplate(inputStream, configure);
		XWPFTemplate template = xwpfTemplate.render(dataMap);
		try {
			// 获取word原始模板页数 注意，如果文档中有分节符或者分页符，那么获取的页数可能会不准确。
			// NiceXWPFDocument xwpfDocument = template.getXWPFDocument();
			// POIXMLProperties properties = xwpfDocument.getProperties();
			// POIXMLProperties.ExtendedProperties extendedProperties =
			// properties.getExtendedProperties();
			// int pages = extendedProperties.getPages();
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

	public List<ElementMeta> templateElement(InputStream inputStream) {
		return templateElement(inputStream, PoiGlobalConfig.defaultConfigure());
	}

	public List<ElementMeta> templateElement(InputStream inputStream, Configure configure) {
		RenderFunction<List<ElementMeta>> renderFunction = xwpfTemplate -> {
			List<MetaTemplate> elementTemplates = xwpfTemplate.getElementTemplates();
			return convertToElementMetas(elementTemplates);
		};
		return render(inputStream, configure, renderFunction);
	}

	public <R> R render(InputStream inputStream, Configure configure, RenderFunction<R> function) {
		XWPFTemplate xwpfTemplate = getXwpfTemplate(inputStream, configure);
		try {
			return function.render(xwpfTemplate);
		}
		finally {
			PoitlIOUtils.closeQuietlyMulti(xwpfTemplate);
		}
	}

	private XWPFTemplate getXwpfTemplate(InputStream inputStream, Configure configure) {
		return XWPFTemplate.compile(inputStream, configure);
	}

	/**
	 * 将获取到去重后的word模板元素
	 * @author yakir
	 * @date 2022/4/18 17:21
	 * @param elementTemplates
	 * @return java.util.List<com.relaxed.third.util.word.domain.ElementMeta>
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
