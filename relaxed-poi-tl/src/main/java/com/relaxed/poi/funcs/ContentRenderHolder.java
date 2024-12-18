package com.relaxed.poi.funcs;



import com.relaxed.poi.domain.LabelData;
import com.relaxed.poi.enums.ContentTypeEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Yakir
 * @Topic ContentRenderHolder
 * @Description
 * @date 2024/3/25 14:25
 * @Version 1.0
 */
public class ContentRenderHolder {

	private final Map<String, WordContentRender> TYPE_BACK_DATA = new HashMap<>();

	public <T extends LabelData> void register(String contentType, WordContentRender<T> wordContentRender) {
		TYPE_BACK_DATA.put(contentType, wordContentRender);
	}

	public <T extends LabelData> WordContentRender<T> getRender(String contentType) {
		return TYPE_BACK_DATA.get(contentType);
	}

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
