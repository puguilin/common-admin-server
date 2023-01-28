package com.github.guilin.mapper;

import com.github.guilin.domain.entity.SysConfig;
import java.util.List;

public interface SysConfigMapper {
    int insert(SysConfig sysConfig);

    int deleteByPrimaryKey(Long id);

    int updateByPrimaryKey(SysConfig sysConfig);

    SysConfig selectByPrimaryKey(Long id);

    List<SysConfig> selectList(SysConfig sysConfig);

    int updateValueByKey(SysConfig sysConfig);
}
