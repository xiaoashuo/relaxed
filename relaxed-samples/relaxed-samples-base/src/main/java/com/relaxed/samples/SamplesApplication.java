package com.relaxed.samples;

import com.relaxed.common.job.annotation.EnableXxlJob;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Yakir
 * @Topic SamplesApplication
 * @Description
 * @date 2021/6/21 11:29
 * @Version 1.0
 */
@EnableXxlJob
@SpringBootApplication(scanBasePackages = "com.relaxed")
public class SamplesApplication {

	public static void main(String[] args) {
		SpringApplication.run(SamplesApplication.class, args);
	}

}
