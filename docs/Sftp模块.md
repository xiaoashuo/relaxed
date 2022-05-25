# Sftp使用

## 1.引入坐标

```xml
        <dependency>
            <groupId>com.lovecyy</groupId>
            <artifactId>relaxed-spring-boot-starter-sftp</artifactId>
            <version>${version}</version>
        </dependency>
```

## 2.application.yml

```yml
relaxed:
   sftp:
    host: 127.0.0.1
    port: 2222
    username: root
    password: 123456
    sessionConnectTimeout: 15000
    channelConnectedTimeout: 15000
    ###以下为对象池配置
    pool:
      max-total: 20
      max-idle: 10
      min-idle: 5
      lifo: true
      fairness: false
      max-wait-millis: 5000
      min-evictable-idle-time-millis: -1
      evictor-shutdown-timeout-millis: 10000
      soft-min-evictable-idle-time-millis: 1800000
      num-tests-per-eviction-run: 3
      test-on-create: false
      test-on-borrow: true
      test-on-return: false
      test-while-idle: true
      time-between-eviction-runs-millis: 600000
      block-when-exhausted: true
      jmx-enabled: false
      jmx-name-prefix: pool
      jmx-name-base: sftp
    abandoned:
      remove-abandoned-on-borrow: true
      remove-abandoned-on-maintenance: true
      remove-abandoned-timeout: 300
      log-abandoned: false
      require-full-stack-trace: false
      use-usage-tracking: false

```

## 3.测试操作

```java
@Autowired
private  ISftpClient iSftpClient;
//不带返回值
iSftpClient.open(sftp -> {
            //此处可以进行sftp 方式调用
	        List<String> list = sftp.list(path);
			log.info("查询到文件列表{}", list);
});
//带返回值
List<String> list = iSftpClient.supplyOpen(sftp -> sftp.list(path));


```

## 附：自定义sftp client方法

### 1.自定义客户端函数

定义`CustomSftp.java` 继成 自`SftpExecutor.java`

```java
public SftpExtensionExecutor(ChannelSftp channelSftp) {
		super(channelSftp);
	}

	@Override
	public File download(String absoluteFilePath, File file) {
		downloadPathCheck(file);
		return super.download(absoluteFilePath, file);
	}

	@Override
	public File download(String dir, String name, File file) {
		downloadPathCheck(file);
		return super.download(dir, name, file);
	}

	/**
	 * 下载文件目标路径检查不存在 父级文件夹则创建
	 * @author yakir
	 * @date 2022/5/25 10:30
	 * @param file
	 */
	public static void downloadPathCheck(File file) {
		File dirFile = file.getParentFile();
		mkdir(dirFile);
	}

	private static File mkdir(File dir) {
		if (dir == null) {
			return null;
		}
		if (false == dir.exists()) {
			mkdirsSafely(dir, 5, 1);
		}
		return dir;
	}

	/**
	 * 安全地级联创建目录 (确保并发环境下能创建成功)
	 *
	 * <pre>
	 *     并发环境下，假设 test 目录不存在，如果线程A mkdirs "test/A" 目录，线程B mkdirs "test/B"目录，
	 *     其中一个线程可能会失败，进而导致以下代码抛出 FileNotFoundException 异常
	 *
	 *     file.getParentFile().mkdirs(); // 父目录正在被另一个线程创建中，返回 false
	 *     file.createNewFile(); // 抛出 IO 异常，因为该线程无法感知到父目录已被创建
	 * </pre>
	 * @param dir 待创建的目录
	 * @param tryCount 最大尝试次数
	 * @param sleepMillis 线程等待的毫秒数
	 * @return true表示创建成功，false表示创建失败
	 * @author z8g
	 * @since 5.7.21
	 */
	private static boolean mkdirsSafely(File dir, int tryCount, long sleepMillis) {
		if (dir == null) {
			return false;
		}
		if (dir.isDirectory()) {
			return true;
		}
		for (int i = 1; i <= tryCount; i++) { // 高并发场景下，可以看到 i 处于 1 ~ 3 之间
			// 如果文件已存在，也会返回 false，所以该值不能作为是否能创建的依据，因此不对其进行处理
			// noinspection ResultOfMethodCallIgnored
			dir.mkdirs();
			if (dir.exists()) {
				return true;
			}

			sleep(sleepMillis);
		}
		return dir.exists();
	}

	/**
	 * 挂起当前线程
	 * @param millis 挂起的毫秒数
	 * @return 被中断返回false，否则true
	 * @since 5.3.2
	 */
	public static boolean sleep(long millis) {
		if (millis > 0) {
			try {
				Thread.sleep(millis);
			}
			catch (InterruptedException e) {
				return false;
			}
		}
		return true;
	}

```

### 2.注册到容器

```java
	/**
	 * sftp provider 主要负责提供产出{@See AbstractSql}子实列的动作
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean
	public ISftpProvider iSftpProvider() {
		return channelSftp -> new SftpExecutor(channelSftp);
	}
```

