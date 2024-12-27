package com.relaxed.test.sftp;

import cn.hutool.core.io.FileUtil;
import com.jcraft.jsch.SftpException;
import com.relaxed.common.jsch.sftp.SftpAutoConfiguration;
import com.relaxed.common.jsch.sftp.client.ISftpClient;
import com.relaxed.common.jsch.sftp.executor.ISftpExecutor;
import com.relaxed.test.utils.file.FileUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.List;

/**
 * @author Yakir
 * @Topic SftpTest
 * @Description
 * @date 2024/12/27 10:29
 * @Version 1.0
 */
@Slf4j
@SpringBootTest(classes = {SftpAutoConfiguration.class},
		properties = "spring.config.location=classpath:/sftp/application-sftp.yml")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SftpTest {
	@Autowired
	private ISftpClient iSftpClient;

	private String DIR_PATH="/home/dir";

	private String FILE_NAME="Poi测试模板.docx";

	//上传
	@Order(1)
	@Test
	public void testUpload(){
		// 模板文件
		File locaFile = getLocalFile();
		//上传文件 当前工作目录会变更
		iSftpClient.open(sftp->{
			sftp.upload(DIR_PATH,FILE_NAME,locaFile);
			log.info("文件上传成功,目录:{},名称:{}",DIR_PATH,FILE_NAME);
		});
	}
	//下载(含存在性判断)
	@Order(2)
	@Test
	public void testDownload(){
		Boolean isExists = iSftpClient.supplyOpen(sftp -> sftp.isExist(DIR_PATH + "/" + FILE_NAME));
		log.info("当前文件是否存在:{}",isExists);
		byte[] bytes = iSftpClient.supplyOpen(sftp -> {
			byte[] download = sftp.download( DIR_PATH, FILE_NAME);
			return download;
		});
		log.info("当前字节大小:{}",bytes.length);
	}
	//查询列表
	@Order(3)
	@Test
	public void testListFiles(){
 		//不带返回值
		iSftpClient.open(sftp -> {
			//此处可以进行sftp 方式调用
			List<String> list = sftp.list(DIR_PATH);
			log.info("查询到文件列表{}", list);
		});
		//带返回值
		List<String> list = iSftpClient.supplyOpen(sftp -> sftp.list(DIR_PATH));
		log.info("获取文件列表内容如下:{}",list);
	}

	//删除
	@Order(4)
	@Test
	public void testDelete(){
		iSftpClient.open(sftp -> {
			sftp.delete(DIR_PATH,FILE_NAME);
			log.info("文件删除成功,目录:{},名称:{}",DIR_PATH,FILE_NAME);

		});

	}




	/**
	 * 	 https://xiaoashuo.github.io/relaxed-docs/zh/question/SFTP%E5%B7%A5%E5%85%B7%E7%B1%BB%E5%BC%82%E5%B8%B8.html#%E9%97%AE%E9%A2%98%E6%8F%8F%E8%BF%B0
	 * 	 依赖包 部分方法 会切换当前工作目录,导致获取文件不到 后续版本 调整完成后回到根目录(已修复)
	 * 	 替代解决方案 所有的的路径使用绝对路径 测试复现方法如下
	 * 	 sftp.getChannelSftp().cd("/");
	 * 	 sftp.delete("tianze3/55030002/upload","BILLING_20241221.ok");
	 * 	 String pwd = sftp.getChannelSftp().pwd();
	 * 	 boolean exista =
	 * 	 sftp.isExist(PathUtils.normalizePathBySlash("tianze3/55030002/upload",
	 * 		 "BILLING_20241221.ok"));
	 */
	@SneakyThrows
	@Test
	public void testWorkDir() {
		// 模板文件
		File locaFile = getLocalFile();
		//上传文件 当前工作目录会变更
		iSftpClient.open(sftp->{
			printWorkDir(sftp);
			sftp.upload(DIR_PATH,FILE_NAME,locaFile);
			printWorkDir(sftp);
		});
		byte[] bytes = iSftpClient.supplyOpen(sftp -> {
			printWorkDir(sftp);
			byte[] download = sftp.download( DIR_PATH, FILE_NAME);
			printWorkDir(sftp);
			return download;
		});
		log.info("当前字节大小:{}",bytes.length);
	}

	private static File getLocalFile() {
		File locaFile =FileUtils.loadClassPathFile("template\\poi\\Poi测试模板.docx");
		return locaFile;
	}

	private static void printWorkDir(ISftpExecutor sftp) {
		try {
			String pwd = sftp.getChannelSftp().pwd();
			log.info("当前工作目录:{}",pwd);
		} catch (SftpException e) {
			throw new RuntimeException(e);
		}
	}

}
