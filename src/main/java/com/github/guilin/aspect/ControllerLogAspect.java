package com.github.guilin.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Aspect
@Component
public class ControllerLogAspect {
    @Pointcut("execution(public * com.github.guilin.controller..*.*(..))")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object handle(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String className = proceedingJoinPoint.getTarget().getClass().getSimpleName();
        String methodName = proceedingJoinPoint.getSignature().getName();
        StopWatch stopWatch = new StopWatch();
        Object result;
        try {
            stopWatch.start();
            result = proceedingJoinPoint.proceed();
            stopWatch.stop();
        } catch (Exception exception) {
            log.error("[{}][{}] handling exception message is: {}", className, methodName, exception.getMessage());
            throw exception;
        } catch (Throwable throwable) {
            log.error("[{}][{}] handling Throwable message is: {}", className, methodName, throwable.getMessage());
            throw throwable;
        } finally {
            if (stopWatch.isRunning()) {
                stopWatch.stop();
            }
            long timeMillis = stopWatch.getLastTaskTimeMillis();
            log.info("[{}][{}][{}][{}ms]", className, methodName, request.getQueryString(), timeMillis);
        }
        return result;
    }
}
