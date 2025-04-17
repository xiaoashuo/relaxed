package com.relaxed.test.web.exception;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.relaxed.extend.mybatis.plus.conditions.update.LambdaUpdateWrapperX;
import com.relaxed.extend.mybatis.plus.toolkit.WrappersX;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

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

		TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), User.class);
		User user = new User();
		LambdaUpdateWrapperX<User> as = WrappersX.lambdaUpdateX(user).eq(User::getUsername, "as")
				.set(User::getUsername, "asaaa")

				.eqIfPresent(User::getAge, null);

		System.out.println(as);
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
