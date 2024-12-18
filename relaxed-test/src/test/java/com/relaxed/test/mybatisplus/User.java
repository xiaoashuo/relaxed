package com.relaxed.test.mybatisplus;

import com.relaxed.extend.mybatis.plus.alias.TableAlias;
import lombok.Data;

/**
 * @author Yakir
 * @Topic User
 * @Description
 * @date 2021/12/29 18:06
 * @Version 1.0
 */
@TableAlias("t")
@Data
public class User {

	private String username;

	private String sex;

	private Integer age;

}
