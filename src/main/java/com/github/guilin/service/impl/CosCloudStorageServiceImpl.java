package com.github.guilin.service.impl;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.DeleteObjectsRequest;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.region.Region;
import com.github.guilin.domain.vo.CloudStorageConfigVo;
import com.github.guilin.service.CloudStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * 腾讯云存储
 */
@Slf4j
@Service("cosCloudStorageServiceImpl")
public class CosCloudStorageServiceImpl implements CloudStorageService {
    @Override
    public boolean put(CloudStorageConfigVo cloudStorageConfig, byte[] data, String key) {
        String secretId = cloudStorageConfig.getAccessKey();
        String secretKey = cloudStorageConfig.getSecretKey();
        String bucket = cloudStorageConfig.getBucket();
        String region = cloudStorageConfig.getRegion();

        try {
            COSCredentials cosCredentials = new BasicCOSCredentials(secretId, secretKey);
            ClientConfig clientConfig = new ClientConfig(new Region(region));
            clientConfig.setHttpProtocol(HttpProtocol.https);
            COSClient cosClient = new COSClient(cosCredentials, clientConfig);
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(data.length);
            InputStream inputStream = new ByteArrayInputStream(data);
            cosClient.putObject(bucket, key, inputStream, objectMetadata);
            cosClient.shutdown();
            log.debug("Upload Success!");
            return true;
        } catch (Exception e) {
            log.debug("Upload Fail! Message = {}", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(CloudStorageConfigVo cloudStorageConfig, String key) {
        String secretId = cloudStorageConfig.getAccessKey();
        String secretKey = cloudStorageConfig.getSecretKey();
        String bucket = cloudStorageConfig.getBucket();
        String region = cloudStorageConfig.getRegion();

        try {
            COSCredentials cosCredentials = new BasicCOSCredentials(secretId, secretKey);
            ClientConfig clientConfig = new ClientConfig(new Region(region));
            clientConfig.setHttpProtocol(HttpProtocol.https);
            COSClient cosClient = new COSClient(cosCredentials, clientConfig);
            cosClient.deleteObject(bucket, key);
            cosClient.shutdown();
            log.debug("Delete Success!");
            return true;
        } catch (Exception e) {
            log.debug("Delete Fail! Message = {}", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean batchDelete(CloudStorageConfigVo cloudStorageConfig, List<String> keys) {
        String secretId = cloudStorageConfig.getAccessKey();
        String secretKey = cloudStorageConfig.getSecretKey();
        String bucket = cloudStorageConfig.getBucket();
        String region = cloudStorageConfig.getRegion();

        try {
            COSCredentials cosCredentials = new BasicCOSCredentials(secretId, secretKey);
            ClientConfig clientConfig = new ClientConfig(new Region(region));
            clientConfig.setHttpProtocol(HttpProtocol.https);
            COSClient cosClient = new COSClient(cosCredentials, clientConfig);
            cosClient.deleteObjects(new DeleteObjectsRequest(bucket).withKeys(keys.toArray(new String[keys.size()])));
            cosClient.shutdown();
            log.debug("Batch delete Success!");
            return true;
        } catch (Exception e) {
            log.debug("Batch delete Fail! Message = {}", e.getMessage());
            return false;
        }
    }
}
