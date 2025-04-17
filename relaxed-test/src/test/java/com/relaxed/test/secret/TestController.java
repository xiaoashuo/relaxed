package com.relaxed.test.secret;

import com.relaxed.common.secret.annotation.RequestDecrypt;
import com.relaxed.common.secret.annotation.ResponseEncrypt;
import org.springframework.web.bind.annotation.*;

/**
 * @author Yakir
 * @Topic TestController
 * @Description
 * @date 2021/11/15 10:41
 * @Version 1.0
 */
@RequestMapping("/test")
@RestController
public class TestController {

	@RequestDecrypt
	@ResponseEncrypt
	@PostMapping("/ab")
	public BaseDto test(@RequestBody BaseDto<String> ba) {
		BaseDto baseDto = new BaseDto();
		baseDto.setContent("afsafsasfa");
		return baseDto;
	}

}
