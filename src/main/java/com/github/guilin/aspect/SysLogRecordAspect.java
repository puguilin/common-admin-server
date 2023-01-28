package com.github.guilin.aspect;

import com.alibaba.fastjson.JSON;
import com.github.guilin.annotation.SysLogRecord;
import com.github.guilin.domain.entity.SysLog;
import com.github.guilin.domain.entity.SysUser;
import com.github.guilin.service.SysLogAsyncService;
import com.github.guilin.utils.IpUtils;
import com.github.guilin.utils.JsonUtils;
import com.github.guilin.utils.RegionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Aspect
@Component
public class SysLogRecordAspect {
    @Resource
    private HttpServletRequest request;
    @Resource
    private SysLogAsyncService sysLogAsyncService;
    @Value("${sysLogRecord.enable}")
    private Boolean sysLogRecordEnable;
    @Value("${sysLogRecord.params}")
    private Boolean sysLogRecordParams;
    @Value("${sysLogRecord.result}")
    private Boolean sysLogRecordResult;

    @Pointcut("@annotation(com.github.guilin.annotation.SysLogRecord)")
    public void pointAspect() {
    }

    @Around("pointAspect()")
    public Object handle(ProceedingJoinPoint point) throws Throwable {
        // normal execution
        LocalDateTime startTime = LocalDateTime.now();
        Object result = point.proceed();
        LocalDateTime endTime = LocalDateTime.now();

        // system log record
        if (sysLogRecordEnable) {
            record(point, 0, result, null, startTime, endTime);
        }

        return result;
    }

    @AfterThrowing(value = "pointAspect()", throwing = "e")
    public void hasException(JoinPoint point, Throwable e) {
        // system log record
        if (sysLogRecordEnable) {
            record(point, 1, null, e.getMessage(), null, null);
        }
    }

    public void record(JoinPoint point, Integer type, Object result, String exception, LocalDateTime startTime, LocalDateTime endTime) {
        SysLog sysLog = new SysLog();
        // record type
        sysLog.setType(type);
        // run time
        sysLog.setStartTime(startTime);
        sysLog.setEndTime(endTime);
        // client info
        String remoteIp = IpUtils.getRemoteIp(request);
        String remoteRegion = RegionUtils.getRemoteRegion(remoteIp);
        sysLog.setRemoteIp(remoteIp);
        sysLog.setRemoteRegion(remoteRegion);
        // create by
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SysUser sysUser = (SysUser) authentication.getPrincipal();
        sysLog.setCreateBy(sysUser.getId());
        // method info
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        String[] methodNameSplit = method.toGenericString().split(" ");
        String methodName = methodNameSplit[methodNameSplit.length - 1];
        int indexOf = methodName.indexOf("(");
        methodName = methodName.substring(0, indexOf);
        SysLogRecord record = method.getAnnotation(SysLogRecord.class);

        sysLog.setTitle(record.value());
        sysLog.setMapping(request.getRequestURI());
        sysLog.setMode(request.getMethod());
        sysLog.setMethod(methodName);

        // opening is not recommended
        if (sysLogRecordParams) {
            Map<String, Object> requestParams = new HashMap<String, Object>();
            String[] argNames = signature.getParameterNames();
            Object[] args = point.getArgs();
            for (int i = 0; i < argNames.length; i++) {
                if (args[i] instanceof ServletRequest || args[i] instanceof ServletResponse || args[i] instanceof MultipartFile) {
                    requestParams.put(argNames[i], argNames[i]);
                } else {
                    requestParams.put(argNames[i], args[i]);
                }
            }
            if (requestParams.size() > 0) {
                sysLog.setParams(JsonUtils.toString(requestParams));
                log.debug("methodName ==> " + JSON.toJSONString(methodName));
                log.debug("args ==> " + JSON.toJSONString(requestParams));
            }
        }

        // opening is not recommended
        if (sysLogRecordResult && result != null) {
            sysLog.setResult(JsonUtils.toString(result));
        }

        // set exception
        if (!StringUtils.isEmpty(exception)) {
            sysLog.setException(exception);
        }

        sysLogAsyncService.add(sysLog);
    }
}
