package com.relaxed.test.util.file;

import cn.hutool.json.JSONUtil;
import com.relaxed.common.core.util.file.FileConfig;
import com.relaxed.common.core.util.file.FileConstants;
import com.relaxed.common.core.util.file.FileHandler;
import com.relaxed.common.core.util.file.FileHandlerLoader;
import com.relaxed.common.core.util.file.FileMeta;
import com.relaxed.common.core.util.file.FileMultipartFile;
import com.relaxed.common.core.util.file.FileStoreUtils;
import com.relaxed.common.core.util.file.LocalFileHandler;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.File;

/**
 * @author Yakir
 * @Topic FileTest
 * @Description
 * @date 2023/5/17 11:17
 * @Version 1.0
 */
@Slf4j
public class FileTest {

	@Test
	void fileUpload() {
		String rootPath = "D:\\other\\100000";
		String relativePath = "file";
		String uploadFilePath = "D:\\other\\100000\\jieqingzhengming_131331131313.pdf";
		FileMultipartFile fileMultipartFile = new FileMultipartFile("file", new File(uploadFilePath));
		FileMeta fileMeta = FileStoreUtils.upload(rootPath, relativePath, fileMultipartFile,
				FileConfig.create().splitDate(true));
		log.info("upload  successfully,result:[{}] ,相对路径[{}],完整路径[{}]", JSONUtil.toJsonStr(fileMeta),
				fileMeta.getRelativeFilePath(), fileMeta.getFullFilePath());
	}

	@Test
	void fileDelete() {
		String rootPath = "D:\\other\\100000\\file\\";
		String relativePath = "20250623";
		boolean deleted = FileStoreUtils.delete("local", rootPath, relativePath);
		log.info("deleted  successfully,result:[{}]", deleted);
	}

	@Test
	void fileIsExist() {
		String rootPath = "D:\\other";
		String relativePath = "PJ3030230475525459968_REPAY_DETAI9L.xlsx";
		boolean exist = FileStoreUtils.isExist("local", rootPath, relativePath);
		log.info("file is exist check  successfully,result:[{}]", exist);
	}

	@Test
	void fileRealObjGet() {
		// 本地客户端默认没有任何方法
		LocalFileHandler.LocalFileClient localClient = FileStoreUtils.getTargetObject("local",
				LocalFileHandler.LocalFileClient.class);

		log.info("file is exist check  successfully,result:[{}]", localClient);
	}

	@Test
	void fileLoad() {
		FileHandler test = FileHandlerLoader.load(FileConstants.DEFAULT_HANDLE_TYPE);
		System.out.println(test);
	}

}
