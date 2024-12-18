package com.relaxed.test.mybatisplus;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.relaxed.extend.mybatis.plus.conditions.query.LambdaAliasQueryWrapperX;
import com.relaxed.extend.mybatis.plus.conditions.query.LambdaQueryWrapperX;
import com.relaxed.extend.mybatis.plus.toolkit.WrappersX;
import org.apache.ibatis.builder.MapperBuilderAssistant;

/**
 * @author Yakir
 * @Topic Test
 * @Description
 * @date 2023/9/16 10:35
 * @Version 1.0
 */
public class Test {

	public static void main(String[] args) {
		TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), User.class);
		LambdaQueryWrapperX<User> eq = WrappersX.lambdaQueryX(User.class).eq(User::getUsername, "1")

				.eq(User::getSex, User::getAge).ne(User::getSex, User::getAge).gt(User::getUsername, User::getAge)
				.ge(User::getUsername, User::getAge).lt(User::getUsername, User::getAge)
				.le(User::getUsername, User::getAge);
		String sqlSegment = eq.getCustomSqlSegment();
		System.out.println(sqlSegment);

		LambdaAliasQueryWrapperX<User> wrapperX = WrappersX.lambdaAliasQueryX(User.class);
		wrapperX.eq(User::getUsername, 1).eq(User::getSex, User::getAge).ne(User::getSex, User::getAge)
				.gt(User::getUsername, User::getAge).ge(User::getUsername, User::getAge)
				.lt(User::getUsername, User::getAge).le(User::getUsername, User::getAge);
		String lambdaSqlSegment = wrapperX.getCustomSqlSegment();
		System.out.println(lambdaSqlSegment);

	}

}
