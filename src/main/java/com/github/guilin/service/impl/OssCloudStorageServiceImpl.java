package com.github.guilin.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.github.guilin.domain.vo.CloudStorageConfigVo;
import com.github.guilin.service.CloudStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * 阿里云存储
 */
@Slf4j
@Service("ossCloudStorageServiceImpl")
public class OssCloudStorageServiceImpl implements CloudStorageService {
    @Override
    public boolean put(CloudStorageConfigVo cloudStorageConfig, byte[] data, String key) {
        String accessKeyId = cloudStorageConfig.getAccessKey();
        String accessKeySecret = cloudStorageConfig.getSecretKey();
        String bucket = cloudStorageConfig.getBucket();
        String endpoint = cloudStorageConfig.getRegion();

        try {
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            InputStream inputStream = new ByteArrayInputStream(data);
            ossClient.putObject(bucket, key, inputStream);
            ossClient.shutdown();
            log.debug("Upload Success!");
            return true;
        } catch (Exception e) {
            log.debug("Upload Fail! Message = {}", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(CloudStorageConfigVo cloudStorageConfig, String key) {
        String accessKeyId = cloudStorageConfig.getAccessKey();
        String accessKeySecret = cloudStorageConfig.getSecretKey();
        String bucket = cloudStorageConfig.getBucket();
        String endpoint = cloudStorageConfig.getRegion();

        try {
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            ossClient.deleteObject(bucket, key);
            ossClient.shutdown();
            log.debug("Delete Success!");
            return true;
        } catch (Exception e) {
            log.debug("Delete Fail! Message = {}", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean batchDelete(CloudStorageConfigVo cloudStorageConfig, List<String> keys) {
        String accessKeyId = cloudStorageConfig.getAccessKey();
        String accessKeySecret = cloudStorageConfig.getSecretKey();
        String bucket = cloudStorageConfig.getBucket();
        String endpoint = cloudStorageConfig.getRegion();

        try {
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            ossClient.deleteObjects(new DeleteObjectsRequest(bucket).withKeys(keys));
            ossClient.shutdown();
            log.debug("Batch Delete Success!");
            return true;
        } catch (Exception e) {
            log.debug("Batch Delete Fail! Message = {}", e.getMessage());
            return false;
        }
    }
}
