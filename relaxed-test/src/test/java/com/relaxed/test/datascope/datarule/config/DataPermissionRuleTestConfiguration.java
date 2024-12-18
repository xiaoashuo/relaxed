package com.relaxed.test.datascope.datarule.config;

import com.relaxed.common.datascope.DataScope;

import com.relaxed.test.datascope.datarule.datascope.ClassDataScope;
import com.relaxed.test.datascope.datarule.datascope.SchoolDataScope;
import com.relaxed.test.datascope.datarule.service.StudentService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author hccake
 */
@EnableAspectJAutoProxy
@Configuration(proxyBeanMethods = false)
public class DataPermissionRuleTestConfiguration {

	@Bean
	public DataScope classDataScope() {
		return new ClassDataScope();
	}

	@Bean
	public DataScope orderDataScope() {
		return new SchoolDataScope();
	}

	@Bean
	public StudentService studentService() {
		return new StudentService();
	}

}
