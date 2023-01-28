package com.github.guilin.service;

import com.github.pagehelper.PageInfo;
import com.github.guilin.domain.entity.SysConfig;

import java.util.Map;

public interface SysConfigService {
    int add(SysConfig sysConfig);

    int deleteById(Long id);

    int updateById(SysConfig sysConfig);

    SysConfig getById(Long id);

    PageInfo<SysConfig> getPage(SysConfig query, Integer pageNum, Integer pageSize);

    Map<String, String> selectMap();

    int updateValueByKey(SysConfig sysConfig);
}
