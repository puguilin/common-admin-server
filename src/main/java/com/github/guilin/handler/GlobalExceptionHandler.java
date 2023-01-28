package com.github.guilin.handler;

import com.github.guilin.common.Constant;
import com.github.guilin.domain.entity.SysLog;
import com.github.guilin.domain.entity.SysUser;
import com.github.guilin.domain.vo.ResultVo;
import com.github.guilin.service.SysLogAsyncService;
import com.github.guilin.utils.IpUtils;
import com.github.guilin.utils.RegionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @Resource
    private HttpServletRequest request;
    @Resource
    private SysLogAsyncService sysLogAsyncService;
    @Value("${sysLogRecord.enable}")
    private Boolean sysLogRecordEnable;

    @ExceptionHandler(AccessDeniedException.class)
    public ResultVo accessDeniedException(AccessDeniedException e) {
        log.info("Access denied exception: {}", e.getMessage());
        record(e, "用户权限不足");
        return ResultVo.failWith(Constant.ACCESS_DENIED_EXCEPTION, "用户权限不足！");
    }

    @ExceptionHandler(IOException.class)
    public ResultVo iOException(IOException e) {
        log.info("IO exception: {}", e.getMessage());
        record(e, "文件操作失败");
        return ResultVo.failWith(Constant.RUNTIME_EXCEPTION, "文件上传失败！");
    }

    @ExceptionHandler(RuntimeException.class)
    public ResultVo runtimeException(RuntimeException e) {
        log.info("Server runtime exception: {}", e.getMessage());
        record(e, "服务出现异常");
        return ResultVo.failWith(Constant.RUNTIME_EXCEPTION, "服务出现异常！");
    }

    public void record(Exception e, String title) {
        if (sysLogRecordEnable) {
            SysLog sysLog = new SysLog();
            // record type
            sysLog.setType(1);
            // client info
            String remoteIp = IpUtils.getRemoteIp(request);
            String remoteRegion = RegionUtils.getRemoteRegion(remoteIp);
            sysLog.setRemoteIp(remoteIp);
            sysLog.setRemoteRegion(remoteRegion);
            // create by
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            SysUser sysUser = (SysUser) authentication.getPrincipal();
            sysLog.setCreateBy(sysUser.getId());
            // other info
            sysLog.setTitle(title);
            sysLog.setMapping(request.getRequestURI());
            sysLog.setMode(request.getMethod());
            sysLog.setException(e.getMessage());
            sysLogAsyncService.add(sysLog);
        }
    }
}
