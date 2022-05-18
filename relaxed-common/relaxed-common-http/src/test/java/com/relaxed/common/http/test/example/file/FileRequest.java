package com.relaxed.common.http.test.example.file;

import cn.hutool.core.codec.Base64;

import com.relaxed.common.http.core.request.AbstractRequest;
import com.relaxed.common.http.domain.HttpResponseWrapper;
import com.relaxed.common.http.domain.IHttpResponse;

import lombok.Data;

/**
 * @author Yakir
 * @Topic FileRequest
 * @Description
 * @date 2022/5/18 9:39
 * @Version 1.0
 */
@Data
public class FileRequest extends AbstractRequest<FileResponse> {

	private String fileNo;

	@Override
	public String getUrl(String baseUrl) {
		return baseUrl + "/stamp-app/stamp/download/pdf";
	}

	@Override
	public boolean isDownloadRequest() {
		return true;
	}

	@Override
	public FileResponse convertToResponse(IHttpResponse response) {
		byte[] fileStream = response.bodyBytes();
		FileResponse fileResponse = new FileResponse();
		fileResponse.setCode(200);
		fileResponse.setMessage("success");
		fileResponse.setFileContent(Base64.encode(fileStream));
		return fileResponse;
	}

}
