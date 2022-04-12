## MybatisPlus 模块

### 一、背景

官方网站: https://baomidou.com/

基于Mybatis plus插件上做的的二次封装，将原生的一些增强功能集成进来

**数据准备**

其对应的数据库 Schema 脚本如下：

```sql
DROP TABLE IF EXISTS user;

CREATE TABLE user
(
    id BIGINT(20) NOT NULL COMMENT '主键ID',
    name VARCHAR(30) NULL DEFAULT NULL COMMENT '姓名',
    PRIMARY KEY (id)
);
DROP TABLE IF EXISTS user_info;
CREATE TABLE user_info
(
    id BIGINT(20) NOT NULL COMMENT '主键ID',
    user_id BIGINT(20) NOT NULL COMMENT '用户ID',
    email VARCHAR(50) NULL DEFAULT NULL COMMENT '邮箱',
    PRIMARY KEY (id)
);
```

其对应的数据库 Data 脚本如下：

```sql
truncate table user;

INSERT INTO user (id, name) VALUES
(1, 'Jone'),
(2, 'Jack'),
(3, 'Tom'),
(4, 'Sandy'),
(5, 'Billie');
truncate table user_wallet;
INSERT INTO user (id,user_id, balance) VALUES
(1, 1,10),
(2, 2,20),
(3, 3,30)
;
```

### 二、引入坐标

```xml
<!--H2 内存数据库--> 
<dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>
<!--Mybatis plus 原始依赖--> 
<dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
  </dependency>
<!--Mybatis plus 扩展依赖--> 
<dependency>
      <groupId>com.lovecyy</groupId>
      <artifactId>relaxed-extend-mybatis-plus</artifactId>
     <version>${version}</version>
</dependency>        
```

### 三、application.yml

```yml
# DataSource Config
spring:
  datasource:
    driver-class-name: org.h2.Driver
    schema: classpath:db/schema-h2.sql
    data: classpath:db/data-h2.sql
    url: jdbc:h2:mem:test
    username: root
    password: test
    
mybatis-plus:
  mapper-locations: classpath:/mapper/**/*Mapper.xml
  global-config:
    banner: false
    db-config:
      id-type: auto
      insert-strategy: not_empty
      update-strategy: not_null
      logic-delete-field: valid
      logic-delete-value: now() # 逻辑已删除值(使用当前时间标识)
      logic-not-delete-value: 1 # 逻辑未删除值(默认为 0)
```

### 四、启动类加注解

```java
@SpringBootApplication
@MapperScan("com.relaxed.**.mapper")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
```

### 五、编码

编写实体类 `User.java`（此处使用了 [Lombok (opens new window)](https://www.projectlombok.org/)简化代码）

```java
@Data
public class User {
    private Long id;
    private String name;
}
```

编写实体类 `UserWallet.java`

```
@Data
public class UserWallet {
    private Long id;
    private Long userId;
    private Double balance;
}
```

编写 Mapper 包下的 `UserMapper`接口

```java
public interface UserMapper extends ExtendMapper<User> {
}
```

编写 Mapper 包下的 `UserWalletMapper`接口

```java
public interface UserWalletMapper extends ExtendMapper<UserWallet> {
}
```

### 六、开始使用

添加测试类，进行功能测试：

```java
@SpringBootTest
public class SampleTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testSelect() {
        System.out.println(("----- selectAll method test ------"));
        List<User> userList = userMapper.selectList(null);
        Assert.assertEquals(5, userList.size());
        userList.forEach(System.out::println);
    }

}

```

### 附加功能

#### 一、连表查询

##### 编码

修改`User.java`实体增进`TableAlias`注解

```java
@TableAlias("u")
@Data
public class User {
    private Long id;
    private String name;
}
```

修改 `UserMapper接口`

```java
public interface UserMapper extends ExtendMapper<User> {
    	IPage<User> selectByPage(IPage<User> page, @Param(Constants.WRAPPER) Wrapper wrapper);

}
```

修改`UserMapper.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.relaxed..UserMapper">
  <resultMap id="userMap" type="User">
                  <id property="id" column="id"/>
                        <result property="name" column="name"/>
            </resultMap>
	<sql id="Base_Column_List">
		id,        
		name       
	</sql>

	<sql id="Base_Alias_Column_List">
		u.id,        
		u.name       
     
	</sql>
    <select id="selectByPage" resultType="User">
		select <include refid="Base_Alias_Column_List"/>,uc.balance
from user u left join
    user_wallet uc on u.id=uc.user_id
		${ew.customSqlSegment}
	</select>
</mapper>
```

##### 开始测试

要求: 查询姓名为张三 钱包余额为10的用户

```java
@SpringBootTest
public class SampleTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testSelect() {
        //1.生成分页条件
        IPage<StampFilePageVO> page = this.prodPage(new PageParam());
        //2.构建别名表达式  默认主表别名根据 当前实体上的 TableAlias 获取  且注解必须存在
		LambdaAliasQueryWrapperX<User> aliasQueryWrapperX = WrappersX.lambdaAliasQueryX(User.class);
        //若要自定义主表别名 获取方式 则实现 TableAliasFunction函数即可 使用如下构造即可
        //LambdaAliasQueryWrapperX<StampFile> aliasQueryWrapperX = WrappersX.lambdaAliasQueryX(StampFile.class, clazz -> "u");
		//值存在   则加入此条件
        aliasQueryWrapperX.eqIfPresent(User::name,"张三");
		//无论值为什么条件都会加入  
        aliasQueryWrapperX.eq(OtherTableColumnAliasFunction.joinTableAliasColumn("uc.balance"),10);
		this.selectByPage(page, aliasQueryWrapperX);
		return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Data
    public static class PageParam {
        private long current = 1L;
        private long size = 10L;
    }
}

```



