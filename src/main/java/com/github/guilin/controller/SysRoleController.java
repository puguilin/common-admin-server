package com.github.guilin.controller;

import com.github.pagehelper.PageInfo;
import com.github.guilin.annotation.SysLogRecord;
import com.github.guilin.domain.entity.SysRole;
import com.github.guilin.domain.vo.PermissionVo;
import com.github.guilin.domain.vo.ResultVo;
import com.github.guilin.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Api(tags = "系统角色接口")
@RestController
public class SysRoleController {
    @Resource
    private SysRoleService sysRoleService;

    @SysLogRecord("新增系统角色信息")
    @PreAuthorize("hasAuthority('sys:role:add')")
    @ApiOperation("新增系统角色信息")
    @PostMapping("/api/sysRole/add")
    public ResultVo add(@RequestBody SysRole sysRole) {
        int rows = sysRoleService.add(sysRole);
        if (rows > 0) {
            return ResultVo.success("添加成功！");
        } else {
            return ResultVo.fail("添加失败！");
        }
    }

    @SysLogRecord("根据主键删除角色")
    @PreAuthorize("hasAuthority('sys:role:delete')")
    @ApiOperation("根据主键删除角色")
    @GetMapping("/api/sysRole/deleteById")
    public ResultVo deleteById(Long id) {
        int rows = sysRoleService.deleteById(id);
        if (rows > 0) {
            return ResultVo.success("删除成功！");
        } else {
            return ResultVo.fail("删除失败！");
        }
    }

    @SysLogRecord("更新系统角色信息")
    @PreAuthorize("hasAuthority('sys:role:update')")
    @ApiOperation("更新系统角色信息")
    @PostMapping("/api/sysRole/update")
    public ResultVo updateById(@RequestBody SysRole sysRole) {
        int rows = sysRoleService.updateById(sysRole);
        if (rows > 0) {
            return ResultVo.success("更新成功！");
        } else {
            return ResultVo.fail("更新失败！");
        }
    }

    @SysLogRecord("根据主键获取角色")
    @PreAuthorize("hasAuthority('sys:role:update')")
    @ApiOperation("根据主键获取角色")
    @GetMapping("/api/sysRole/getById")
    public ResultVo getById(Long id) {
        SysRole sysRole = sysRoleService.getById(id);
        return ResultVo.successWith(sysRole);
    }

    @SysLogRecord("分页获取角色列表")
    @ApiOperation("分页获取角色列表")
    @GetMapping("/api/sysRole/getPage")
    public ResultVo getPage(SysRole query, Integer pageNum, Integer pageSize) {
        PageInfo<SysRole> sysRolePage = sysRoleService.getPage(query, pageNum, pageSize);
        return ResultVo.successWith(sysRolePage);
    }

    @SysLogRecord("根据主键获取权限")
    @PreAuthorize("hasAuthority('sys:role:assign')")
    @ApiOperation("根据主键获取权限")
    @GetMapping("/api/sysRole/getPermission")
    public ResultVo getPermission(Long id) {
        PermissionVo permissionVo = sysRoleService.getPermission(id);
        return ResultVo.successWith(permissionVo);
    }

    @SysLogRecord("指定角色分配菜单")
    @PreAuthorize("hasAuthority('sys:role:assign')")
    @ApiOperation("指定角色分配菜单")
    @GetMapping("/api/sysRole/assignMenu")
    public ResultVo assignMenu(Long rid, @RequestParam(value = "mids[]", required = false) List<Long> mids) {
        int rows = sysRoleService.assignMenu(rid, mids);
        if (rows > 0) {
            return ResultVo.success("分配菜单成功！");
        } else {
            return ResultVo.fail("分配菜单失败！");
        }
    }
}
