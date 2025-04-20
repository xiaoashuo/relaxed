package com.relaxed.poi.domain;

import com.deepoove.poi.data.AttachmentType;
import lombok.Data;

import java.io.File;

/**
 * 附件内容数据类，用于存储 Word 文档中的附件数据。 支持不同类型的附件文件，支持链式调用。
 *
 * @author Yakir
 * @since 1.0.0
 */
@Data
public class AttachmentContentData extends LabelData {

	/**
	 * 本地附件文件，用于从本地文件系统加载附件
	 */
	private File localFile;

	/**
	 * 附件类型，用于指定附件的文件类型
	 */
	private AttachmentType attachmentType;

}
