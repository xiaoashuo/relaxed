package com.relaxed.common.jsch.sftp.factory;

import com.relaxed.common.jsch.sftp.executor.ISftpExecutor;
import org.apache.commons.pool2.impl.GenericObjectPool;

/**
 * sftp 对象池
 * <p>
 * 要使用 ACP 实现一个对象池，大致可以分为三个步骤： 1. 创建对象工厂：告诉 ACP 如何创建你要的对象 2. 创建对象池：告诉 ACP 你想创建一个怎样的对象池 3.
 * 使用对象池：ACP 告诉你如何使用你的对象
 * </p>
 *
 * @author shuoyu
 */
public class SftpPool extends GenericObjectPool<ISftpExecutor> {

	/**
	 * 创建一个{@link GenericObjectPool}对象池，跟踪使用后未返回给对象池的对象，防止对象泄漏
	 * @param factory 对象工厂
	 * @param config 对象池配置
	 * @param abandonedConfig 废弃对象跟踪配置
	 */
	public SftpPool(SftpFactory factory, SftpPoolConfig config, SftpAbandonedConfig abandonedConfig) {
		super(factory, config, abandonedConfig);
	}

}
