package com.relaxed.common.jsch.sftp.executor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Yakir
 * @Topic FileAttr
 * @Description
 * @date 2024/11/19 16:30
 * @Version 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FileAttr {

	private boolean isDir;

	private String fileName;

	private long modifyTime;

}
