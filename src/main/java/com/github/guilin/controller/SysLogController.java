package com.github.guilin.controller;

import com.github.pagehelper.PageInfo;
import com.github.guilin.annotation.SysLogRecord;
import com.github.guilin.domain.entity.SysLog;
import com.github.guilin.domain.vo.ResultVo;
import com.github.guilin.service.SysLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@Api(tags = "系统日志接口")
@RestController
public class SysLogController {
    @Resource
    private SysLogService sysLogService;

    @SysLogRecord("根据主键删除日志")
    @PreAuthorize("hasAuthority('sys:log:delete')")
    @ApiOperation("根据主键删除日志")
    @GetMapping("/api/sysLog/deleteById")
    public ResultVo deleteById(Long id) {
        int rows = sysLogService.deleteById(id);
        if (rows > 0) {
            return ResultVo.success("删除成功！");
        } else {
            return ResultVo.fail("删除失败！");
        }
    }

    @PreAuthorize("hasAuthority('sys:log:view')")
    @ApiOperation("根据主键获取日志")
    @GetMapping("/api/sysLog/getById")
    public ResultVo getById(Long id) {
        SysLog sysLog = sysLogService.getById(id);
        return ResultVo.successWith(sysLog);
    }

    @ApiOperation("分页获取日志列表")
    @GetMapping("/api/sysLog/getPage")
    public ResultVo getPage(SysLog query, Integer pageNum, Integer pageSize) {
        PageInfo<SysLog> sysLogPage = sysLogService.getPage(query, pageNum, pageSize);
        return ResultVo.successWith(sysLogPage);
    }

    @SysLogRecord("清空系统日志信息")
    @PreAuthorize("hasAuthority('sys:log:clear')")
    @ApiOperation("清空系统日志信息")
    @GetMapping("/api/sysLog/clear")
    public ResultVo clear() {
        int rows = sysLogService.clear();
        if (rows > 0) {
            return ResultVo.success("清空成功！");
        } else {
            return ResultVo.fail("清空失败！");
        }
    }
}

