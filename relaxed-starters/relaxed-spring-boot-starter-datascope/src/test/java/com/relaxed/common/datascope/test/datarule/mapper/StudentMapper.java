package com.relaxed.common.datascope.test.datarule.mapper;

import com.relaxed.common.datascope.test.datarule.entity.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StudentMapper {

	@Select("select * from h2student")
	List<Student> listStudent();

}