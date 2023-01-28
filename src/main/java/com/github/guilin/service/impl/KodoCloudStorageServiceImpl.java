package com.github.guilin.service.impl;

import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.github.guilin.domain.vo.CloudStorageConfigVo;
import com.github.guilin.service.CloudStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 七牛云存储
 */
@Slf4j
@Service("kodoCloudStorageServiceImpl")
public class KodoCloudStorageServiceImpl implements CloudStorageService {
    @Override
    public boolean put(CloudStorageConfigVo cloudStorageConfig, byte[] data, String key) {
        String accessKey = cloudStorageConfig.getAccessKey();
        String secretKey = cloudStorageConfig.getSecretKey();
        String bucket = cloudStorageConfig.getBucket();
        String regionName = cloudStorageConfig.getRegion();
        Region region = null;

        if ("huadong".equals(regionName)) {
            region = Region.huadong();
        }
        if ("huabei".equals(regionName)) {
            region = Region.huabei();
        }
        if ("huanan".equals(regionName)) {
            region = Region.huanan();
        }
        if ("beimei".equals(regionName)) {
            region = Region.beimei();
        }
        if ("xinjiapo".equals(regionName)) {
            region = Region.xinjiapo();
        }

        try {
            Configuration cfg = new Configuration(region);
            UploadManager uploadManager = new UploadManager(cfg);
            Auth auth = Auth.create(accessKey, secretKey);
            String token = auth.uploadToken(bucket);
            uploadManager.put(data, key, token);
            log.debug("Upload Success!");
            return true;
        } catch (Exception e) {
            log.debug("Upload Fail! Message = {}", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(CloudStorageConfigVo cloudStorageConfig, String key) {
        String accessKey = cloudStorageConfig.getAccessKey();
        String secretKey = cloudStorageConfig.getSecretKey();
        String bucket = cloudStorageConfig.getBucket();
        String regionName = cloudStorageConfig.getRegion();
        Region region = null;

        if ("huadong".equals(regionName)) {
            region = Region.huadong();
        }
        if ("huabei".equals(regionName)) {
            region = Region.huabei();
        }
        if ("huanan".equals(regionName)) {
            region = Region.huanan();
        }
        if ("beimei".equals(regionName)) {
            region = Region.beimei();
        }
        if ("xinjiapo".equals(regionName)) {
            region = Region.xinjiapo();
        }

        try {
            Configuration cfg = new Configuration(region);
            Auth auth = Auth.create(accessKey, secretKey);
            BucketManager bucketManager = new BucketManager(auth, cfg);
            bucketManager.delete(bucket, key);
            log.debug("Delete Success!");
            return true;
        } catch (Exception e) {
            log.debug("Delete Fail! Message = {}", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean batchDelete(CloudStorageConfigVo cloudStorageConfig, List<String> keys) {
        String accessKey = cloudStorageConfig.getAccessKey();
        String secretKey = cloudStorageConfig.getSecretKey();
        String bucket = cloudStorageConfig.getBucket();
        String regionName = cloudStorageConfig.getRegion();
        Region region = null;

        if ("huadong".equals(regionName)) {
            region = Region.huadong();
        }
        if ("huabei".equals(regionName)) {
            region = Region.huabei();
        }
        if ("huanan".equals(regionName)) {
            region = Region.huanan();
        }
        if ("beimei".equals(regionName)) {
            region = Region.beimei();
        }
        if ("xinjiapo".equals(regionName)) {
            region = Region.xinjiapo();
        }

        try {
            Configuration cfg = new Configuration(region);
            Auth auth = Auth.create(accessKey, secretKey);
            BucketManager bucketManager = new BucketManager(auth, cfg);
            BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
            batchOperations.addDeleteOp(bucket, keys.toArray(new String[keys.size()]));
            bucketManager.batch(batchOperations);
            log.debug("Batch Delete Success!");
            return true;
        } catch (Exception e) {
            log.debug("Batch Delete Fail! Message = {}", e.getMessage());
            return false;
        }
    }
}
