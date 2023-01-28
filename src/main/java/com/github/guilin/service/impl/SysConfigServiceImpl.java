package com.github.guilin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.guilin.annotation.RedisCache;
import com.github.guilin.domain.entity.SysConfig;
import com.github.guilin.mapper.SysConfigMapper;
import com.github.guilin.service.SysConfigService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysConfigServiceImpl implements SysConfigService {
    @Resource
    private SysConfigMapper sysConfigMapper;

    @RedisCache(flush = true)
    @Transactional
    @Override
    public int add(SysConfig sysConfig) {
        sysConfig.setCreateTime(LocalDateTime.now());
        return sysConfigMapper.insert(sysConfig);
    }

    @RedisCache(flush = true)
    @Transactional
    @Override
    public int deleteById(Long id) {
        return sysConfigMapper.deleteByPrimaryKey(id);
    }

    @RedisCache(flush = true)
    @Transactional
    @Override
    public int updateById(SysConfig sysConfig) {
        return sysConfigMapper.updateByPrimaryKey(sysConfig);
    }

    @Override
    public SysConfig getById(Long id) {
        return sysConfigMapper.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo<SysConfig> getPage(SysConfig query, Integer pageNum, Integer pageSize) {
        if (pageNum != null && pageSize != null) {
            PageHelper.startPage(pageNum, pageSize);
        }
        List<SysConfig> list = sysConfigMapper.selectList(query);
        return new PageInfo<>(list);
    }

    @RedisCache
    @Override
    public Map<String, String> selectMap() {
        List<SysConfig> list = sysConfigMapper.selectList(null);
        Map<String, String> sysConfigs = new HashMap<>();
        for (SysConfig sysConfig : list) {
            sysConfigs.put(sysConfig.getKey(), sysConfig.getValue());
        }
        return sysConfigs;
    }

    @RedisCache(flush = true)
    @Override
    public int updateValueByKey(SysConfig sysConfig) {
        return sysConfigMapper.updateValueByKey(sysConfig);
    }
}
