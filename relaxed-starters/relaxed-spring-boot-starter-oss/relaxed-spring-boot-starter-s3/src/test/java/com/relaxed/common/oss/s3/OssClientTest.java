package com.relaxed.common.oss.s3;


import com.relaxed.common.oss.s3.domain.StreamMeta;
import com.relaxed.common.oss.s3.modifier.DefaultPathModifier;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

/**
 * @author Yakir
 * @Topic OssClientTest
 * @Description
 * @date 2021/11/26 9:50
 * @Version 1.0
 */
@Slf4j
public class OssClientTest {
    private OssProperties properties;




    private OssClient ossClient;

    @BeforeEach
    public void before(){
        buildTxProperties();
        OssClientBuilder ossClientBuilder = new OssClientBuilder();
        ossClientBuilder.rootPath(properties.getRootPath());
        ossClientBuilder.region(properties.getRegion());
        ossClientBuilder.accessKey(properties.getAccessKey());
        ossClientBuilder.accessSecret(properties.getAccessSecret());
        ossClientBuilder.bucket(properties.getBucket());
        ossClientBuilder.domain(properties.getDomain());
        ossClientBuilder.pathStyleAccess(properties.getPathStyleAccess());
        ossClientBuilder.endpoint(properties.getEndpoint());
        ossClientBuilder.acl(properties.getAcl());
        ossClientBuilder.pathModifier(new DefaultPathModifier());
         ossClient = ossClientBuilder.build();
    }
    private void buildAliProperties() {
        properties = new OssProperties();
        properties.setRootPath("/");
        properties.setAcl(null);
        properties.setBucket("yakir-oss");
        properties.setAccessKey("LTAI4FiGxjNniXjFNtddUJNT");
        properties.setAccessSecret("BC3Sd5dbcDxU9LsswXFCt4tgc19uRF");
        // 根据自己的需求配置
        properties.setEndpoint("oss-cn-beijing.aliyuncs.com");
        properties.setRegion("oss-cn-beijing");
    }
    private void buildTxProperties() {
        properties = new OssProperties();
        properties.setRootPath("/");
        properties.setAcl(null);
        properties.setBucket("test-1258769891");
        properties.setAccessKey("AKIDSoxtbBevynOmxud7NPDn9HBqHXZNxKTO");
        properties.setAccessSecret("lIbgXCestgFajfPwEI7j7HFzgJtRp1zk");
        // 根据自己的需求配置
        properties.setEndpoint("cos.ap-shanghai.myqcloud.com");
        properties.setRegion("ap-shanghai");
    }


    @SneakyThrows
    @Test
    void txUpload(){
        String bucketName = "test";
        String relativePath = "img/test3.jpg";
        File file = new File("D:\\other\\100000\\test4.jpg");
        InputStream stream = new FileInputStream(file);
        StreamMeta streamMeta = StreamMeta.convertToByteStreamMeta(stream);
        String downloadUrl = ossClient.upload(streamMeta.getInputStream(), streamMeta.getSize(), bucketName, relativePath);
        log.info("上传结果:{}",downloadUrl);
        Assert.state(ossClient.getDownloadUrl(bucketName,relativePath).equals(downloadUrl),"下载地址不一致");
    }

    @SneakyThrows
    @Test
    void txCopy(){
        String destBucketName = "dest";
        String destPath = "test4.jpg";
        String copyDownloadUrl = ossClient.copy("test", "img/test3.jpg", destBucketName, destPath);
        Assert.state(ossClient.getDownloadUrl(destBucketName,destPath).equals(copyDownloadUrl),"Copy下载地址不一致");
        log.info("copy结果:{}",copyDownloadUrl);
    }

    @SneakyThrows
    @Test
    void txDelete(){
        //单条删除
        String bucketName = "test";
        String relativePath = "img/test4.jpg";
        ossClient.delete(bucketName,relativePath);

        //批量删除
        Set<String> paths=new HashSet<>();
        paths.add("img/test5.jpg");
        paths.add("img/test6.jpg");
        ossClient.batchDelete(bucketName,paths);
    }
    @SneakyThrows
    @Test
    void txList(){
        String bucketName="test";
        List<String> paths = ossClient.list(bucketName);
        log.info("结果数组:{}",paths);
    }
}