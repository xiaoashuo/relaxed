package com.relaxed.test.datascope.datarule.service;

import com.relaxed.common.datascope.annotation.DataPermission;

import com.relaxed.test.datascope.datarule.datascope.ClassDataScope;
import com.relaxed.test.datascope.datarule.datascope.SchoolDataScope;
import com.relaxed.test.datascope.datarule.entity.Student;
import com.relaxed.test.datascope.datarule.mapper.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hccake
 */
@Service
public class StudentService {

	@Autowired
	private StudentMapper studentMapper;

	public List<Student> listStudent() {
		return studentMapper.listStudent();
	}

	@DataPermission(includeResources = ClassDataScope.RESOURCE_NAME)
	public List<Student> listStudentOnlyFilterClass() {
		return studentMapper.listStudent();
	}

	@DataPermission(includeResources = SchoolDataScope.RESOURCE_NAME)
	public List<Student> listStudentOnlyFilterSchool() {
		return studentMapper.listStudent();
	}

	@DataPermission(ignore = true)
	public List<Student> listStudentWithoutDataPermission() {
		return studentMapper.listStudent();
	}

}
