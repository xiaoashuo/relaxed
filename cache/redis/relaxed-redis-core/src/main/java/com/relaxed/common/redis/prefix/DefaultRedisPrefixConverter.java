package com.relaxed.common.redis.prefix;

/**
 * Redis key前缀默认转换器实现类。 提供简单的Redis key前缀转换功能，始终使用固定的前缀。
 *
 * @author huyuanzhi
 * @since 1.0
 */
public class DefaultRedisPrefixConverter implements IRedisPrefixConverter {

	/**
	 * Redis key的前缀
	 */
	private final String prefix;

	/**
	 * 构造函数
	 * @param prefix Redis key的前缀
	 */
	public DefaultRedisPrefixConverter(String prefix) {
		this.prefix = prefix;
	}

	@Override
	public String getPrefix() {
		return prefix;
	}

	@Override
	public boolean match() {
		return true;
	}

}
