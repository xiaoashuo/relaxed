package com.relaxed.poi.funcs;

import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.Attachments;
import com.deepoove.poi.policy.AttachmentRenderPolicy;
import com.deepoove.poi.util.ByteUtils;
import com.relaxed.poi.domain.AttachmentContentData;
import com.relaxed.poi.enums.ContentTypeEnum;

/**
 * @author Yakir
 * @Topic TextContentRender
 * @Description
 * @date 2024/3/25 14:17
 * @Version 1.0
 */
public class AttachmentContentRender implements WordContentRender<AttachmentContentData> {

	@Override
	public String contentType() {
		return ContentTypeEnum.ATTACHMENT.name();
	}

	@Override
	public Object render(Configure configure, AttachmentContentData data) {
		// 注册渲染策略
		configure.customPolicy(data.getLabelName(), new AttachmentRenderPolicy());
		return Attachments.ofBytes(ByteUtils.getLocalByteArray(data.getLocalFile()), data.getAttachmentType()).create();
	}

}
