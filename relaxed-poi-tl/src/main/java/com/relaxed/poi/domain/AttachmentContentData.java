package com.relaxed.poi.domain;

import com.deepoove.poi.data.AttachmentType;
import lombok.Data;

import java.io.File;

/**
 * @author Yakir
 * @Topic AttachmentContentData
 * @Description
 * @date 2024/3/29 11:41
 * @Version 1.0
 */
@Data
public class AttachmentContentData extends LabelData {

	/**
	 * 本地文件
	 */
	private File localFile;

	/**
	 * 附件类型
	 */

	private AttachmentType attachmentType;

}
