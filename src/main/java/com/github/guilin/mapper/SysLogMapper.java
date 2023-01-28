package com.github.guilin.mapper;

import com.github.guilin.domain.entity.SysLog;

import java.util.List;

public interface SysLogMapper {
    int insert(SysLog sysLog);

    int deleteByPrimaryKey(Long id);

    int updateByPrimaryKey(SysLog sysLog);

    SysLog selectByPrimaryKey(Long id);

    List<SysLog> selectList(SysLog sysLog);

    int clear();
}
