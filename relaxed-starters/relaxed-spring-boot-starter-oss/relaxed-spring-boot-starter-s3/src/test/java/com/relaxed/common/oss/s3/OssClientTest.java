package com.relaxed.common.oss.s3;

import com.relaxed.common.oss.s3.domain.StreamMeta;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

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

    private OssClient client;

    public static void main(String[] args) {
        OssClientBuilder ossClientBuilder = new OssClientBuilder();
        ossClientBuilder.rootPath("test");
        OssClient build = ossClientBuilder.build();
        System.out.println(build);
    }


    @SneakyThrows
    @Test
    void tx(){
        properties = new OssProperties();
        properties.setRootPath("/");
        properties.setAcl(null);

        properties.setBucket("test-1258769891");
        properties.setAccessKey("AKIDSoxtbBevynOmxud7NPDn9HBqHXZNxKTO");
        properties.setAccessSecret("lIbgXCestgFajfPwEI7j7HFzgJtRp1zk");
        // 根据自己的需求配置
        properties.setEndpoint("https://test-1258769891.cos.ap-shanghai.myqcloud.com");
        properties.setRegion("ap-shanghai");
        OssClientBuilder ossClientBuilder = new OssClientBuilder();
        ossClientBuilder.rootPath(properties.getRootPath());
        ossClientBuilder.region(properties.getRegion());
        ossClientBuilder.accessKey(properties.getAccessKey());
        ossClientBuilder.accessSecret(properties.getAccessSecret());
        ossClientBuilder.bucket(properties.getBucket());
        ossClientBuilder.domain(properties.getDomain());
        ossClientBuilder.pathStyleAccess(properties.getPathStyleAccess());
        ossClientBuilder.acl(properties.getAcl());
        OssClient ossClient = ossClientBuilder.build();
        File file = new File("D:\\other\\oss-test.log");
        InputStream stream = new FileInputStream(file);

        StreamMeta streamMeta = StreamMeta.convertToByteStreamMeta(stream);
        String fullPath = ossClient.upload(streamMeta.getInputStream(), streamMeta.getSize(), "oss.txt");

        log.info("完全路径{}",fullPath);

    }
}
