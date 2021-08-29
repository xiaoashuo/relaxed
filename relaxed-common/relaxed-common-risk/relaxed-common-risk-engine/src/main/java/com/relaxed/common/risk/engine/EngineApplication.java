package com.relaxed.common.risk.engine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Yakir
 * @Topic EngineApplication
 * @Description
 * @date 2021/8/29 15:36
 * @Version 1.0
 */
@SpringBootApplication(scanBasePackages = "com.relaxed")
public class EngineApplication {

	public static void main(String[] args) {
		SpringApplication.run(EngineApplication.class, args);
	}

}
