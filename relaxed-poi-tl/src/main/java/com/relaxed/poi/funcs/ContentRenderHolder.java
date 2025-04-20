package com.relaxed.poi.funcs;

import com.relaxed.poi.domain.LabelData;
import com.relaxed.poi.enums.ContentTypeEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * A holder class for managing content renderers in Word document generation. This class
 * maintains a registry of content renderers for different content types and provides
 * methods for registration and retrieval of renderers.
 *
 * @author Yakir
 * @since 1.0.0
 */
public class ContentRenderHolder {

	/**
	 * A map that stores content renderers indexed by their content type.
	 */
	private final Map<String, WordContentRender> TYPE_BACK_DATA = new HashMap<>();

	/**
	 * Registers a content renderer for the specified content type.
	 * @param contentType the type of content the renderer handles
	 * @param wordContentRender the renderer to register
	 * @param <T> the type of label data the renderer can process
	 */
	public <T extends LabelData> void register(String contentType, WordContentRender<T> wordContentRender) {
		TYPE_BACK_DATA.put(contentType, wordContentRender);
	}

	/**
	 * Retrieves the content renderer for the specified content type.
	 * @param contentType the type of content to get the renderer for
	 * @param <T> the type of label data the renderer can process
	 * @return the content renderer for the specified type, or null if not found
	 */
	public <T extends LabelData> WordContentRender<T> getRender(String contentType) {
		return TYPE_BACK_DATA.get(contentType);
	}

	/**
	 * Initializes the content render holder with default renderers for all supported
	 * content types. This method registers renderers for text, pictures, tables, lists,
	 * loop row tables, attachments, and HTML content.
	 */
	public void init() {
		this.register(ContentTypeEnum.TEXT.name(), new TextContentRender());
		this.register(ContentTypeEnum.PICTURE.name(), new PicContentRender());
		this.register(ContentTypeEnum.TABLE.name(), new TableContentRender());
		this.register(ContentTypeEnum.LIST.name(), new ListContentRender());
		this.register(ContentTypeEnum.LOOP_ROW_TABLE.name(), new LoopRowTableContentRender());
		this.register(ContentTypeEnum.ATTACHMENT.name(), new AttachmentContentRender());
		this.register(ContentTypeEnum.HTML.name(), new HtmlContentRender());
	}

}
