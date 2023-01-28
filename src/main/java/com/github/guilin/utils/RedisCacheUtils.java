package com.github.guilin.utils;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * AOP工具类
 *
 * @author CaoChenLei
 */
public class RedisCacheUtils {
    public static String getKey(ProceedingJoinPoint point, String prefix) throws NoSuchMethodException {
        StringBuilder key = new StringBuilder();
        key.append(getKeyOfClassPrefix(point, prefix));
        key.append("_");
        key.append(getMethod(point).getName());
        key.append(getMethodArgs(point.getArgs()));
        return key.toString();
    }

    public static String getKeyOfClassPrefix(ProceedingJoinPoint point, String prefix) {
        StringBuilder keyPrefix = new StringBuilder();
        if (!StringUtils.isEmpty(prefix)) {
            keyPrefix.append(prefix);
        }
        keyPrefix.append(getClassName(point));
        return keyPrefix.toString();
    }

    public static String getClassName(ProceedingJoinPoint point) {
        return point.getTarget().getClass().getName().replaceAll("\\.", "_");
    }

    public static Method getMethod(ProceedingJoinPoint point) throws NoSuchMethodException {
        Signature signature = point.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        return point.getTarget().getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
    }

    private static String getMethodArgs(Object... params) {
        // 没有参数
        if (params == null) {
            return "";
        }
        // 含有参数
        StringBuilder key = new StringBuilder();
        key.append("(");
        for (Object param : params) {
            String json = JsonUtils.toString(param);
            json = json.replaceAll("\"", "'");
            json = json.replaceAll(":", "_");
            json = json.concat("_");
            key.append(JsonUtils.toString(json));
        }
        key.append(")");
        return key.toString().replace("_)", ")");
    }
}
