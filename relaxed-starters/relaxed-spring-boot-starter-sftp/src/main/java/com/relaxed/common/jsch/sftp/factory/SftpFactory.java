package com.relaxed.common.jsch.sftp.factory;

import com.jcraft.jsch.*;
import com.relaxed.common.jsch.sftp.exception.SftpClientException;
import com.relaxed.common.jsch.sftp.SftpProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.BasePooledObjectFactory;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * sftp factory
 *
 * @author Yakir
 */
@Slf4j
@RequiredArgsConstructor
public class SftpFactory extends BasePooledObjectFactory<AbstractSftp> {

	private static final String CHANNEL_TYPE = "sftp";

	private final SftpProperties sftpProperties;

	private final ISftpProvider iSftpProvider;

	public static SftpFactory of(SftpProperties sftpProperties, ISftpProvider iSftpProvider) {
		return new SftpFactory(sftpProperties, iSftpProvider);
	}

	/**
	 * 创建一个{@link AbstractSftp}子实例 这个方法必须支持并发多线程调用
	 * @return {@link AbstractSftp}子实例
	 */
	@Override
	public AbstractSftp create() throws Exception {
		try {
			JSch jSch = new JSch();
			Session session = jSch.getSession(sftpProperties.getUsername(), sftpProperties.getHost(),
					sftpProperties.getPort());
			session.setPassword(sftpProperties.getPassword());
			session.setConfig("StrictHostKeyChecking", sftpProperties.getStrictHostKeyChecking());
			session.connect(sftpProperties.getSessionConnectTimeout());
			ChannelSftp channel = (ChannelSftp) session.openChannel(CHANNEL_TYPE);
			channel.connect(sftpProperties.getChannelConnectedTimeout());
			return iSftpProvider.provide(channel);
		}
		catch (JSchException e) {
			throw new SftpClientException("connection sftp failed", e);
		}
	}

	/**
	 * 用{@link PooledObject}的实例包装对象
	 * @param abstractSftp 被包装的对象
	 * @return 对象包装器
	 */
	@Override
	public PooledObject<AbstractSftp> wrap(AbstractSftp abstractSftp) {
		return new DefaultPooledObject<>(abstractSftp);
	}

	/**
	 * 销毁对象
	 * @param p 对象包装器
	 */
	@Override
	public void destroyObject(PooledObject<AbstractSftp> p) {
		if (p != null) {
			AbstractSftp sftp = p.getObject();
			if (sftp != null) {
				ChannelSftp channelSftp = sftp.getChannelSftp();
				if (channelSftp != null) {
					channelSftp.disconnect();
					try {
						Session session = channelSftp.getSession();
						if (session != null) {
							session.disconnect();
						}
					}
					catch (JSchException e) {
						log.error("close session error", e);
					}
				}
			}
		}
	}

	/**
	 * 检查连接是否可用
	 * @param p 对象包装器
	 * @return {@code true} 可用，{@code false} 不可用
	 */
	@Override
	public boolean validateObject(PooledObject<AbstractSftp> p) {
		if (p != null) {
			AbstractSftp sftp = p.getObject();
			if (sftp != null) {
				try {
					sftp.getChannelSftp().cd("./");
					return true;
				}
				catch (SftpException e) {
					log.error("validate obj error", e);
					return false;
				}
			}
		}
		return false;
	}

}
