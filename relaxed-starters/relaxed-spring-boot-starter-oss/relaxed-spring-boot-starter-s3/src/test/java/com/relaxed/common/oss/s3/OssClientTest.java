package com.relaxed.common.oss.s3;

import com.relaxed.common.oss.s3.domain.S3UploadResult;
import com.relaxed.common.oss.s3.domain.StreamMeta;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.waiters.S3Waiter;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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

    private OssClient client;

    public static void main(String[] args) {
        OssClientBuilder ossClientBuilder = new OssClientBuilder();
        ossClientBuilder.rootPath("test");
        OssClient build = ossClientBuilder.build();
        System.out.println(build);
    }
    OssClient ossClient;

    @BeforeEach
    public void before(){
        properties = new OssProperties();
        properties.setRootPath("/");
        properties.setAcl(null);

        properties.setBucket("test-1258769891");
        properties.setAccessKey("AKIDSoxtbBevynOmxud7NPDn9HBqHXZNxKTO");
        properties.setAccessSecret("lIbgXCestgFajfPwEI7j7HFzgJtRp1zk");
        // 根据自己的需求配置
        properties.setEndpoint("cos.ap-shanghai.myqcloud.com");
        properties.setRegion("ap-shanghai");
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
         ossClient = ossClientBuilder.build();
    }
    @Test
    void delObj(){
//      ossClient.delete("email/test1.jpg");
//
//        Set<String> paths=new HashSet<>();
//        paths.add("email/test10.jpg");
//        paths.add("email/test11.jpg");
//      ossClient.deletes(paths);

//        ListObjectsRequest listObjects = ListObjectsRequest
//                .builder()
//                .bucket("test-1258769891/email")
//                .prefix("email")
//                .build();
//        ListObjectsResponse res = ossClient.getS3Client().listObjects(listObjects);
//        System.out.println(res);


        //copy对象
//        CopyObjectRequest copyObjRequest =  CopyObjectRequest
//                .builder()
//
//                .sourceBucket("test-1258769891")
//                .sourceKey("test-1258769891/email/test9.jpg").destinationBucket("test-1258769891")
//                .destinationKey("test9.jpg").build();
//
//        ossClient.getS3Client().copyObject(copyObjRequest);
        CopyObjectRequest copyObjRequest =  CopyObjectRequest
                .builder()

                .sourceBucket("test-1258769891")
                .sourceKey("img/email/test9.jpg").destinationBucket("test1")
                .destinationKey("email/test4.jpg").build();

        ossClient.getS3Client().copyObject(copyObjRequest);
        log.info("成功");
//        String encodedUrl = null;
//        try {
//            encodedUrl = URLEncoder.encode("test-1258769891" + "/" + "email/test12.jpg", StandardCharsets.UTF_8.toString());
//        } catch (UnsupportedEncodingException e) {
//            System.out.println("URL could not be encoded: " + e.getMessage());
//        }
//        CopyObjectRequest copyReq = CopyObjectRequest.builder()
//                .copySource("test-1258769891" + "/" + "email/test12.jpg")
//                .destinationBucket("test-1258769891")
//                .destinationKey("test12.jpg")
//                .build();
//
//        try {
//            CopyObjectResponse copyRes = ossClient.getS3Client().copyObject(copyReq);
//            System.out.println(copyRes.copyObjectResult().toString());
//        } catch (S3Exception e) {
//            System.err.println(e.awsErrorDetails().errorMessage());
//
//        }
    }
    @SneakyThrows
    @Test
    void tx(){
//       上传
//        File file = new File("D:\\other\\100000\\test4.jpg");
//        InputStream stream = new FileInputStream(file);
//
//        StreamMeta streamMeta = StreamMeta.convertToByteStreamMeta(stream);
//        S3UploadResult s3UploadResult = ossClient.upload(streamMeta.getInputStream(), streamMeta.getSize(), "test", "img/test4.jpg");
//
//        log.info("上传结果:{}",s3UploadResult);
//        String relativePath = ossClient.upload(streamMeta.getInputStream(), streamMeta.getSize(), "email/test9.jpg");
//        String fullPathDownloadUrl = ossClient.getDownloadUrl(relativePath);
//        log.info("相对路径{}",relativePath);
//        log.info("全路径{}",fullPathDownloadUrl);
        //删除
//        ossClient.delete("/test","img/test4.jpg");

//        Set<String> paths=new HashSet<>();
//        paths.add("email/test11.jpg");
//        paths.add("email/test12.jpg");
//        ossClient.deletes("test",paths);

       //copy
//        S3UploadResult copy = ossClient.copy("img", "email/test9.jpg", "test2", "img/test4.jpg");
//        log.info("adada{}",copy);
       //列出




        ListObjectsRequest listObjects = ListObjectsRequest
                .builder()
                .bucket("/test-1258769891")

                .build();

        ListObjectsResponse res = ossClient.getS3Client().listObjects(listObjects);
        List<S3Object> objects = res.contents();

        for (ListIterator iterVals = objects.listIterator(); iterVals.hasNext(); ) {
            S3Object myValue = (S3Object) iterVals.next();
            System.out.print("\n The name of the key is " + myValue.key());
            System.out.print("\n The owner is " + myValue.owner());

        }

    }
}
