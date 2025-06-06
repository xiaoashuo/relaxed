package com.relaxed.test.download.controller;

import cn.hutool.core.lang.Assert;
import com.relaxed.starter.download.annotation.ResponseDownload;
import com.relaxed.starter.download.domain.DownloadModel;
import com.relaxed.starter.download.enums.DownTypeEnum;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * DownloadController
 *
 * @author Yakir
 */
@RequestMapping("/test/download")
@RestController
public class DownloadController {

	@ResponseDownload(channel = DownTypeEnum.OSS)
	@GetMapping("oss")
	public DownloadModel oss(HttpServletResponse response, String dir, String filename) {
		DownloadModel downloadModel = new DownloadModel(dir + "/" + filename, "/");
		return downloadModel;
	}

	@ResponseDownload(channel = DownTypeEnum.SFTP)
	@GetMapping("sftp")
	public DownloadModel sftp(HttpServletResponse response, String dir, String filename) {
		DownloadModel downloadModel = new DownloadModel(dir + "/" + filename, "/");
		return downloadModel;
	}

	@ResponseDownload(channel = DownTypeEnum.LOCAL)
	@GetMapping("local")
	public DownloadModel local(HttpServletResponse response, String dir, String filename) {
		DownloadModel downloadModel = new DownloadModel(dir + "/" + filename, "/");
		// downloadModel.setDisplayFileName("attachment模式."+downloadModel.getFileType());
		return downloadModel;
	}

}
