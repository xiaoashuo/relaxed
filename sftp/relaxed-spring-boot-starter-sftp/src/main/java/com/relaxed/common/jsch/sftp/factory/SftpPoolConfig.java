package com.relaxed.common.jsch.sftp.factory;

import com.relaxed.common.jsch.sftp.SftpProperties;
import com.relaxed.common.jsch.sftp.executor.ISftpExecutor;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.pool2.impl.DefaultEvictionPolicy;
import org.apache.commons.pool2.impl.EvictionPolicy;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * sftp连接池配置
 *
 * @author shuoyu
 */
public class SftpPoolConfig extends GenericObjectPoolConfig<ISftpExecutor> {

	/**
	 * 根据配置获得池配置
	 * @param sftpProperties SFTP连接池配置属性
	 * @return 配置好的SFTP连接池配置对象
	 */
	public static SftpPoolConfig of(SftpProperties sftpProperties) {
		SftpProperties.Pool pool = sftpProperties.getPool();
		return new SftpPoolConfig.Builder().maxTotal(pool.getMaxTotal()).maxIdle(pool.getMaxIdle())
				.minIdle(pool.getMinIdle()).lifo(pool.isLifo()).fairness(pool.isFairness())
				.maxWaitMillis(pool.getMaxWaitMillis()).minEvictableIdleTimeMillis(pool.getMinEvictableIdleTimeMillis())
				.evictorShutdownTimeoutMillis(pool.getEvictorShutdownTimeoutMillis())
				.softMinEvictableIdleTimeMillis(pool.getSoftMinEvictableIdleTimeMillis())
				.numTestsPerEvictionRun(pool.getNumTestsPerEvictionRun()).evictionPolicy(null)
				// 驱逐策略此处使用默认
				.evictionPolicyClassName(DefaultEvictionPolicy.class.getName()).testOnCreate(pool.isTestOnCreate())
				.testOnBorrow(pool.isTestOnBorrow()).testOnReturn(pool.isTestOnReturn())
				.testWhileIdle(pool.isTestWhileIdle())
				.timeBetweenEvictionRunsMillis(pool.getTimeBetweenEvictionRunsMillis())
				.blockWhenExhausted(pool.isBlockWhenExhausted()).jmxEnabled(pool.isJmxEnabled())
				.jmxNamePrefix(pool.getJmxNamePrefix()).jmxNameBase(pool.getJmxNameBase()).build();
	}

	public static class Builder {

		private int maxTotal;

		private int maxIdle;

		private int minIdle;

		private boolean lifo;

		private boolean fairness;

		private long maxWaitMillis;

		private long minEvictableIdleTimeMillis;

		private long evictorShutdownTimeoutMillis;

		private long softMinEvictableIdleTimeMillis;

		private int numTestsPerEvictionRun;

		private EvictionPolicy<ISftpExecutor> evictionPolicy; // 仅2.6.0版本commons-pool2需要设置

		private String evictionPolicyClassName;

		private boolean testOnCreate;

		private boolean testOnBorrow;

		private boolean testOnReturn;

		private boolean testWhileIdle;

		private long timeBetweenEvictionRunsMillis;

		private boolean blockWhenExhausted;

		private boolean jmxEnabled;

		private String jmxNamePrefix;

		private String jmxNameBase;

		public SftpPoolConfig build() {
			SftpPoolConfig config = new SftpPoolConfig();
			config.setMaxTotal(maxTotal);
			config.setMaxIdle(maxIdle);
			config.setMinIdle(minIdle);
			config.setLifo(lifo);
			config.setFairness(fairness);
			config.setMaxWaitMillis(maxWaitMillis);
			config.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
			config.setEvictorShutdownTimeoutMillis(evictorShutdownTimeoutMillis);
			config.setSoftMinEvictableIdleTimeMillis(softMinEvictableIdleTimeMillis);
			config.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
			config.setEvictionPolicy(evictionPolicy);
			config.setEvictionPolicyClassName(evictionPolicyClassName);
			config.setTestOnCreate(testOnCreate);
			config.setTestOnBorrow(testOnBorrow);
			config.setTestOnReturn(testOnReturn);
			config.setTestWhileIdle(testWhileIdle);
			config.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
			config.setBlockWhenExhausted(blockWhenExhausted);
			config.setJmxEnabled(jmxEnabled);
			config.setJmxNamePrefix(jmxNamePrefix);
			config.setJmxNameBase(jmxNameBase);
			return config;
		}

		public Builder maxTotal(int maxTotal) {
			this.maxTotal = maxTotal;
			return this;
		}

		public Builder maxIdle(int maxIdle) {
			this.maxIdle = maxIdle;
			return this;
		}

		public Builder minIdle(int minIdle) {
			this.minIdle = minIdle;
			return this;
		}

		public Builder lifo(boolean lifo) {
			this.lifo = lifo;
			return this;
		}

		public Builder fairness(boolean fairness) {
			this.fairness = fairness;
			return this;
		}

		public Builder maxWaitMillis(long maxWaitMillis) {
			this.maxWaitMillis = maxWaitMillis;
			return this;
		}

		public Builder minEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
			this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
			return this;
		}

		public Builder evictorShutdownTimeoutMillis(long evictorShutdownTimeoutMillis) {
			this.evictorShutdownTimeoutMillis = evictorShutdownTimeoutMillis;
			return this;
		}

		public Builder softMinEvictableIdleTimeMillis(long softMinEvictableIdleTimeMillis) {
			this.softMinEvictableIdleTimeMillis = softMinEvictableIdleTimeMillis;
			return this;
		}

		public Builder numTestsPerEvictionRun(int numTestsPerEvictionRun) {
			this.numTestsPerEvictionRun = numTestsPerEvictionRun;
			return this;
		}

		public Builder evictionPolicy(EvictionPolicy<ISftpExecutor> evictionPolicy) {
			this.evictionPolicy = evictionPolicy;
			return this;
		}

		public Builder evictionPolicyClassName(String evictionPolicyClassName) {
			this.evictionPolicyClassName = evictionPolicyClassName;
			return this;
		}

		public Builder testOnCreate(boolean testOnCreate) {
			this.testOnCreate = testOnCreate;
			return this;
		}

		public Builder testOnBorrow(boolean testOnBorrow) {
			this.testOnBorrow = testOnBorrow;
			return this;
		}

		public Builder testOnReturn(boolean testOnReturn) {
			this.testOnReturn = testOnReturn;
			return this;
		}

		public Builder testWhileIdle(boolean testWhileIdle) {
			this.testWhileIdle = testWhileIdle;
			return this;
		}

		public Builder timeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
			this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
			return this;
		}

		public Builder blockWhenExhausted(boolean blockWhenExhausted) {
			this.blockWhenExhausted = blockWhenExhausted;
			return this;
		}

		public Builder jmxEnabled(boolean jmxEnabled) {
			this.jmxEnabled = jmxEnabled;
			return this;
		}

		public Builder jmxNamePrefix(String jmxNamePrefix) {
			this.jmxNamePrefix = jmxNamePrefix;
			return this;
		}

		public Builder jmxNameBase(String jmxNameBase) {
			this.jmxNameBase = jmxNameBase;
			return this;
		}

	}

}
