# Mybatis映射Map转换

## 简言

基于mybatis扩展Map返回类型转换器

## 使用方式

### 一、依赖引入

```xml
        <dependency>
            <groupId>com.lovecyy</groupId>
            <artifactId>relaxed-extend-mybatis</artifactId>
            <version>${version}</version>
        </dependency>
```

### 二、注册拦截器

```java
    @Bean
    public MapResultInterceptor mapResultInterceptor(){
        return new MapResultInterceptor(new DefaultMapResultHandler());
    }
```

### 三、定义Mapper

#### 注解MapResult

- key 指定提取参数key
- valueNames 数组  指定提取列的名称，默认提取全部列
- valJoint 值拼接符 默认为 -

#### 1.User

```java
@Data
public class User {
    private Long id;
    private String name;
    private Integer age;
    private String email;
}

```

#### 2.UserMapper.java

```java
    @MapResult(key = "id",valueNames = {"age"},valJoint = "-")
    //根据Map 参数化类型 确定 返回结果类型
    // 1.提取key+整型 此时 valNames 只能为1列基本数据类型
    //  Map<String,Integer> selectMap(); 
    // 2.提取key+字符串 此时 valNames 支持多列 并用分隔符拼接
    //  Map<String,String> selectMap(); 
    // 3.提取key+对象 此时 valNames 支持多列 默认值类型为Map
    //  Map selectMap(); 
    Map selectMap();
```

#### 3.UserMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.example.mybatis.map.demo.mapper.UserMapper">

	<select id="selectMap" resultType="com.example.mybatis.map.demo.domain.User">
         select * from user
	</select>

</mapper>
```

### 四、测试

```java
@SpringBootTest
class MybatisMapDemoApplicationTests {
    @Autowired
    private UserMapper userMapper;
    @Test
    void contextLoads() {
        Map map = userMapper.selectMap();
        System.out.println(map);
    }

}

```



