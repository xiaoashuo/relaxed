package com.relaxed.common.jsch.sftp.factory;

import org.apache.commons.pool2.impl.GenericObjectPool;

/**
 * sftp 对像池 https://commons.apache.org/proper/commons-pool/ 要使用 ACP 实现一个对象池，大致可以分为三个步骤：
 *
 * 创建对象工厂：告诉 ACP 如何创建你要的对象。 创建对象池：告诉 ACP 你想创建一个怎样的对象池。 使用对象池：ACP 告诉你如何使用你的对象。
 *
 * @author shuoyu
 */
public class SftpPool extends GenericObjectPool<AbstractSftp> {

	/**
	 * Creates a new <code>GenericObjectPool</code> that tracks and destroys objects that
	 * are checked out, but never returned to the pool.
	 * 创建一个{@link GenericObjectPool}对象池，跟踪使用后未返回给对象池的对象，防止对象泄漏。
	 * @param factory The object factory to be used to create object instances used by
	 * this pool 对象工厂
	 * @param config The base pool configuration to use for this pool instance. The
	 * configuration is used by value. Subsequent changes to the configuration object will
	 * not be reflected in the pool. 对象池配置
	 * @param abandonedConfig Configuration for abandoned object identification 废弃对象跟踪配置
	 */
	public SftpPool(SftpFactory factory, SftpPoolConfig config, SftpAbandonedConfig abandonedConfig) {
		super(factory, config, abandonedConfig);
	}

}
