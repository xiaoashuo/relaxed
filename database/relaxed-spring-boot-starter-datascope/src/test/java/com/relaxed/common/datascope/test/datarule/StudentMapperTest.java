package com.relaxed.common.datascope.test.datarule;

import com.relaxed.common.datascope.DataScope;
import com.relaxed.common.datascope.DataScopeAutoConfiguration;
import com.relaxed.common.datascope.handler.DataPermissionHandler;
import com.relaxed.common.datascope.handler.DataPermissionRule;
import com.relaxed.common.datascope.test.datarule.config.DataPermissionRuleTestConfiguration;
import com.relaxed.common.datascope.test.datarule.config.DataSourceConfiguration;
import com.relaxed.common.datascope.test.datarule.datascope.ClassDataScope;
import com.relaxed.common.datascope.test.datarule.datascope.SchoolDataScope;
import com.relaxed.common.datascope.test.datarule.entity.Student;
import com.relaxed.common.datascope.test.datarule.service.StudentService;
import com.relaxed.common.datascope.test.datarule.user.LoginUser;
import com.relaxed.common.datascope.test.datarule.user.LoginUserHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

@Sql({ "/student.ddl.sql", "/student.insert.sql" })
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { DataSourceConfiguration.class, DataPermissionRuleTestConfiguration.class,
		DataScopeAutoConfiguration.class, MybatisAutoConfiguration.class })
@MapperScan("com.hccake.ballcat.common.datascope.test.datarule.mapper")
class StudentMapperTest {

	@Autowired
	private StudentService studentService;

	@Autowired
	private DataPermissionHandler dataPermissionHandler;

	@BeforeEach
	void login() {
		// 设置当前登录用户权限
		LoginUser loginUser = new LoginUser();
		loginUser.setSchoolNameList(Collections.singletonList("实验中学"));
		loginUser.setClassNameList(Collections.singletonList("一班"));
		LoginUserHolder.set(loginUser);
	}

	@AfterEach
	void clear() {
		LoginUserHolder.remove();
	}

	@Test
	void testSelect1() {
		// 实验中学，一班总共有 2 名学生
		List<Student> studentList1 = studentService.listStudent();
		Assertions.assertEquals(2, studentList1.size());

		// 更改当前登录用户权限，让它只能看德高中学的学生
		LoginUser loginUser = LoginUserHolder.get();
		loginUser.setSchoolNameList(Collections.singletonList("德高中学"));

		// 德高中学，一班总共有 3 名学生
		List<Student> studentList2 = studentService.listStudent();
		Assertions.assertEquals(3, studentList2.size());

		/* 忽略权限控制，一共有 10 名学生 */
		// 编程式忽略
		DataPermissionRule dataPermissionRule = new DataPermissionRule();
		dataPermissionRule.setIgnore(true);
		dataPermissionHandler.executeWithDataPermissionRule(dataPermissionRule,
				() -> Assertions.assertEquals(10, studentService.listStudent().size()));
		// 注解忽略权限
		List<Student> studentList4 = studentService.listStudentWithoutDataPermission();
		Assertions.assertEquals(10, studentList4.size());

		/* 只控制班级的数据权限，实验中学 + 德高中学 一班总共有 5 名学生 */
		// 编程式忽略
		DataPermissionRule dataPermissionRule1 = new DataPermissionRule();
		dataPermissionRule1.setIncludeResources(new String[] { ClassDataScope.RESOURCE_NAME });
		dataPermissionHandler.executeWithDataPermissionRule(dataPermissionRule1,
				() -> Assertions.assertEquals(5, studentService.listStudent().size()));
		// 注解忽略
		List<Student> studentList5 = studentService.listStudentOnlyFilterClass();
		Assertions.assertEquals(5, studentList5.size());

		/* 只控制学校的数据权限，"德高中学"、一班、二班 总共有 6 名学生 */
		DataPermissionRule dataPermissionRule2 = new DataPermissionRule();
		// 编程式忽略
		dataPermissionRule2.setIncludeResources(new String[] { SchoolDataScope.RESOURCE_NAME });
		dataPermissionHandler.executeWithDataPermissionRule(dataPermissionRule2,
				() -> Assertions.assertEquals(6, studentService.listStudent().size()));
		// 注解忽略
		List<Student> studentList6 = studentService.listStudentOnlyFilterSchool();
		Assertions.assertEquals(6, studentList6.size());

	}

	/**
	 * 权限规则优先级： 同一方法内：编程式规则 > 当前方法上的注解规则 > 当前类上的注解规则 > 调用者所使用的权限规则 > 全局默认规则
	 */
	@Test
	void testRulePriority() {
		// 全局数据权限，默认是全部 DataScope 都控制
		List<Student> studentList = studentService.listStudent();
		Assertions.assertEquals(2, studentList.size());

		// 编程式数据权限，
		DataPermissionRule dataPermissionRule = new DataPermissionRule();
		dataPermissionRule.setIncludeResources(new String[] { ClassDataScope.RESOURCE_NAME });
		dataPermissionHandler.executeWithDataPermissionRule(dataPermissionRule, () -> {
			// 编程式数据权限内部方法，走指定的规则
			List<Student> studentList2 = studentService.listStudent();
			Assertions.assertEquals(5, studentList2.size());

			// 嵌套的权限控制
			DataPermissionRule dataPermissionRule1 = new DataPermissionRule();
			dataPermissionRule1.setIgnore(true);
			dataPermissionHandler.executeWithDataPermissionRule(dataPermissionRule1, () -> {
				// 规则嵌套时，优先使用内部规则
				List<Student> studentList1 = studentService.listStudent();
				Assertions.assertEquals(10, studentList1.size());

				// 由于调用的方法上添加了注解，走该方法注解的权限规则
				List<Student> students1 = studentService.listStudentOnlyFilterClass();
				Assertions.assertEquals(5, students1.size());
				// 注解权限控制
				List<Student> students2 = studentService.listStudentOnlyFilterSchool();
				Assertions.assertEquals(4, students2.size());
			});
		});
	}

	@Test
	void testExecuteWithDataPermissionRule() {

		DataPermissionRule dataPermissionRule = new DataPermissionRule();
		dataPermissionRule.setIgnore(true);
		dataPermissionHandler.executeWithDataPermissionRule(dataPermissionRule, () -> {
			List<DataScope> dataScopes = dataPermissionHandler.filterDataScopes(null);
			Assertions.assertTrue(dataScopes.isEmpty());
		});

		DataPermissionRule dataPermissionRule1 = new DataPermissionRule();
		dataPermissionRule1.setIncludeResources(new String[] { ClassDataScope.RESOURCE_NAME });
		dataPermissionHandler.executeWithDataPermissionRule(dataPermissionRule1, () -> {
			List<DataScope> dataScopes = dataPermissionHandler.filterDataScopes(null);
			Assertions.assertFalse(dataScopes.isEmpty());
			Assertions.assertEquals(1, dataScopes.size());
			Assertions.assertEquals(ClassDataScope.RESOURCE_NAME, dataScopes.get(0).getResource());
		});

		DataPermissionRule dataPermissionRule2 = new DataPermissionRule();
		dataPermissionRule2.setExcludeResources(new String[] { SchoolDataScope.RESOURCE_NAME });
		dataPermissionHandler.executeWithDataPermissionRule(dataPermissionRule2, () -> {
			List<DataScope> dataScopes = dataPermissionHandler.filterDataScopes(null);
			Assertions.assertFalse(dataScopes.isEmpty());
			Assertions.assertEquals(1, dataScopes.size());
			Assertions.assertEquals(ClassDataScope.RESOURCE_NAME, dataScopes.get(0).getResource());
		});
	}

}