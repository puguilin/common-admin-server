package com.github.guilin.service.impl;

import com.github.guilin.domain.entity.SysLog;
import com.github.guilin.service.SysLogAsyncService;
import com.github.guilin.service.SysLogService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SysLogAsyncServiceImpl implements SysLogAsyncService {
    @Resource
    private SysLogService sysLogService;

    @Async
    @Override
    public void add(SysLog sysLog) {
        sysLogService.add(sysLog);
    }
}
