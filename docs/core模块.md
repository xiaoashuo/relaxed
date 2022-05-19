# Core核心模块

## 引入依赖

```xml
   <dependency>
            <groupId>com.lovecyy</groupId>
            <artifactId>relaxed-common-core</artifactId>
            <version>${version}</version>
   </dependency>
```



## 一、批次处理

### 1、定义BatchOps 

```java
public class BatchOps extends AbstractBatchOps {

	private ThreadPoolTaskExecutor executor = SpringUtils
			.getBean(TaskExecutionAutoConfiguration.APPLICATION_TASK_EXECUTOR_BEAN_NAME);

	@Override
	protected Executor executor() {
		return executor;
	}

	// /**
	// * 核心线程池大小
	// */
	// private static int coreSize = Runtime.getRuntime().availableProcessors();
	//
	// /**
	// * 最大线程池大小
	// */
	// private static int maxSize = coreSize * 2 + 1;
	//
	// private static ThreadPoolExecutor executor = ThreadUtil.newExecutor(coreSize,
	// maxSize);
	//
	// /**
	// * 执行器
	// * @return
	// */
	// @Override
	// protected ThreadPoolExecutor executor() {
	// return executor;
	// }

}
```

### 2、单元测试

```java
//1.定义批次分组
BatchGroup batchGroup = new BatchGroup(totalCount, SINGLE_BATCH_LIMIT);
//2.定义数据提供者
BatchSupplier batchSupplier = (startIndex, batchSize) -> dataService.list(startIndex, batchSize);
//3.定义数据消费者
BatchConsumer batchConsumer = batchConsumerParam -> {
			List<Data> datas = batchConsumerParam.getData();
			if (CollectionUtil.isEmpty(datas)) {
				return;
			}
};
//执行方法
BatchOps batchOps = new BatchOps();	
BatchParam batchParam = BatchParam.ofRun(batchGroup, batchSupplier, batchConsumer);
batchParam.setTaskName("执行任务");
batchParam.setAsync(true);
batchOps.runBatch(batchParam);
```



## 二、责任链处理

### 1、定义ContextModel

```java
@Data
public class ProcessContextModel implements ProcessModel {

	private Integer stage;

	private String data;

}
```

### 2、定义处理流程

**上传**

```java
@Slf4j
public class UploadProcess implements BusinessProcess {

	@Override
	public boolean support(ProcessContext context) {
		ProcessContextModel processContextModel = (ProcessContextModel) context.getProcessModel();
		Integer stage = processContextModel.getStage();
		return stage.equals(1);
	}

	@Override
	public void process(ProcessContext context) {
		log.info("上传处理器开始{}", context);
		ProcessContextModel processContextModel = (ProcessContextModel) context.getProcessModel();
		if (processContextModel.getData() == null) {
			context.setNeedBreak(true).setResponse(R.failed(400, "客户端参数错误"));
			return;
		}
		processContextModel.setStage(2);
		log.info("上传处理器结束{}", context);
	}

}
```

**签名**

```java
@Slf4j
public class SignProcess implements BusinessProcess {

	@Override
	public boolean support(ProcessContext context) {
		ProcessContextModel processContextModel = (ProcessContextModel) context.getProcessModel();
		Integer stage = processContextModel.getStage();
		return stage.equals(2);
	}

	@Override
	public void process(ProcessContext context) {
		log.info("签署处理器开始{}", context);
		ProcessContextModel processContextModel = (ProcessContextModel) context.getProcessModel();
		processContextModel.setStage(3);
		processContextModel.setData("签署");
		log.info("签署处理器结束{}", context);
	}

}
```

**下载**

```java
@Slf4j
public class DownProcess implements BusinessProcess {

	@Override
	public boolean support(ProcessContext context) {
		ProcessContextModel processContextModel = (ProcessContextModel) context.getProcessModel();
		Integer stage = processContextModel.getStage();
		return stage.equals(3);
	}

	@Override
	public void process(ProcessContext context) {
		log.info("下载处理器开始{}", context);
		ProcessContextModel processContextModel = (ProcessContextModel) context.getProcessModel();
		processContextModel.setStage(4);
		processContextModel.setData("下载");
		log.info("下载处理器结束{}", context);
	}

}
```

### 3、注册

```java
	static ProcessExecutor processExecutor;
	static {
		ProcessTemplate processTemplate = getStampProcessTemplate();
		Map<String, ProcessTemplate> templateConfig = new HashMap<>(4);

		templateConfig.put("ts", processTemplate);
		processExecutor = new ProcessExecutor(templateConfig);
	}


	/**
	 * 顺序处理 1.上传处理器 2.签署处理器 3.下载处理器
	 * @return
	 */
	private static ProcessTemplate getStampProcessTemplate() {
		ProcessTemplate processTemplate = new ProcessTemplate();

		ArrayList<BusinessProcess> processList = new ArrayList<>();
		processList.add(new UploadProcess());
		processList.add(new SignProcess());
		processList.add(new DownProcess());
		processTemplate.setProcessList(processList);
		return processTemplate;
	}
```

### 4、执行

```java
	public static void main(String[] args) {
		ProcessContextModel processContextModel = new ProcessContextModel();
		processContextModel.setStage(1);
		// processContextModel.setData("签署");

		ProcessContext processContext = ProcessContext.builder().processId(IdUtil.simpleUUID())
				.processModel(processContextModel).channel("ts").needBreak(false).response(R.ok("流程开始")).build();

		processExecutor.process(processContext);

	}
```

