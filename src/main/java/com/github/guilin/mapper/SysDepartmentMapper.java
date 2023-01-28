package com.github.guilin.mapper;

import com.github.guilin.domain.entity.SysDepartment;

import java.util.List;

public interface SysDepartmentMapper {
    int insert(SysDepartment sysDepartment);

    int deleteByPrimaryKey(Long id);

    int updateByPrimaryKey(SysDepartment sysDepartment);

    SysDepartment selectByPrimaryKey(Long id);

    List<SysDepartment> selectList(SysDepartment sysDepartment);
}
