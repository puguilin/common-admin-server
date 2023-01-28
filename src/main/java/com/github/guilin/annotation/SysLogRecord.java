package com.github.guilin.annotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SysLogRecord {
    /**
     * 业务的名称，默认为空串
     */
    String value() default "";
}
