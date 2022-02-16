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

定义`CustomSftp.java` 继成 自`AbstractSftp.java`

```java
public DefaultSftp(ChannelSftp channelSftp) {
		super(channelSftp);
	}

	@Override
	public boolean isExist(String path) {
		try {
			getChannelSftp().lstat(path);
			return true;
		}
		catch (SftpException e) {
			log.error("isExist exception params[{}]", path, e);
			return false;
		}
	}

	@Override
	public InputStream getInputStream(String absoluteFilePath) {
		if (!isExist(absoluteFilePath)) {
			throw new SftpClientException(String.format("the file (%s) does not exist.", absoluteFilePath));
		}
		try {
			return channelSftp.get(absoluteFilePath);
		}
		catch (SftpException e) {
			throw new SftpClientException(String.format("get remote file exception params[path=%s]", absoluteFilePath),
					e);
		}
	}

	@Override
	public InputStream getInputStream(String dir, String name) {
		if (!isExist(dir)) {
			throw new SftpClientException(String.format("the directory (%s) does not exist.", dir));
		}
		String absoluteFilePath = dir + "/" + name;
		if (!isExist(absoluteFilePath)) {
			throw new SftpClientException(String.format("the file (%s) does not exist.", absoluteFilePath));
		}
		try {
			channelSftp.cd(dir);
			return channelSftp.get(name);
		}
		catch (SftpException e) {
			throw new SftpClientException(String.format("get remote file exception params[dir=%s,name=%s]", dir, name),
					e);
		}
	}
```

### 2.注册到容器

```java
	/**
	 * sftp provider 主要负责提供产出{@See AbstractSql}子实列的动作
	 * @return
	 */
	@Bean
	public ISftpProvider iSftpProvider() {
		return channelSftp -> new DefaultSftp(channelSftp);
	}
```

