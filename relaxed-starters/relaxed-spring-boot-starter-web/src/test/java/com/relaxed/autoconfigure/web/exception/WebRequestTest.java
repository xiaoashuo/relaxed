package com.relaxed.autoconfigure.web.exception;

import com.baomidou.mybatisplus.annotation.Version;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 测试
 *
 * @author yakir
 * @date 2021/12/22 10:07
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("web")
@AutoConfigureMockMvc
public class WebRequestTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testWeb() throws Exception {
		MvcResult mvcResult = null;
		try {
			mvcResult = mockMvc.perform(get("/test/hello")).andReturn();
		}
		catch (Exception e) {

		}
		Thread.sleep(50000);
		System.out.println(mvcResult);

	}

}
