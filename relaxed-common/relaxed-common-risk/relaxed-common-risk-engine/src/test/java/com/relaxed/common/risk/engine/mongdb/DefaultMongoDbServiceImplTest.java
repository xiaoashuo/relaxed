package com.relaxed.common.risk.engine.mongdb;

import com.relaxed.common.risk.engine.EngineApplication;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Yakir
 * @Topic DefaultMongoDbServiceImplTest
 * @Description
 * @date 2021/8/29 15:19
 * @Version 1.0
 */

@SpringBootTest(classes = EngineApplication.class)
class DefaultMongoDbServiceImplTest {

	@Autowired
	private MongoDbService mongoDbService;

	@Test
	void count() {
		long entity_255 = mongoDbService.count("entity_255");
		System.out.println(entity_255);
	}

}