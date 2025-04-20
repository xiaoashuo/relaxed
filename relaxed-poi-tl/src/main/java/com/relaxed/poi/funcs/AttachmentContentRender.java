package com.relaxed.poi.funcs;

import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.Attachments;
import com.deepoove.poi.policy.AttachmentRenderPolicy;
import com.deepoove.poi.util.ByteUtils;
import com.relaxed.poi.domain.AttachmentContentData;
import com.relaxed.poi.enums.ContentTypeEnum;

/**
 * A content render implementation for handling attachment content in Word documents. This
 * renderer processes attachment data from local files and converts it into a format
 * suitable for Word document generation.
 *
 * @author Yakir
 * @since 1.0.0
 */
public class AttachmentContentRender implements WordContentRender<AttachmentContentData> {

	/**
	 * Returns the content type identifier for attachments.
	 * @return the content type as defined in ContentTypeEnum
	 */
	@Override
	public String contentType() {
		return ContentTypeEnum.ATTACHMENT.name();
	}

	/**
	 * Renders the attachment content data and configures the appropriate attachment
	 * render policy. Registers a custom policy for handling attachments and converts the
	 * local file into bytes.
	 * @param configure the configuration for rendering
	 * @param data the attachment content data to render
	 * @return an Attachments object containing the rendered attachment data
	 */
	@Override
	public Object render(Configure configure, AttachmentContentData data) {
		// Register the render policy
		configure.customPolicy(data.getLabelName(), new AttachmentRenderPolicy());
		return Attachments.ofBytes(ByteUtils.getLocalByteArray(data.getLocalFile()), data.getAttachmentType()).create();
	}

}
