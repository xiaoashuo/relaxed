package com.relaxed.samples.controller;

import com.relaxed.common.jsch.sftp.client.ISftpClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Yakir
 * @Topic SftpController
 * @Description
 * @date 2021/6/21 11:41
 * @Version 1.0
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("sftp")
public class SftpController {

	private final ISftpClient iSftpClient;

	/**
	 * 查询文件列表不带返回值
	 * @param path
	 * @return
	 */
	@GetMapping("/list")
	public String list(String path) {
		iSftpClient.open(sftp -> {
			List<String> list = sftp.list(path);
			log.info("查询到文件列表{}", list);
		});
		return "ok";
	}

	/**
	 * 查询文件列表带返回值
	 * @param path
	 * @return
	 */
	@GetMapping("/supply/list")
	public List supplyList(String path) {
		return iSftpClient.supplyOpen(sftp -> sftp.list(path));
	}

}
