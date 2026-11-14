package com.sky.utils;

import com.aliyun.oss.*;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.common.comm.SignVersion;
import com.aliyun.oss.model.ObjectMetadata;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import java.io.ByteArrayInputStream;

@Data
@AllArgsConstructor
@Slf4j
public class AliOssUtil {

    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
    private String region;
    /**
     * 文件上传
     *
     * @param bytes
     * @param objectName
     * @return
     */
    public String upload(byte[] bytes, String objectName) throws Exception {
        if (bytes == null || bytes.length == 0) {
            throw new RuntimeException("上传的文件内容不能为空");
        }
        if (objectName == null || objectName.isEmpty()) {
            throw new RuntimeException("上传的文件路径不能为空");
        }
        if (bucketName == null || bucketName.isEmpty()) {
            throw new RuntimeException("Bucket 名称不能为空");
        }
        // SDK 只接受域名形式的 endpoint（oss-cn-beijing.aliyuncs.com），这里兼容配置里带 http(s):// 的写法
        String host = normalizeEndpoint(endpoint);
        if (host.isEmpty()) {
            throw new RuntimeException("OSS endpoint 不能为空");
        }
        if (region == null || region.isEmpty()) {
            throw new RuntimeException("V4 签名必须配置 region，例如 cn-beijing");
        }

        // 使用 V4 签名并指定 region
        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);

        OSS ossClient = OSSClientBuilder.create()
                .endpoint("https://" + host)
                .credentialsProvider(new DefaultCredentialProvider(accessKeyId, accessKeySecret))
                .clientConfiguration(clientBuilderConfiguration)
                .region(region)
                .build();

        try {
            // 显式给出内容长度，避免流式上传走 chunked + UNSIGNED-PAYLOAD 分支
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(bytes.length);
            ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(bytes), metadata);
        } catch (OSSException oe) {
            log.error("OSSException, ErrorMessage={}, ErrorCode={}, RequestId={}, HostId={}",
                    oe.getErrorMessage(), oe.getErrorCode(), oe.getRequestId(), oe.getHostId());
            throw new RuntimeException("OSS 上传失败：" + oe.getErrorMessage(), oe);
        } catch (ClientException ce) {
            log.error("ClientException, ErrorMessage={}", ce.getMessage());
            throw new RuntimeException("OSS 客户端异常：" + ce.getMessage(), ce);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }

        //文件访问路径规则 https://BucketName.Endpoint/ObjectName
        String url = "https://" + bucketName + "." + host + "/" + objectName;

        log.info("文件上传到:{}", url);

        return url;
    }

    /**
     * 去掉配置里可能带的协议头和结尾斜杠，只保留域名部分
     */
    private static String normalizeEndpoint(String rawEndpoint) {
        if (rawEndpoint == null) {
            return "";
        }
        String host = rawEndpoint.trim();
        if (host.startsWith("https://")) {
            host = host.substring("https://".length());
        } else if (host.startsWith("http://")) {
            host = host.substring("http://".length());
        }
        while (host.endsWith("/")) {
            host = host.substring(0, host.length() - 1);
        }
        return host;
    }
}
