package com.relaxed.common.jsch.sftp.executor;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import com.relaxed.common.jsch.sftp.SftpProperties;
import com.relaxed.common.jsch.sftp.exception.SftpClientException;
import com.relaxed.common.jsch.sftp.utils.ByteUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * SFTP执行器默认实现
 *
 * @author shuoyu
 */
@Slf4j
public class SftpExecutor extends AbstractSftpExecutor {

	private static final String NO_SUCH_FILE = "No such file";

	/**
	 * 创建SFTP执行器实例
	 * @param channelSftp SFTP通道
	 * @param sftpProperties SFTP配置属性
	 */
	public SftpExecutor(ChannelSftp channelSftp, SftpProperties sftpProperties) {
		super(channelSftp, sftpProperties);
		String workdir = sftpProperties.getWorkdir();
		if (!StringUtils.hasText(workdir)) {
			try {
				String workDir = channelSftp.pwd();
				sftpProperties.setWorkdir(workDir);
			}
			catch (SftpException e) {
				log.error("获取默认工作目录失败", e);
			}
		}

	}

	/**
	 * 检查文件或目录是否存在
	 * @param path 文件或目录路径
	 * @return 如果存在返回true，否则返回false
	 * @throws SftpClientException 检查过程中可能抛出的异常
	 */
	@Override
	public boolean isExist(String path) {
		try {
			getChannelSftp().lstat(path);
			return true;
		}
		catch (SftpException e) {
			if (NO_SUCH_FILE.equalsIgnoreCase(e.getMessage())) {
				log.debug("isExist exception,路径:{}", path, e);
				return false;
			}
			throw new SftpClientException(String.format("isExist exception params[%s]", path), e);
		}
	}

	/**
	 * 获取远程文件的输入流
	 * @param absoluteFilePath 绝对文件路径
	 * @return 远程文件输入流
	 * @throws SftpClientException 获取输入流时可能抛出的异常
	 */
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

	/**
	 * 获取远程文件的输入流
	 * @param dir 文件目录
	 * @param name 文件名
	 * @return 远程文件输入流
	 * @throws SftpClientException 获取输入流时可能抛出的异常
	 */
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
		finally {
			this.switchToWorkDir();
		}
	}

	/**
	 * 下载远程文件到指定文件
	 * @param dir 远程目录
	 * @param name 远程文件名
	 * @param file 本地文件
	 * @return 下载后的本地文件
	 * @throws SftpClientException 下载过程中可能抛出的异常
	 */
	@Override
	public File download(String dir, String name, File file) {
		if (!isExist(dir)) {
			throw new SftpClientException(String.format("the directory (%s) does not exist.", dir));
		}
		String absoluteFilePath = dir + "/" + name;
		return download(absoluteFilePath, file);

	}

	/**
	 * 下载远程文件到指定文件
	 * @param absoluteFilePath 远程文件绝对路径
	 * @param file 本地文件
	 * @return 下载后的本地文件
	 * @throws SftpClientException 下载过程中可能抛出的异常
	 */
	@Override
	public File download(String absoluteFilePath, File file) {
		try (FileOutputStream fos = new FileOutputStream(file)) {
			if (!isExist(absoluteFilePath)) {
				throw new SftpClientException(String.format("the file (%s) does not exist.", absoluteFilePath));
			}
			channelSftp.get(absoluteFilePath, fos);
			return file;
		}
		catch (Exception e) {
			throw new SftpClientException(
					String.format("remote file download exception param[fullPath=%s]", absoluteFilePath), e);
		}
	}

	/**
	 * 下载远程文件到字节数组
	 * @param dir 远程目录
	 * @param name 远程文件名
	 * @return 文件内容字节数组
	 * @throws SftpClientException 下载过程中可能抛出的异常
	 */
	@Override
	public byte[] download(String dir, String name) {
		InputStream in = getInputStream(dir, name);
		return ByteUtil.inputStreamToByteArray(in);
	}

	/**
	 * 下载远程文件到字节数组
	 * @param absoluteFilePath 远程文件绝对路径
	 * @return 文件内容字节数组
	 * @throws SftpClientException 下载过程中可能抛出的异常
	 */
	@Override
	public byte[] download(String absoluteFilePath) {
		InputStream in = getInputStream(absoluteFilePath);
		return ByteUtil.inputStreamToByteArray(in);
	}

	/**
	 * 上传文件到远程服务器
	 * @param dir 远程目录
	 * @param name 远程文件名
	 * @param in 文件输入流
	 * @throws SftpClientException 上传过程中可能抛出的异常
	 */
	@Override
	public void upload(String dir, String name, InputStream in) {
		try {
			mkdirs(dir);
			channelSftp.cd(dir);
			channelSftp.put(in, name);
		}
		catch (SftpException e) {
			throw new SftpClientException(String.format("upload file exception params[dir=%s,name=%s]", dir, name), e);
		}
		finally {
			this.switchToWorkDir();
		}
	}

	/**
	 * 上传文件到远程服务器
	 * @param dir 远程目录
	 * @param name 远程文件名
	 * @param file 本地文件
	 * @throws SftpClientException 上传过程中可能抛出的异常
	 */
	@Override
	public void upload(String dir, String name, File file) {
		try (FileInputStream fis = new FileInputStream(file)) {
			upload(dir, name, fis);
		}
		catch (SftpClientException | IOException e) {
			throw new SftpClientException(String.format("upload file exception params[dir=%s,name=%s]", dir, name), e);
		}
	}

	/**
	 * 上传文件到远程服务器
	 * @param dir 远程目录
	 * @param name 远程文件名
	 * @param src 本地文件路径
	 * @throws SftpClientException 上传过程中可能抛出的异常
	 */
	@Override
	public void upload(String dir, String name, String src) {
		try {
			mkdirs(dir);
			channelSftp.cd(dir);
			channelSftp.put(src, name);
		}
		catch (SftpException e) {
			throw new SftpClientException(String.format("upload file exception params[dir=%s,name=%s]", dir, name), e);
		}
		finally {
			this.switchToWorkDir();
		}
	}

	/**
	 * 删除远程文件或目录
	 * @param path 远程文件或目录路径
	 * @throws SftpClientException 删除过程中可能抛出的异常
	 */
	@Override
	public void delete(String path) {
		if (!isExist(path)) {
			return;
		}
		try {
			if (isDir(path)) {
				channelSftp.rmdir(path);
			}
			else {
				channelSftp.rm(path);
			}

		}
		catch (SftpException e) {
			throw new SftpClientException(String.format("delete path exception params[path=%s]", path), e);
		}
	}

	/**
	 * 删除远程文件
	 * @param dir 远程目录
	 * @param name 远程文件名
	 * @throws SftpClientException 删除过程中可能抛出的异常
	 */
	@Override
	public void delete(String dir, String name) {
		if (!isDir(dir)) {
			return;
		}
		String absoluteFilePath = dir + "/" + name;
		if (!isExist(absoluteFilePath)) {
			return;
		}
		try {
			channelSftp.cd(dir);
			channelSftp.rm(name);
		}
		catch (SftpException e) {
			throw new SftpClientException(String.format("delete file exception params[dir=%s,name=%s]", dir, name), e);
		}
		finally {
			this.switchToWorkDir();
		}
	}

	@Override
	public void mkdirs(String dir) {
		String[] folders = dir.split("/");
		try {
			channelSftp.cd("/");
			for (String folder : folders) {
				if (folder.length() > 0) {
					try {
						channelSftp.cd(folder);
					}
					catch (Exception e) {
						channelSftp.mkdir(folder);
						channelSftp.cd(folder);
					}
				}
			}
		}
		catch (SftpException e) {
			throw new SftpClientException(String.format("create directory exception params[dir=%s]", dir), e);
		}
		finally {
			this.switchToWorkDir();
		}
	}

	@Override
	public boolean isDir(String path) {
		try {
			SftpATTRS attrs = channelSftp.lstat(path);
			return attrs.isDir();
		}
		catch (SftpException e) {
			return false;
		}
	}

	@Override
	public List<String> list(String path) {
		return this.list(path, (fileAttr) -> true);
	}

	@Override
	public List<String> list(String path, Function<FileAttr, Boolean> filterFunction) {
		List<String> result = new ArrayList<>();
		ChannelSftp.LsEntrySelector selector = lsEntry -> {
			String filename = lsEntry.getFilename();
			if (!".".equals(filename) && !"..".equals(filename)) {
				SftpATTRS attrs = lsEntry.getAttrs();
				boolean isDir = attrs.isDir();
				// "atime"和"mtime"分别包含文件的访问和修改时间,它们表示为UTC 1970年1月1日起的秒数。
				long modifyTime = attrs.getMTime() * 1000L;
				FileAttr fileAttr = new FileAttr(isDir, filename, modifyTime);
				Boolean isNeedStorage = filterFunction.apply(fileAttr);
				if (isNeedStorage) {
					result.add(filename);
				}
			}

			return ChannelSftp.LsEntrySelector.CONTINUE;
		};
		try {
			channelSftp.ls(path, selector);
		}
		catch (SftpException e) {
			throw new SftpClientException(String.format("view directory exception params[path=%s]", path), e);
		}
		return result;
	}

	@Override
	public void move(String src, String target) {
		try {
			channelSftp.rename(src, target);
		}
		catch (SftpException e) {
			throw new SftpClientException(
					String.format("sftp move file exception params[src=%s,target=%s]", src, target), e);
		}
	}

	@Override
	public void move(String src, String target, String fileName) {
		try {
			mkdirs(target);
			channelSftp.cd(target);
			channelSftp.rename(src, fileName);
		}
		catch (SftpException e) {
			throw new SftpClientException(
					String.format(" move file exception params[src=%s,target=%s,fileName=%s]", src, target, fileName),
					e);
		}
		finally {
			this.switchToWorkDir();
		}
	}

	@Override
	public void chmod(String permissions, String path) {
		if (permissions == null) {
			throw new SftpClientException("permissions can not be empty.");
		}
		if (permissions.length() != 3) {
			throw new SftpClientException("the permission must be 3 digits 0-7");
		}
		for (char c : permissions.toCharArray()) {
			int i;
			try {
				i = Integer.parseInt(String.valueOf(c));
			}
			catch (NumberFormatException e) {
				throw new SftpClientException("the permission must be 3 digits 0-7");
			}
			if (i > 7 || i < 0) {
				throw new SftpClientException("the permission must be 3 digits 0-7");
			}
			Integer p = Integer.valueOf(permissions, 8);
			try {
				channelSftp.chmod(p, path);
			}
			catch (SftpException e) {
				throw new SftpClientException(
						String.format("modify permissions exception params[perm=%s,path=%s]", permissions, path), e);
			}
		}

	}

	@Override
	public long filesize(String path) {
		if (path == null) {
			throw new SftpClientException("file path can not be empty.");
		}
		boolean isDir = isDir(path);
		if (isDir) {
			throw new SftpClientException("dir size not support.");
		}
		try {
			SftpATTRS lstat = getChannelSftp().lstat(path);
			return lstat.getSize();
		}
		catch (SftpException e) {
			throw new SftpClientException("get file size exception", e);
		}

	}

	/**
	 * 切换回默认工作目录
	 */
	private void switchToWorkDir() {
		SftpProperties sftpProperties = super.getSftpProperties();
		String workdir = sftpProperties.getWorkdir();
		if (StringUtils.hasText(workdir)) {
			try {
				super.getChannelSftp().cd(workdir);
			}
			catch (SftpException e) {
				log.error("切回工作目录失败", e);
			}
		}
	}

}
