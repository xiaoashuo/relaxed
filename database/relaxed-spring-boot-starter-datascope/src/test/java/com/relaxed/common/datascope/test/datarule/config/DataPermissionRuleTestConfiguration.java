package com.relaxed.common.datascope.test.datarule.config;

import com.relaxed.common.datascope.DataScope;
import com.relaxed.common.datascope.test.datarule.datascope.ClassDataScope;
import com.relaxed.common.datascope.test.datarule.datascope.SchoolDataScope;
import com.relaxed.common.datascope.test.datarule.service.StudentService;
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
