package com.github.guilin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.guilin.domain.entity.SysLog;
import com.github.guilin.mapper.SysLogMapper;
import com.github.guilin.service.SysLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SysLogServiceImpl implements SysLogService {
    @Resource
    private SysLogMapper sysLogMapper;

    @Transactional
    @Override
    public int add(SysLog sysLog) {
        sysLog.setCreateTime(LocalDateTime.now());
        return sysLogMapper.insert(sysLog);
    }

    @Transactional
    @Override
    public int deleteById(Long id) {
        return sysLogMapper.deleteByPrimaryKey(id);
    }

    @Override
    public SysLog getById(Long id) {
        return sysLogMapper.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo<SysLog> getPage(SysLog query, Integer pageNum, Integer pageSize) {
        if (pageNum != null && pageSize != null) {
            PageHelper.startPage(pageNum, pageSize);
        }
        List<SysLog> list = sysLogMapper.selectList(query);
        return new PageInfo<>(list);
    }

    @Transactional
    @Override
    public int clear() {
        return sysLogMapper.clear();
    }
}
