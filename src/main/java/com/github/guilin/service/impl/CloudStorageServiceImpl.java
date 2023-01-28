package com.github.guilin.service.impl;

import com.github.guilin.domain.vo.CloudStorageConfigVo;
import com.github.guilin.service.CloudStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service("cloudStorageServiceImpl")
public class CloudStorageServiceImpl implements CloudStorageService {
    @Resource//七牛云存储
    private CloudStorageService kodoCloudStorageServiceImpl;
    @Resource//阿里云存储
    private CloudStorageService ossCloudStorageServiceImpl;
    @Resource//腾讯云存储
    private CloudStorageService cosCloudStorageServiceImpl;

    @Override
    public boolean put(CloudStorageConfigVo cloudStorageConfig, byte[] data, String key) {
        CloudStorageService cloudStorageService = null;
        Integer type = cloudStorageConfig.getType();
        if (type.equals(0)) {
            cloudStorageService = kodoCloudStorageServiceImpl;
        }
        if (type.equals(1)) {
            cloudStorageService = ossCloudStorageServiceImpl;
        }
        if (type.equals(2)) {
            cloudStorageService = cosCloudStorageServiceImpl;
        }
        return cloudStorageService.put(cloudStorageConfig, data, key);
    }

    @Override
    public boolean delete(CloudStorageConfigVo cloudStorageConfig, String key) {
        CloudStorageService cloudStorageService = null;
        Integer type = cloudStorageConfig.getType();
        if (type.equals(0)) {
            cloudStorageService = kodoCloudStorageServiceImpl;
        }
        if (type.equals(1)) {
            cloudStorageService = ossCloudStorageServiceImpl;
        }
        if (type.equals(2)) {
            cloudStorageService = cosCloudStorageServiceImpl;
        }
        return cloudStorageService.delete(cloudStorageConfig, key);
    }

    @Override
    public boolean batchDelete(CloudStorageConfigVo cloudStorageConfig, List<String> keys) {
        CloudStorageService cloudStorageService = null;
        Integer type = cloudStorageConfig.getType();
        if (type.equals(0)) {
            cloudStorageService = kodoCloudStorageServiceImpl;
        }
        if (type.equals(1)) {
            cloudStorageService = ossCloudStorageServiceImpl;
        }
        if (type.equals(2)) {
            cloudStorageService = cosCloudStorageServiceImpl;
        }
        return cloudStorageService.batchDelete(cloudStorageConfig, keys);
    }
}
