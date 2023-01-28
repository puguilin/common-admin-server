package com.github.guilin.aspect;

import com.github.guilin.annotation.RedisCache;
import com.github.guilin.common.Constant;
import com.github.guilin.utils.RedisCacheUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Set;

@Slf4j
@Aspect
@Component
public class RedisCacheAspect {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Pointcut("@annotation(com.github.guilin.annotation.RedisCache)")
    public void pointcut() {}

    @Around("pointcut()")
    public Object handle(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        RedisCache cache = method.getAnnotation(RedisCache.class);
        //缓存键名
        String cacheKey = RedisCacheUtils.getKey(point, Constant.REDIS_CACHE_PREFIX);
        //是否刷新
        if (cache.flush()) {
            String classPrefix = RedisCacheUtils.getKeyOfClassPrefix(point, Constant.REDIS_CACHE_PREFIX);
            Set<String> keys = redisTemplate.keys(classPrefix + "*");
            redisTemplate.delete(keys);
            log.debug("清除缓存：{}", keys);
            return point.proceed();
        }
        //分支判断
        Boolean hasKey = redisTemplate.hasKey(cacheKey);
        if (hasKey) {
            Object result = redisTemplate.opsForValue().get(cacheKey);
            log.debug("从缓存中获取：{}", cacheKey);
            return result;
        } else {
            Object result = point.proceed();
            log.debug("从数据库获取：{}", cacheKey);
            redisTemplate.opsForValue().set(cacheKey, result, cache.expire(), cache.unit());
            log.debug("新增缓存：{}", cacheKey);
            return result;
        }
    }
}
