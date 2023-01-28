package com.github.guilin.controller;

import com.github.guilin.annotation.SysLogRecord;
import com.github.guilin.domain.entity.SysDepartment;
import com.github.guilin.domain.entity.SysUser;
import com.github.guilin.domain.vo.ResultVo;
import com.github.guilin.service.SysDepartmentService;
import com.github.guilin.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Api(tags = "系统部门接口")
@RestController
public class SysDepartmentController {
    @Resource
    private SysDepartmentService sysDepartmentService;
    @Resource
    private SysUserService sysUserService;

    @SysLogRecord("新增系统部门信息")
    @PreAuthorize("hasAuthority('sys:department:add')")
    @ApiOperation("新增系统部门信息")
    @PostMapping("/api/sysDepartment/add")
    public ResultVo add(@RequestBody SysDepartment sysDepartment) {
        int rows = sysDepartmentService.add(sysDepartment);
        if (rows > 0) {
            return ResultVo.success("添加成功！");
        } else {
            return ResultVo.fail("添加失败！");
        }
    }

    @SysLogRecord("根据主键删除部门")
    @PreAuthorize("hasAuthority('sys:department:delete')")
    @ApiOperation("根据主键删除部门")
    @GetMapping("/api/sysDepartment/deleteById")
    public ResultVo deleteById(Long id) {
        List<SysDepartment> child = sysDepartmentService.getChild(id);
        if (child.isEmpty()) {
            List<SysDepartment> offspring = sysDepartmentService.getOffspring(id);
            List<Long> deptIds = offspring.stream().map(item -> item.getId()).collect(Collectors.toList());
            deptIds.add(id);
            List<SysUser> sysUsers = sysUserService.selectByDeptIds(deptIds);
            if (sysUsers.isEmpty()) {
                int rows = sysDepartmentService.deleteById(id);
                if (rows > 0) {
                    return ResultVo.success("删除成功！");
                } else {
                    return ResultVo.fail("删除失败！");
                }
            } else {
                return ResultVo.fail("该部门存在用户，不能直接删除！");
            }
        } else {
            return ResultVo.fail("该部门存在下级，不能直接删除！");
        }
    }

    @SysLogRecord("更新系统部门信息")
    @PreAuthorize("hasAuthority('sys:department:update')")
    @ApiOperation("更新系统部门信息")
    @PostMapping("/api/sysDepartment/update")
    public ResultVo updateById(@RequestBody SysDepartment sysDepartment) {
        Long parentId = sysDepartment.getParentId();
        Long id = sysDepartment.getId();
        if (!parentId.equals(id)) {
            int rows = sysDepartmentService.updateById(sysDepartment);
            if (rows > 0) {
                return ResultVo.success("更新成功！");
            } else {
                return ResultVo.fail("更新失败！");
            }
        } else {
            return ResultVo.fail("该部门的上级，不能指向自己！");
        }
    }

    @SysLogRecord("根据主键获取部门")
    @PreAuthorize("hasAuthority('sys:department:update')")
    @ApiOperation("根据主键获取部门")
    @GetMapping("/api/sysDepartment/getById")
    public ResultVo getById(Long id) {
        SysDepartment sysDepartment = sysDepartmentService.getById(id);
        return ResultVo.successWith(sysDepartment);
    }

    @SysLogRecord("获取系统部门列表")
    @ApiOperation("获取系统部门列表")
    @GetMapping("/api/sysDepartment/getList")
    public ResultVo getList() {
        List<SysDepartment> sysDepartmentList = sysDepartmentService.getList();
        return ResultVo.successWith(sysDepartmentList);
    }

    @SysLogRecord("条件查询系统部门")
    @PreAuthorize("hasAuthority('sys:department:search')")
    @ApiOperation("条件查询系统部门")
    @GetMapping("/api/sysDepartment/search")
    public ResultVo search(SysDepartment query) {
        List<SysDepartment> sysDepartmentList = sysDepartmentService.search(query);
        return ResultVo.successWith(sysDepartmentList);
    }
}
