package com.relaxed.common.http.test.example.file;

import com.relaxed.common.http.core.response.BaseResponse;
import lombok.Data;

/**
 * @author Yakir
 * @Topic FileResponse
 * @Description
 * @date 2022/5/18 9:40
 * @Version 1.0
 */
@Data
public class FileResponse extends BaseResponse {

	private String fileContent;

}
