package com.github.guilin.controller;

import com.github.pagehelper.PageInfo;
import com.github.guilin.annotation.SysLogRecord;
import com.github.guilin.domain.entity.SysConfig;
import com.github.guilin.domain.vo.ResultVo;
import com.github.guilin.service.SysConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@Slf4j
@Api(tags = "系统配置接口")
@RestController
public class SysConfigController {
    @Resource
    private SysConfigService sysConfigService;

    @SysLogRecord("新增系统配置信息")
    @PreAuthorize("hasAuthority('sys:config:add')")
    @ApiOperation("新增系统配置信息")
    @PostMapping("/api/sysConfig/add")
    public ResultVo add(@RequestBody SysConfig sysConfig) {
        int rows = sysConfigService.add(sysConfig);
        if (rows > 0) {
            return ResultVo.success("添加成功！");
        } else {
            return ResultVo.fail("添加失败！");
        }
    }

    @SysLogRecord("根据主键删除配置")
    @PreAuthorize("hasAuthority('sys:config:delete')")
    @ApiOperation("根据主键删除配置")
    @GetMapping("/api/sysConfig/deleteById")
    public ResultVo deleteById(Long id) {
        int rows = sysConfigService.deleteById(id);
        if (rows > 0) {
            return ResultVo.success("删除成功！");
        } else {
            return ResultVo.fail("删除失败！");
        }
    }

    @SysLogRecord("更新系统配置信息")
    @PreAuthorize("hasAuthority('sys:config:update')")
    @ApiOperation("更新系统配置信息")
    @PostMapping("/api/sysConfig/update")
    public ResultVo updateById(@RequestBody SysConfig sysConfig) {
        int rows = sysConfigService.updateById(sysConfig);
        if (rows > 0) {
            return ResultVo.success("更新成功！");
        } else {
            return ResultVo.fail("更新失败！");
        }
    }

    @SysLogRecord("根据主键获取配置")
    @PreAuthorize("hasAuthority('sys:config:update')")
    @ApiOperation("根据主键获取配置")
    @GetMapping("/api/sysConfig/getById")
    public ResultVo getById(Long id) {
        SysConfig sysConfig = sysConfigService.getById(id);
        return ResultVo.successWith(sysConfig);
    }

    @SysLogRecord("获取系统配置分页")
    @ApiOperation("获取系统配置分页")
    @GetMapping("/api/sysConfig/getPage")
    public ResultVo getPage(SysConfig query, Integer pageNum, Integer pageSize) {
        PageInfo<SysConfig> sysConfigPage = sysConfigService.getPage(query, pageNum, pageSize);
        return ResultVo.successWith(sysConfigPage);
    }

    @SysLogRecord("根据键获取配置项")
    @ApiOperation("根据键获取配置项")
    @GetMapping("/api/sysConfig/getValueByKey")
    public ResultVo getValueByKey(String key) {
        Map<String, String> sysConfigs = sysConfigService.selectMap();
        String value = sysConfigs.get(key);
        return ResultVo.successWith(value);
    }

    @SysLogRecord("根据键更新配置项")
    @ApiOperation("根据键更新配置项")
    @PostMapping("/api/sysConfig/updateValueByKey")
    public ResultVo updateValueByKey(@RequestBody SysConfig sysConfig) {
        int rows = sysConfigService.updateValueByKey(sysConfig);
        if (rows > 0) {
            return ResultVo.success("保存成功！");
        } else {
            return ResultVo.fail("保存失败！");
        }
    }
}
