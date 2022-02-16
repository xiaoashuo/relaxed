# Excel导入导出

## 简言

EasyExcel是一个基于Java的简单、省内存的读写Excel的开源项目。在尽可能节约内存的情况下支持读写百M的Excel。 64M内存1分钟内读取75M(46W行25列)的Excel,当然还有急速模式能更快，但是内存占用会在100M多一点

![img](http://pigx.vip/20200331165749_w0DXBK_Screenshot.jpeg)

## 一、依赖引用

```xml
  <dependency>
    <groupId>com.lovecyy</groupId>
    <artifactId>relaxed-spring-boot-starter-easyexcel</artifactId>
    <version>${version}</version>
</dependency>

```

## 二、导入Excel

- 接口类定义List 接受表格对应的数据 使用 @RequestExcel 标记

```java
@PostMapping("/upload")
public void upload(@RequestExcel List<DemoData> dataList, BindingResult bindingResult) {
  // JSR 303 校验通用校验获取失败的数据
  List<ErrorMessage> errorMessageList = (List<ErrorMessage>) bindingResult.getTarget();
}
```

- 实体声明

```java
@Data
public class Demo {
  @ExcelProperty(index = 0)
  private String username;

  @ExcelProperty(index = 1)
  private String password;
}
```

- 测试表格

![img](https://minio.pigx.vip/oss/1618560470.png)

## 三、导出Excel

只需要在 `Controller` 层返回 List 并增加 `@ResponseExcel`注解即可

```java
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ResponseExcel {
  String name() default "";
  ExcelTypeEnum suffix() default ExcelTypeEnum.XLSX;
  String password() default "";
  Sheet[] sheets() default @Sheet(sheetName = "sheet1");
  boolean inMemory() default false;
  String template() default "";
  String[] include() default {};
  String[] exclude() default {};
  Class<? extends WriteHandler>[] writeHandler() default {};
  Class<? extends Converter>[] converter() default {};
  Class<? extends HeadGenerator> headGenerator() default HeadGenerator.class;
}
```

### 基础用法

- 返回单 `sheet`, 全部字段导出

```java
@ResponseExcel(name = "test", sheets = @Sheet(sheetName = "testSheet1"))
@GetMapping("/e1")
public List<DemoData> e1() {
    List<DemoData> dataList = new ArrayList<>();
    for (int i = 0; i < 100; i++) {
        DemoData data = new DemoData();
        data.setUsername("tr1" + i);
        data.setPassword("tr2" + i);
        dataList.add(data);
    }
    return dataList;
}

// 实体对象
@Data
public class DemoData {
	private String username;
	private String password;
}
```

![img](http://pigx.vip/20200331162637_DVMcXW_Screenshot.jpeg)

- 自定义字段属性

```java
@Data
public class DemoData {
    @ColumnWidth(50)  // 定义宽度
	@ExcelProperty("用户名") // 定义列名称
    @ContentStyle(fillPatternType = FillPatternType.SOLID_FOREGROUND, fillForegroundColor = 40)
	private String username;
	@ExcelProperty("密码")
	private String password;
}
```

![img](http://pigx.vip/20200331163948_E91zjM_Screenshot.jpeg)

- 忽略部分字段

```java
@Data
public class DemoData {
    @ColumnWidth(50)  // 定义宽度
	@ExcelProperty("用户名") // 定义列名称
    @ContentStyle(fillPatternType = FillPatternType.SOLID_FOREGROUND, fillForegroundColor = 40)
	private String username;
	@ExcelIgnore // 忽略这个字段
	private String password;
}
```

![img](http://pigx.vip/20200331164144_l2gwfD_Screenshot.jpeg)

### 导出并加密

```java
@ResponseExcel(name = "lengleng", password = "lengleng")
@GetMapping("/e1")
public List<DemoData> e1() {
    return list();
}
```

![img](http://pigx.vip/20200331164945_6fsEsG_Screenshot.jpeg)

### 导出多sheet

```java
@ResponseExcel(name = "lengleng", sheets = {
    @Sheet(sheetName = "第一个Sheet"), 
    @Sheet(sheetName = "第二个sheet")
})
@GetMapping("/e1")
public List<List<DemoData>> e1() {
    List<List<DemoData>> lists = new ArrayList<>();
    lists.add(list());
    lists.add(list());
    return lists;
}
```

![img](http://pigx.vip/20200331164527_sbYDsC_Screenshot.jpeg)

### 导出不同的 Sheet

这里两个 sheet 导出不同类型的对象，只导出 DemoData 中的 username 属性，且将 testData 中的 number 属性排除。

```java
@Controller
@RequestMapping("public/excel")
public class ExportMultiSheetController {

	@ResponseExcel(name = "不同Sheet的导出", sheets = {
			@Sheet(sheetName = "demoData", includes = {"username"}),
			@Sheet(sheetName = "testData", excludes = {"number"})
	})
	@GetMapping("/different-sheet")
	public List<List> multiDifferent() {
		List<List> lists = new ArrayList<>();
		lists.add(demoDatalist());
		lists.add(testDatalist());
		return lists;
	}

	private List<DemoData> demoDatalist(){
		List<DemoData> dataList = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			DemoData data = new DemoData();
			data.setUsername("tr1" + i);
			data.setPassword("tr2" + i);
			dataList.add(data);
		}
		return dataList;
	}

	private List<TestData> testDatalist(){
		List<TestData> dataList = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			TestData data = new TestData();
			data.setStr("str" + i);
			data.setNumber(i);
			data.setLocalDateTime(LocalDateTime.now());
			dataList.add(data);
		}
		return dataList;
	}

	// 实体对象
	@Data
	public static class DemoData {
		private String username;
		private String password;
	}

	@Data
	public static class TestData {
		private String str;
		private Integer number;
		@ColumnWidth(50)  // 定义宽度
		private LocalDateTime localDateTime;
	}

}
```



![导出不同的 Sheet](https://hccake-img.oss-cn-shanghai.aliyuncs.com/ballcat/doc/excel-different.png)

### 导出并自定义头信息

**测试实体类：**

```java
@Data
public class SimpleData {
    @ExcelProperty("字符串标题")
    private String string;
    @ExcelProperty("日期标题")
    private Date date;
    @ExcelProperty("数字标题")
    private Integer number;
    // 忽略
    @ExcelIgnore
    private String ignore;
}
```

**自定义头信息生成器**：

> 注意需要实现 `HeadGenerator` 接口，且注册为一个 spring bean.

```java
@Component
public class SimpleDataHeadGenerator implements HeadGenerator {
    @Override
    public HeadMeta head(Class<?> clazz) {
        HeadMeta headMeta = new HeadMeta();
        headMeta.setHead(simpleDataHead());
        // 排除 number 属性
        headMeta.setIgnoreHeadFields(new HashSet<>(Collections.singletonList("number")));
        return headMeta;
    }

    private List<List<String>> simpleDataHead() {
        List<List<String>> list = new ArrayList<>();
        List<String> head0 = new ArrayList<>();
        head0.add("自定义字符串标题" + System.currentTimeMillis());
        List<String> head1 = new ArrayList<>();
        head1.add("自定义日期标题" + System.currentTimeMillis());
        list.add(head0);
        list.add(head1);
        return list;
    }
}
```

该头生成器，将固定返回 自定义字符串标题 和 自定义日期标题 两列头信息，实际使用时可根据业务动态处理，方便在一些权限控制时动态修改或者增删列头。

**测试代码：**

```java
@RequestMapping("/head")
@RestController
public class ExcelHeadTestController {

    @ResponseExcel(name = "customHead", headGenerator = SimpleDataHeadGenerator.class)
    @GetMapping
    public List<SimpleData> multi() {
        List<SimpleData> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            SimpleData simpleData = new SimpleData();
            simpleData.setString("str" + i);
            simpleData.setNumber(i);
            simpleData.setDate(new Date());
            list.add(simpleData);
        }
        return list;
    }
}
```

![自定义头信息](https://hccake-img.oss-cn-shanghai.aliyuncs.com/ballcat/doc/excel-customHeader.png)