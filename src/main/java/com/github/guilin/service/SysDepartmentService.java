package com.github.guilin.service;

import com.github.guilin.domain.entity.SysDepartment;

import java.util.List;

public interface SysDepartmentService {
    int add(SysDepartment sysDepartment);

    int deleteById(Long id);

    int updateById(SysDepartment sysDepartment);

    SysDepartment getById(Long id);

    List<SysDepartment> getList();

    List<SysDepartment> search(SysDepartment query);

    List<SysDepartment> getChild(Long id);

    List<SysDepartment> getOffspring(Long id);
}
