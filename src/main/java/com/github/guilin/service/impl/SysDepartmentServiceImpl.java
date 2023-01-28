package com.github.guilin.service.impl;

import com.github.guilin.domain.entity.SysDepartment;
import com.github.guilin.mapper.SysDepartmentMapper;
import com.github.guilin.service.SysDepartmentService;
import com.github.guilin.utils.MakeUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SysDepartmentServiceImpl implements SysDepartmentService {
    @Resource
    private SysDepartmentMapper sysDepartmentMapper;

    @Transactional
    @Override
    public int add(SysDepartment sysDepartment) {
        sysDepartment.setCreateTime(LocalDateTime.now());
        return sysDepartmentMapper.insert(sysDepartment);
    }

    @Transactional
    @Override
    public int deleteById(Long id) {
        return sysDepartmentMapper.deleteByPrimaryKey(id);
    }

    @Transactional
    @Override
    public int updateById(SysDepartment sysDepartment) {
        return sysDepartmentMapper.updateByPrimaryKey(sysDepartment);
    }

    @Override
    public SysDepartment getById(Long id) {
        return sysDepartmentMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<SysDepartment> getList() {
        List<SysDepartment> sysDepartmentList = sysDepartmentMapper.selectList(null);
        return MakeUtils.makeDepartmentTree(sysDepartmentList, 0L);
    }

    @Override
    public List<SysDepartment> search(SysDepartment query) {
        return sysDepartmentMapper.selectList(query);
    }

    @Override
    public List<SysDepartment> getChild(Long id) {
        SysDepartment query = new SysDepartment();
        query.setParentId(id);
        return sysDepartmentMapper.selectList(query);
    }

    @Override
    public List<SysDepartment> getOffspring(Long id) {
        List<SysDepartment> childList = new ArrayList<>();
        recursionParse(id, childList);
        return childList;
    }

    private void recursionParse(Long parentId, List<SysDepartment> childList) {
        List<SysDepartment> childs = getChild(parentId);
        if (childs.isEmpty()) return;
        childList.addAll(childs);
        childs.forEach(item -> recursionParse(item.getId(), childList));
    }
}
