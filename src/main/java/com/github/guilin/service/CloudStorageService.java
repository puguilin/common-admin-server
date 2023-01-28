package com.github.guilin.service;

import com.github.guilin.domain.vo.CloudStorageConfigVo;

import java.util.List;

public interface CloudStorageService {
    boolean put(CloudStorageConfigVo cloudStorageConfig, byte[] data, String key);

    boolean delete(CloudStorageConfigVo cloudStorageConfig, String key);

    boolean batchDelete(CloudStorageConfigVo cloudStorageConfig, List<String> keys);
}
