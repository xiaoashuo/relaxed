# Cache模块

## 一、引入依赖

```xml

  <dependency>
            <groupId>com.lovecyy</groupId>
            <artifactId>relaxed-spring-boot-starter-cache</artifactId>
             <version>${version}</version>
  </dependency>
  
```

## 二、配置文件

```yml
relaxed:
   cache:
     #全局key前缀
     key-prefix: 'pc:'
     #锁key后缀
     lock-key-suffix: locked
     #key分隔符
     delimiter: ':'
     #空值占位
     null-value: "N_V"
     #默认全局过期时间 ms
     expire-time: 86400
     #锁超时时间
     locked-time-out: 1000
```

## 三、编程式调用

```java
//1.引入依赖
private final CacheManage<String> cacheManage;
//2.设置缓存
cacheManage.set(key,param,timeout);
//3.查询缓存
String value = cacheManage.get(key);
//4.删除缓存
cacheManage.remove(key);
```

## 四、声明式调用

内置三大注解

`@Cached`  分布式锁缓存，若查出数据为空，则默认方式空值占位

+ prefix  缓存前缀 
+ keyJoint key动态参数，支持SPEL ，若需添加后缀，可在表达式后添加文本。eg: `#param+'suffix'`
+ condition  条件表达式  若成立，则注解生效，否则注解不生效
+ ttl 缓存超时时间 ms

`@CachedPut` 缓存创建或更新

+ prefix  缓存前缀 
+ keyJoint key动态参数，支持SPEL ，若需添加后缀，可在表达式后添加文本。eg: `#param+'suffix'`
+ condition  条件表达式  若成立，则注解生效，否则注解不生效
+ ttl 缓存超时时间 ms

`@CachedDel` 缓存删除

+ prefix  缓存前缀 
+ keyJoint key动态参数，支持SPEL ，若需添加后缀，可在表达式后添加文本。eg: `#param+'suffix'`
+ condition  条件表达式  若成立，则注解生效，否则注解不生效

## 附件功能

### 一、分布式锁

#### 1.LockManage

> 分布式锁管理 工具 ，由Spring托管，直接注入。

```java
	/**
	 * 上锁
	 * @param key
	 * @param requestId
	 * @return
	 */
	boolean lock(String key, T requestId);

	/**
	 * 上锁 指定过期时间
	 * @param key
	 * @param requestId
	 * @param ttl
	 * @return
	 */
	boolean lock(String key, T requestId, Long ttl);

	/**
	 * 上锁
	 * @author yakir
	 * @date 2021/8/26 18:07
	 * @param key
	 * @param requestId
	 * @param ttl 锁过期时间
	 * @param timeout 锁获取超时时间
	 * @return java.lang.Boolean
	 */
	boolean lock(String key, T requestId, Long ttl, long timeout);

	/**
	 * 释放锁
	 * @param key
	 * @param requestId
	 * @return
	 */
	boolean releaseLock(String key, T requestId);
```

#### 2.分布式锁模板调用

```java
String cacheData = DistributedLock.<String>instance().action(ops.lockKey(), () -> {
			String cacheValue = cacheQuery.get();
			if (cacheValue == null) {
				// 从数据库查询数据
				Object dbValue = ops.joinPoint().proceed();
				// 如果数据库中没数据，填充一个String，防止缓存击穿
				cacheValue = dbValue == null ? CachePropertiesHolder.nullValue() : cacheSerializer.serialize(dbValue);
				// 设置缓存
				ops.cachePut().accept(cacheValue);
			}
			return cacheValue;
		}).lockManage(lockManage).onLockFail(cacheQuery).lock();
```

### 二、前缀Key生成器

> 实现KeyGenerator接口，默认前缀拼接生成器(全局生效)

```java
@RequiredArgsConstructor
public class PrefixKeyGenerator implements KeyGenerator {

	private final String prefix;

	@Override
	public String generate(String originalText) {
		if (StrUtil.isEmpty(originalText) || StrUtil.isEmpty(prefix)) {
			return originalText;
		}
		return prefix + originalText;
	}

}
```

### 三、配置文件持有者

`CachePropertiesHolder` 配置文件持有,静态方法直接调用

```java
public static String keyPrefix();

public static String lockKeySuffix();

public static String delimiter();

public static String nullValue();

public static long expireTime();

public static long lockedTimeOut();

```









