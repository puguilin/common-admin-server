package com.github.guilin.service;

import com.github.pagehelper.PageInfo;
import com.github.guilin.domain.entity.SysLog;

public interface SysLogService {
    int add(SysLog sysLog);

    int deleteById(Long id);

    SysLog getById(Long id);

    PageInfo<SysLog> getPage(SysLog query, Integer pageNum, Integer pageSize);

    int clear();
}
