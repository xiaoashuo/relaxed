package com.relaxed.common.jsch.sftp.executor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件属性
 *
 * @author Yakir
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FileAttr {

	private boolean isDir;

	private String fileName;

	private long modifyTime;

}
